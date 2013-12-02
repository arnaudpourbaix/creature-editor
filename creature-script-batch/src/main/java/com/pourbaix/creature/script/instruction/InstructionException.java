package com.pourbaix.creature.script.instruction;

public class InstructionException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final String INVALID_INSTRUCTION_ERROR_MESSAGE = "invalid instruction";
	public static final String UNKNOWN_KEYWORD_ERROR_MESSAGE = "unknown keyword";

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
