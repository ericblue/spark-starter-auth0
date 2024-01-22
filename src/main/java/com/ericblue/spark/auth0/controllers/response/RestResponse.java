package com.ericblue.spark.auth0.controllers.response;

public class RestResponse {

	private RestResponseType responseType;
	private Object response;

	public RestResponse() {
		// TODO Auto-generated constructor stub
	}

	public RestResponseType getResponseType() {
		return responseType;
	}

	public void setResponseType(RestResponseType responseType) {
		this.responseType = responseType;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

}
