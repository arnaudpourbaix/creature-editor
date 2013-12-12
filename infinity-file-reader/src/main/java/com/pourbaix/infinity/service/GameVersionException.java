package com.pourbaix.infinity.service;

public class GameVersionException extends Exception {

	private static final long serialVersionUID = 1L;

	public GameVersionException() {

	}

	public GameVersionException(final String message) {
		super(message);
	}

	public GameVersionException(final Throwable cause) {
		super(cause);
	}

	public GameVersionException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
