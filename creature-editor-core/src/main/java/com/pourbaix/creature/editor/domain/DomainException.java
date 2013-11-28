package com.pourbaix.creature.editor.domain;

public class DomainException extends Exception {

	private static final long serialVersionUID = 1L;

	public DomainException() {

	}

	public DomainException(final String message) {
		super(message);
	}

	public DomainException(final Throwable cause) {
		super(cause);
	}

	public DomainException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
