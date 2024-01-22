package com.ericblue.spark.auth0.controllers.error;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.HashMap;
import java.util.Map;

import com.ericblue.spark.auth0.controllers.base.BaseController;
import com.ericblue.spark.auth0.controllers.response.RestResponse;
import com.ericblue.spark.auth0.controllers.response.RestResponseType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ericblue.spark.auth0.exception.AppJSONException;
import com.ericblue.spark.auth0.utils.DataEncodeUtils;

import spark.Request;
import spark.Response;

public class ErrorController extends BaseController {

	/**
	 * Logger for this class
	 */
	protected static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

	public ErrorController() {

		logger.debug("Initializing Controller " + this.getClass().getName());
		this.defineRoutes();
	}

	protected void processExeception(Exception e, Request request, Response response, Boolean isJson) {

		logger.debug("Processing Exception " + e.getClass().getCanonicalName());

		String reason = e.getMessage();
		reason += " (" + e.getClass() + ")";

		String fullStackTrace = ExceptionUtils.getStackTrace(e);
		fullStackTrace = fullStackTrace.replaceAll("\\r\\n", "|");
		fullStackTrace = fullStackTrace.replaceAll("\\n", "|");

		response.status(500);

		String redirectUrl = "http://localhost:" + config.getHttpPort();
		redirectUrl += "/error?reason=" + reason;
		redirectUrl += "&stack=" + fullStackTrace;
		if (isJson) {
			redirectUrl += "&json=true";
		}

		logger.debug("Processing exception, redirecting to " + redirectUrl);

		response.status(500);
		response.redirect(redirectUrl);

	}

	private String renderError(Request request, Response response) {

		Map<String, Object> attributes = new HashMap<>();

		String reason = request.queryParams("reason");
		if (StringUtils.isBlank(reason)) {
			reason = "Unknown";
		}

		String stack = request.queryParams("stack");

		String json = request.queryParams("json");
		if ((json != null) && ((json.equals("true")))) {
			RestResponse restResponse = new RestResponse();
			restResponse.setResponseType(RestResponseType.FAILURE);

			HashMap<String, Object> message = new HashMap<String, Object>();
			message.put("reason", reason);
			message.put("stack", stack);

			restResponse.setResponse(message);

			// See JSON serialization example at
			// https://sparktutorials.github.io/2015/06/01/spark-freemarker.html
			response.status(500);
			response.type("application/json");
			return DataEncodeUtils.dataToJson(restResponse);

		} else {

			attributes.put("reason", reason);
			attributes.put("stack", stack);

			response.status(500);
			return render(request, attributes, "500.mst");
		}

	}

	protected Boolean isRequestJson(Request request) {

		String contentType = request.headers("Content-Type");
		if (contentType != null) {
			if (contentType.equals("application/json")) {
				logger.debug("Checking content type isRequestJson: true");
				return true;
			} else {
				logger.debug("Checking content type isRequestJson: false");
				return false;
			}
		} else {
			logger.debug("Checking content type isRequestJson: failed, couldn't find content type");
			return false;
		}

	}

	protected void defineRoutes() {

		// Multiple definitions needed since 302 redirects happen on Exceptions,
		// and the HTTP method at the time of the operation will follow

		// 500 - GET
		get("/error", (request, response) -> {

			return renderError(request, response);

		});

		// 500 - PUT
		put("/error", (request, response) -> {

			return renderError(request, response);

		});

		// 500 - PUT
		post("/error", (request, response) -> {

			return renderError(request, response);

		});

		// 500 - PUT
		delete("/error", (request, response) -> {

			return renderError(request, response);

		});

		// Generic Exception handler

		exception(NullPointerException.class, (e, request, response) -> {

			logger.error("Caught NullPointer " + e);
			Integer line = e.getStackTrace()[0].getLineNumber();
			String file = e.getStackTrace()[0].getFileName();
			String method = e.getStackTrace()[0].getMethodName();
			
			Exception np = new Exception("Null pointer on line " + line + ", method = " + method + ", file = " + file);
			processExeception(np, request, response, true);

		});

		exception(RuntimeException.class, (e, request, response) -> {

			logger.error("Caught RuntimeException " + e);
			processExeception(e, request, response, isRequestJson(request));

		});

		exception(Exception.class, (e, request, response) -> {

			logger.error("Caught Exception " + e);
			processExeception(e, request, response, isRequestJson(request));

		});

		exception(AppJSONException.class, (e, request, response) -> {

			logger.error("Caught AppJSONException " + e);
			processExeception(e, request, response, true);

		});

		// 404 - catchall - make sure this is the last defined route

		// TODO - Figure out how to re-enable: Conflicts with enabling
		// websockets since it interferes with catchall routes

		get("*", (request, response) -> {

			String pathInfo = request.raw().getPathInfo();

			// Exclude web sockets location from 404 check due to Spark bug
			// See: https://github.com/perwendel/spark/issues/502
			if (!pathInfo.equals(config.getWebSocketLocation())) {

				logger.debug("Caught 404 for " + pathInfo);

				Map<String, Object> attributes = new HashMap<>();

				response.status(404);
				return render(request, attributes, "404.mst");
			} else {
				return null;
			}

		});

	}

}
