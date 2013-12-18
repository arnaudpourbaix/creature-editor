package com.pourbaix.infinity.cache;

public class CacheException extends Exception {

	private static final long serialVersionUID = 1L;

	public CacheException() {

	}

	public CacheException(final String message) {
		super(message);
	}

	public CacheException(final Throwable cause) {
		super(cause);
	}

	public CacheException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
