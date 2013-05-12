package com.vitaminme.exceptions;

public class RecipeParseException extends VitaminMeException {
	
	private static final long serialVersionUID = 3L;
	
	public RecipeParseException() {
		super();
	}
	
	public RecipeParseException(String message) {
		super(message);
	}
	
	public RecipeParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
