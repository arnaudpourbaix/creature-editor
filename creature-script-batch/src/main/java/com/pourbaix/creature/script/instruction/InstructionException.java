package com.pourbaix.creature.script.instruction;

public class InstructionException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final String MATCHING_BRACKET_ERROR_MESSAGE = "matching bracket error";
	public static final String BAD_SEPARATOR_ERROR_MESSAGE = "bad separator error";

	public InstructionException() {

	}

	public InstructionException(final String message) {
		super(message);
	}

	public InstructionException(final Throwable cause) {
		super(cause);
	}

	public InstructionException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
