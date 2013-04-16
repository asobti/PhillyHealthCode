package com.vitaminme.exceptions;

public class APICallException extends VitaminMeException {	
	private static final long serialVersionUID = 2L;
	
	public APICallException() {
		super();
	}
	
	public APICallException(String message) {
		super(message);
	}
	
	public APICallException(String message, Throwable cause) {
		super(message, cause);
	}
}
