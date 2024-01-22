package com.ericblue.spark.auth0.exception;

public class LoginException extends AppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1800538826495512091L;

	/**
	 * 
	 */

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

}
