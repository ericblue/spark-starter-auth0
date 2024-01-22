package com.ericblue.spark.auth0.controllers.swagger;

import com.ericblue.spark.auth0.controllers.base.BaseController;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericblue.spark.auth0.swagger.SwaggerParser;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import static spark.Spark.get;


@SwaggerDefinition(info = @Info(description = "Spark Starter API", //
				version = "V1.0", //
				title = "Spark Starter API", //
				contact = @Contact(name = "Eric Blue", url = "https://eric-blue.com") ) , //
		schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS }, //
		consumes = { "application/json" }, //
		produces = { "application/json" }, //
		tags = { @Tag(name = "swagger") })

/* Manually add Swagger support to Spark based on this post:
   https://web.archive.org/web/20210423041722/https://serol.ro/posts/2016/swagger_sparkjava/
*/

public class SwaggerController extends BaseController {

	/**
	 * Logger for this class
	 */
	protected static final Logger logger = LoggerFactory.getLogger(SwaggerController.class);

	public static final String APP_PACKAGE = "com.ericblue.spark.auth0";

	public SwaggerController() {

		logger.debug("Initializing Controller " + this.getClass().getName());
		this.defineRoutes();
	}

	protected void defineRoutes() {


		get("/swagger", (request, response) -> {

			String swaggerJson = null;

			try {
				swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}

			return swaggerJson;

		});
		
		
		

	}

}
