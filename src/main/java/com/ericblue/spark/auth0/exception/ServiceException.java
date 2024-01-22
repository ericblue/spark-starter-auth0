package com.ericblue.spark.auth0.exception;

public class ServiceException extends Exception {



	/**
     * 
     */
    private static final long serialVersionUID = 1615259020134814389L;

    public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
