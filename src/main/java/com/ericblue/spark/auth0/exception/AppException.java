package com.ericblue.spark.auth0.exception;

public class AppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2137661847892508615L;

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

}
