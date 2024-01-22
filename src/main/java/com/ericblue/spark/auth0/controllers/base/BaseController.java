package com.ericblue.spark.auth0.controllers.base;

import java.util.Map;

import com.ericblue.spark.auth0.service.SampleAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ericblue.spark.auth0.config.AppConfiguration;
import com.ericblue.spark.auth0.config.AppEnvironment;


import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

public class BaseController {

	/**
	 * Logger for this class
	 */
	protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	protected static SampleAPIService sampleAPIService;

	protected MustacheTemplateEngine mustacheEngine;
	protected static AppConfiguration config = null;

	protected BaseController() {

		initMustache();

	}

	// MustacheTemplateEngine defaults to src/main/resources/template.
	// To customize, we'll need to extend? See
	// https://github.com/perwendel/spark-template-engines/blob/master/spark-template-mustache/
	// /src/test/java/spark/template/mustache/MustacheTemplateExample.java
	private void initMustache() {
		mustacheEngine = new MustacheTemplateEngine();

	}

	public static AppConfiguration getConfig() {
		return config;
	}

	public static void setConfig(AppConfiguration c) {
		config = c;
	}

	
	protected String redirect(Response response, String url) {
		
		response.redirect(url);;
		return "";
		
	}

	@SuppressWarnings("unchecked")
    protected String render(Request request, Map attributes, String view) {

	    
	    // Add default attributes to each render request
	    attributes.put("appVersion", getConfig().getAppVersion());
	    if (getConfig().getEnvironment().equals(AppEnvironment.DEVELOPMENT)) {
	    	attributes.put("development", true);
	    }
	    if (getConfig().getEnvironment().equals(AppEnvironment.TEST)) {
	    	attributes.put("test", true);
	    }
	    if (getConfig().getEnvironment().equals(AppEnvironment.PRODUCTION)) {
	    	attributes.put("production", true);
	    }
	
	    attributes.put("environment", getConfig().getEnvironment());
	    
	    logger.debug("Setting attributes " + attributes);
	    
		// Mustache caches all templates. There is no config or easy workaround
		// other than to re-initialize the template engine each time
		// See: https://github.com/spullara/mustache.java/issues/57
		// For development only. Set caching to true for production;
	    //logger.debug("Rendering with no caching = " + config.getCacheTemplates());
		if (config.getCacheTemplates() == false) {
			initMustache();
		}

		return mustacheEngine.render(new ModelAndView(attributes, view));

	}

	public static void setSampleAPIService(SampleAPIService s) {

		sampleAPIService = s;
	}
	
	

}
