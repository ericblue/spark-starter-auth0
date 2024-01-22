package com.ericblue.spark.auth0.config;

import com.ericblue.spark.auth0.controllers.api.SampleAPIController;
import com.ericblue.spark.auth0.controllers.auth0.AuthController;
import com.ericblue.spark.auth0.controllers.base.BaseController;
import com.ericblue.spark.auth0.controllers.error.ErrorController;
import com.ericblue.spark.auth0.controllers.swagger.SwaggerController;
import com.ericblue.spark.auth0.controllers.web.DashboardController;
import com.ericblue.spark.auth0.controllers.web.IndexController;
import com.ericblue.spark.auth0.controllers.websocket.WebSocketController;
import com.ericblue.spark.auth0.service.SampleAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.util.ArrayList;

import static spark.Spark.webSocket;

public class WebConfig {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	private static SampleAPIService sampleAPIService;

	private static AppConfiguration config = new AppConfigurationLoader().load();

	public WebConfig(SampleAPIService sampleAPIService) {
		WebConfig.sampleAPIService = sampleAPIService;

		initWebServer();
	}

	public static void initWebServer() {

		Spark.staticFiles.location(config.getStaticFileLocation());
		//Spark.staticFiles.expireTime(86400); 
		Spark.port(config.getHttpPort());

		logger.info("Listening on port " + config.getHttpPort() + ", Environment = " + config.getEnvironment());

		// Set Web Sockets
		webSocket(config.getWebSocketLocation(), WebSocketController.class);

		// Set Controllers
		BaseController.setConfig(config);
		BaseController.setSampleAPIService(sampleAPIService);

		// Non-standard setup of controllers to support multiple controllers rather that defining all methods here

		// Important: All Controllers must be added to this list since this initializes routes

		ArrayList<Class> controllers = new ArrayList<Class>() {{
			add(AuthController.class);
			add(SwaggerController.class);
			add(SampleAPIController.class);
			add(IndexController.class);
			add(DashboardController.class);
			add(WebSocketController.class);
			// Make sure Error Controller is last
			add(ErrorController.class);
		}};
		
		loadControllers(controllers);


	}

	// Note: Temporarily doing this since there are loading issues if we make
	// controllers spring-managed
	// ErrorController must always be last due to 404 catch all route

	public static void loadControllers(ArrayList<Class> controllers) {

		for (Class<?> clazz : controllers) {
			logger.info("Loading controller: " + clazz.getName());

			Object c = null;

			try {
				c = Class.forName(clazz.getName()).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load controller: Reason = " + e);
			}
		}

	}

}
