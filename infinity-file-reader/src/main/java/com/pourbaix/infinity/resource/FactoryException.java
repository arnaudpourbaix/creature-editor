package com.pourbaix.infinity.resource;

public class FactoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public FactoryException() {

	}

	public FactoryException(final String message) {
		super(message);
	}

	public FactoryException(final Throwable cause) {
		super(cause);
	}

	public FactoryException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
