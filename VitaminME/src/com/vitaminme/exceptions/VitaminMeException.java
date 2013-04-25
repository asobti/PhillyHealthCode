package com.vitaminme.exceptions;

/*
 * Abstract exception class. All VitaminME exceptions extend this class
 */
public abstract class VitaminMeException extends Exception {	
	private static final long serialVersionUID = 1L;
	
	public VitaminMeException() {
		super();
	}
	
	public VitaminMeException(String message) {
		super(message);
	}
	
	public VitaminMeException(String message, Throwable cause) {
		super(message, cause);
	}
}
