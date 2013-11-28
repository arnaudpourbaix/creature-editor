package com.pourbaix.script.creature.generator;

public class GeneratorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public GeneratorException() {

	}

	public GeneratorException(final String message) {
		super(message);
	}

	public GeneratorException(final Throwable cause) {
		super(cause);
	}

	public GeneratorException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
