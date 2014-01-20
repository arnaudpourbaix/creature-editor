package com.pourbaix.creature.editor.spell;

public class SpellImportException extends Exception {

	private static final long serialVersionUID = 1L;

	public SpellImportException() {

	}

	public SpellImportException(final String message) {
		super(message);
	}

	public SpellImportException(final Throwable cause) {
		super(cause);
	}

	public SpellImportException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
