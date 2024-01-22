package com.ericblue.spark.auth0.app;


import com.ericblue.spark.auth0.config.WebConfig;
import com.ericblue.spark.auth0.service.SampleAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final SampleAPIService sampleAPIService = new SampleAPIService();


    private void run() {

        logger.info("Starting Spark Starter Application");

        // Note: Since there is no dependency injection framework like Spring, we'll need to manually inject
        // the example service so it's available to controllers.  For more sophisticated applications, a DI
        // framework is recommended.

          new WebConfig(sampleAPIService);


    }


    public static void main(String[] args) {

        Application client = new Application();
        try {
            client.run();
        } catch (Exception e) {
            System.err.println("Can't start application: Error = " + e);
            System.exit(1);
        }

    }

}
