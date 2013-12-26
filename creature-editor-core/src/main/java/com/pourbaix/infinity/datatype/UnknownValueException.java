package com.pourbaix.infinity.datatype;

public class UnknownValueException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnknownValueException() {

	}

	public UnknownValueException(final String message) {
		super(message);
	}

	public UnknownValueException(final Throwable cause) {
		super(cause);
	}

	public UnknownValueException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
