package com.pourbaix.infinity.service;

public class GameException extends Exception {

	private static final long serialVersionUID = 1L;

	public GameException() {

	}

	public GameException(final String message) {
		super(message);
	}

	public GameException(final Throwable cause) {
		super(cause);
	}

	public GameException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
