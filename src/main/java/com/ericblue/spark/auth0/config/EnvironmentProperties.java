package com.ericblue.spark.auth0.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentProperties {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentProperties.class);

    public EnvironmentProperties() {
       
    }

    /**
     * Returns a properties file for the specified environment
     * -Denvironment=production or -Denvironment=test
     * Default properties file return if not supplied is application.live.properties
     * 
     * @return the properties file with environment-specific information
     */
    public  Properties loadProperties() {

        Map<String, String> envVariables = System.getenv();

        String env = envVariables.get("environment");
        logger.info("Environment = " + env);
        String defaultPropertiesFile = "application.production.properties";
        String propertiesFile = null;
        if (env != null) {
        	if (env.equals("development")) {
                propertiesFile = "application.development.properties";
            }
            if (env.equals("test")) {
                propertiesFile = "application.test.properties";
            }
            if (env.equals("production")) {
                propertiesFile = "application.production.properties";
            }
        } else {
            propertiesFile = defaultPropertiesFile;
        }

        logger.info("Loading application properties from file " + propertiesFile);

        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(propertiesFile);
        try {
            prop.load(stream);
        } catch (IOException e) {
            logger.error("Caught IOException: " + e);
            throw new RuntimeException(e);
        }

        return prop;

    }

}
