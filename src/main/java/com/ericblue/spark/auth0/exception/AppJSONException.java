package com.ericblue.spark.auth0.exception;

public class AppJSONException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2137661847892508615L;

	public AppJSONException(String message) {
		super(message);
	}

	public AppJSONException(Throwable cause) {
		super(cause);
	}

	public AppJSONException(String message, Throwable cause) {
		super(message, cause);
	}

}
