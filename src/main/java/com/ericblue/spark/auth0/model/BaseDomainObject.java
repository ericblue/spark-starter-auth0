package com.ericblue.spark.auth0.model;

import java.io.IOException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class BaseDomainObject {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseDomainObject.class);
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String toJson() {

		ObjectMapper mapper = new ObjectMapper();

		// mapper.registerModule(DateTimeModule.create());

		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String json = null;
		try {
			json = ow.writeValueAsString(this);
		} catch (JsonGenerationException e) {
			logger.error("Exception " + e);
		} catch (JsonMappingException e) {
			logger.error("Exception " + e);
		} catch (IOException e) {
			logger.error("Exception " + e);
		}

		return json;

	}

}
