package com.ericblue.spark.auth0.controllers.auth0;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.auth.UserInfo;
import com.auth0.json.mgmt.roles.Role;
import com.auth0.json.mgmt.roles.RolesPage;
import com.auth0.json.mgmt.users.User;
import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.net.TokenRequest;
import com.ericblue.spark.auth0.config.AppConfiguration;
import com.ericblue.spark.auth0.controllers.api.SampleAPIController;
import com.ericblue.spark.auth0.controllers.base.BaseController;
import com.ericblue.spark.auth0.utils.Dumper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class AuthController extends BaseController {

    /**
     * Logger for this class
     */
    protected static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private AuthAPI auth;
    private JWTVerifier jwtVerifier;

    // JwkProvider required for RS256 tokens. If using HS256, do not use.
    private JwkProvider jwkProvider;


    public AuthController() {

        logger.debug("Initializing Controller " + this.getClass().getName());


        AppConfiguration config = getConfig();


        auth = new AuthAPI(config.getAuth0IssuerURI(),
                config.getAuth0ClientID(),
                config.getAuth0ClientSecret());

        // Define routes - map URIs to method handlers

        this.defineRoutes();

    }

    public void defineRoutes() {

        // Auth Filter to check authentication
        before((request, response) -> {

            logger.info("Got request for " + request.pathInfo());
            Boolean loggedIn = false;
            if (request.session().attribute("loggedIn") == null) {
                loggedIn = false;
            } else {
                if (request.session().attribute("loggedIn").equals(true)) {
                    loggedIn = true;
                }
            }


            // Make sure to add login, logout, and callback URLs to avoid infinite redirect loops

            ArrayList<String> validUrls = new ArrayList<>();
            validUrls.add("/login");
            validUrls.add("/logout");
            validUrls.add("/callback");
            validUrls.add("/error");
            validUrls.add("/swagger");
            validUrls.add("/swagger-ui/");
            validUrls.add("/");

            String urlRegex = String.join("|", validUrls);

            //logger.debug("Checking if " + request.pathInfo() + " matches valid urls.");

            if ((!loggedIn) && ((!request.pathInfo().matches(urlRegex)))) {
                logger.debug("Not logged in (path = " + request.pathInfo() + ").  Redirecting to /login ");
                response.redirect("/login");
            }

        });


        get("/login", this::login);

        get("/callback", this::callback);

        get("/logout", this::logout);


    }

    public Object login(Request request, Response response) {

        String authorizeUrl = auth.authorizeUrl(config.getAuth0CallbackURL())
                .withScope("openid profile email")
                .build();
        response.redirect(authorizeUrl);
        return null;
    }

    public Object callback(Request request, Response response) {
        String code = request.queryParams("code");

        // Generate a token request given the supplied code

        DecodedJWT decodedToken = null;

        TokenRequest tokenRequest = auth.exchangeCode(code, config.getAuth0CallbackURL());
        logger.info("tokenRequest: " + Dumper.Dump(tokenRequest));

        // Get the access token (not used) and ID token so this can be decoded as a JWT

        try {
            com.auth0.net.Response<TokenHolder> tokenHolderResponse = tokenRequest.execute();
            logger.info("tokenHolderResponse: " + Dumper.Dump(tokenHolderResponse.getBody()));

            String idToken = tokenHolderResponse.getBody().getIdToken();
            String accessToken = tokenHolderResponse.getBody().getAccessToken();

            // TODO - Verify token + JWKS
            decodedToken = JWT.decode(idToken);
            logger.info("decodedToken auth: " + Dumper.Dump(decodedToken));
            decodedToken.getClaims().forEach((k, v) -> logger.info("key: " + k + " value: " + v));

            // Add all default claims to the request session
            request.session().attribute("userClaims", decodedToken.getClaims());
            request.session().attribute("loggedIn", true);

            // Note, to get user role information as part of the JWT you need to add a custom Flow in Auth0
            // See: https://auth0.com/docs/customize/actions/flows-and-triggers/login-flow#add-user-roles-to-id-and-access-tokens
            // And https://community.auth0.com/t/unable-to-get-role-information-back-in-jwt-using-java-2-x-apis/125316

            // Example:
            // exports.onExecutePostLogin = async (event, api) => {
            //  const namespace = "https://auth.mydomain.com";
            //  if (event.authorization) {
            //    api.idToken.setCustomClaim("preferred_username", event.user.email);
            //    api.idToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles);
            //    api.accessToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles);
            //  }
            //};

            // The return JWT custom claim key will be https://auth.mydomain.com/roles and value will be a list of
            // roles (e.g. ["admin", "user"])


        } catch (Auth0Exception e) {
            throw new RuntimeException(e);
        }

        // Get user role info - make sure you application has access to the Auth0 Management API
        // Applications -> APIs -> Auth0 Management API -> Machine to Machine Applications -> <your app> -> Enable

        try {

            // Get token for management API - make sure the application has access to the Management API

            logger.info("decodedToken authn: " + Dumper.Dump(decodedToken));

            TokenRequest mgmtTokenRequest = auth.requestToken("https://" + config.getAuth0IssuerURI() + "/api/v2/");
            TokenHolder holder = mgmtTokenRequest.execute().getBody();
            String mgmtAccessToken = holder.getAccessToken();
            logger.info("mgmtAccessToken: " + mgmtAccessToken);

            ManagementAPI mgmt = ManagementAPI.newBuilder(config.getAuth0IssuerURI(), mgmtAccessToken).build();

            // Get user info from Management API
            User user = mgmt.users().get(decodedToken.getClaim("sub").asString(), new UserFilter()).execute().getBody();
            logger.info("user: " + Dumper.Dump(user));

            logger.info("Getting role information for user: " + decodedToken.getClaim("sub").asString());

            // Get roles from Management API given a sub id - e.g. auth0|65a9e8733d944427580bbd32a
            RolesPage roles = mgmt.users().listRoles(decodedToken.getClaim("sub").asString(), null).execute().getBody();


            roles.getItems().forEach((r) -> logger.info("role: " + Dumper.Dump(r)));

            if (roles.getItems().size() > 0) {
                HashMap<String, Object> userRoles = new HashMap<>();
                userRoles.put("id", roles.getItems().get(0).getId());
                userRoles.put("name", roles.getItems().get(0).getName());
                userRoles.put("description", roles.getItems().get(0).getDescription());
                request.session().attribute("userRoles", userRoles);
            }

        } catch (Auth0Exception e) {
            logger.info("Caught Auth0Exception: " + e.getMessage());
        }


        response.redirect("/dashboard");
        return null;
    }

    public Object logout(Request request, Response response) {
        logger.info("Got request to logout directly");
        request.session().invalidate();
        response.redirect("/");
        return null;
    }


}
