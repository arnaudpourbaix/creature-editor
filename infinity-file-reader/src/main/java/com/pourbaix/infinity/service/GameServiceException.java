package com.pourbaix.infinity.service;

public class GameServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public GameServiceException() {

	}

	public GameServiceException(final String message) {
		super(message);
	}

	public GameServiceException(final Throwable cause) {
		super(cause);
	}

	public GameServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
