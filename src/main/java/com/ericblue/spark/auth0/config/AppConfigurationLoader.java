package com.ericblue.spark.auth0.config;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppConfigurationLoader {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(AppConfigurationLoader.class);

    private AppConfiguration config = new AppConfiguration();

    public AppConfigurationLoader() {

    }

    public AppConfiguration load() {

        Properties prop = new EnvironmentProperties().loadProperties();
        logger.debug(prop.toString());

        // App version
        config.setAppVersion(prop.getProperty("appVersion"));
        if (StringUtils.isBlank(config.getAppVersion())) {
            throw new RuntimeException("Couldn't determine app version.  Aborting.");
        }
        logger.debug("Starting app version " + config.getAppVersion());

        // Static Files
        config.setStaticFileLocation(prop.getProperty("staticFileLocation"));
        if (StringUtils.isBlank(config.getStaticFileLocation())) {
            throw new RuntimeException("Couldn't determine static file location.  Aborting.");
        }
        logger.debug("Starting server with static file location " + config.getStaticFileLocation());

        // Port Number
        String httpPort = prop.getProperty("httpPort");
        if (StringUtils.isBlank(httpPort)) {
            throw new RuntimeException("Couldn't determine http port.  Aborting.");
        }

        config.setHttpPort(Integer.parseInt(httpPort));
        logger.debug("Starting server with port " + config.getHttpPort());

        // ViewCaching
        String cacheTemplates = prop.getProperty("cacheTemplates");
        if (StringUtils.isBlank(cacheTemplates)) {
            throw new RuntimeException("Couldn't determine cacheTemplates flag.  Aborting.");
        }
        if (cacheTemplates.equals("true")) {
            config.setCacheTemplates(true);
        }
        if (cacheTemplates.equals("false")) {
            config.setCacheTemplates(false);
        }
        logger.debug("Setting template caching to " + config.getCacheTemplates());

        // Web Socket Location
        config.setWebSocketLocation(prop.getProperty("webSocketLocation"));
        if (StringUtils.isBlank(config.getWebSocketLocation())) {
            throw new RuntimeException("Couldn't determine web socket file location.  Aborting.");
        }
        logger.debug("Starting server with web socket location " + config.getWebSocketLocation());

        // App Environment
        String environment = prop.getProperty("environment");
        if (StringUtils.isBlank(environment)) {
            throw new RuntimeException("Couldn't determine environment.  Aborting.");
        }
        if (environment.equals("DEVELOPMENT")) {
            config.setEnvironment(AppEnvironment.DEVELOPMENT);
        }
        if (environment.equals("TEST")) {
            config.setEnvironment(AppEnvironment.TEST);
        }
        if (environment.equals("PRODUCTION")) {
            config.setEnvironment(AppEnvironment.PRODUCTION);
        }

        // Load Auth0 properties

        String auth0ClientID = prop.getProperty("auth0-client-id");
        config.setAuth0ClientID(auth0ClientID);

        String auth0ClientSecret = prop.getProperty("auth0-client-secret");
        config.setAuth0ClientSecret(auth0ClientSecret);

        String auth0IssuerURI = prop.getProperty("auth0-issuer-uri");
        config.setAuth0IssuerURI(auth0IssuerURI);

        String auth0CallbackURL = prop.getProperty("auth0-callback-url");
        config.setAuth0CallbackURL(auth0CallbackURL);

        logger.debug("Setting Environment to " + config.getEnvironment());

        return config;

    }

}
