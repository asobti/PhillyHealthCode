package com.vitaminme.exceptions;

public class APILimitExceededException extends APICallException {

	private static final long serialVersionUID = 3L;
	
	public APILimitExceededException() {
		super();
	}
	
	public APILimitExceededException(String message) {
		super(message);
	}
	
	public APILimitExceededException(String message, Throwable cause) {
		super(message, cause);
	}

}
