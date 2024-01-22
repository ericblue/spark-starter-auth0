package com.ericblue.spark.auth0.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Application configuration model")
public class AppConfiguration {

    @ApiModelProperty(value = "Application version", required = true)
    private String appVersion;

    @ApiModelProperty(value = "Location of static files", required = true)
    private String staticFileLocation;

    @ApiModelProperty(value = "WebSocket endpoint location")
    private String webSocketLocation;

    @ApiModelProperty(value = "HTTP port", required = true)
    private Integer httpPort;

    @ApiModelProperty(value = "Whether to cache templates", required = true)
    private Boolean cacheTemplates;

    @ApiModelProperty(value = "Application environment", allowableValues = "DEVELOPMENT, TEST, PRODUCTION")
    private AppEnvironment environment;

    @ApiModelProperty(value = "Auth0 Client ID", required = true)
    private String auth0ClientID;

    @ApiModelProperty(value = "Auth0 Client Secret", required = true)
    private String auth0ClientSecret;

    @ApiModelProperty(value = "Auth0 Issuer URI", required = true)
    private String auth0IssuerURI;

    @ApiModelProperty(value = "Auth0 Callback URL", required = true)
    private String auth0CallbackURL;

    public AppConfiguration() {
        this.staticFileLocation = "/public";
        this.httpPort = 4567;
        this.cacheTemplates = true;
    }

    public String getStaticFileLocation() {
        return staticFileLocation;
    }

    public void setStaticFileLocation(String staticFileLocation) {
        this.staticFileLocation = staticFileLocation;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }

    public Boolean getCacheTemplates() {
        return cacheTemplates;
    }

    public void setCacheTemplates(Boolean cacheTemplates) {
        this.cacheTemplates = cacheTemplates;
    }

    public String getWebSocketLocation() {
        return webSocketLocation;
    }

    public void setWebSocketLocation(String webSocketLocation) {
        this.webSocketLocation = webSocketLocation;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public AppEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(AppEnvironment environment) {
        this.environment = environment;
    }

    public String getAuth0ClientID() {
        return auth0ClientID;
    }

    public void setAuth0ClientID(String auth0ClientID) {
        this.auth0ClientID = auth0ClientID;
    }

    public String getAuth0ClientSecret() {
        return auth0ClientSecret;
    }

    public void setAuth0ClientSecret(String auth0ClientSecret) {
        this.auth0ClientSecret = auth0ClientSecret;
    }

    public String getAuth0IssuerURI() {
        return auth0IssuerURI;
    }

    public void setAuth0IssuerURI(String auth0IssuerURI) {
        this.auth0IssuerURI = auth0IssuerURI;
    }

    public String getAuth0CallbackURL() {
        return auth0CallbackURL;
    }

    public void setAuth0CallbackURL(String auth0CallbackURL) {
        this.auth0CallbackURL = auth0CallbackURL;
    }
}
