package com.pourbaix.infinity.resource;

public class StringResourceException extends Exception {

	private static final long serialVersionUID = 1L;

	public StringResourceException() {

	}

	public StringResourceException(final String message) {
		super(message);
	}

	public StringResourceException(final Throwable cause) {
		super(cause);
	}

	public StringResourceException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
