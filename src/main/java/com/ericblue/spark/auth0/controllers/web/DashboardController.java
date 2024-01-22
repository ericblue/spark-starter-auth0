package com.ericblue.spark.auth0.controllers.web;

import com.ericblue.spark.auth0.controllers.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;


public class DashboardController extends BaseController {

	/**
	 * Logger for this class
	 */
	protected static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	public DashboardController() {

		logger.debug("Initializing Controller " + this.getClass().getName());
		this.defineRoutes();
	}

	protected void defineRoutes() {



		get("/dashboard", (request, response) -> {

			Map<String, Object> attributes = new HashMap<>();
			attributes.put("userClaims", request.session().attribute("userClaims"));


			return render(request, attributes, "dashboard.mst");

		});
		
		
		

	}

}
