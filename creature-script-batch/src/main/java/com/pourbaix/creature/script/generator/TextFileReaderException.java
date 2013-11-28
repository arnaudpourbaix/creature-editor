package com.pourbaix.creature.script.generator;

import java.io.IOException;

public class TextFileReaderException extends IOException {

	private static final long serialVersionUID = 1L;

	private final Integer lineNumber;

	public TextFileReaderException(final String message, Integer lineNumber) {
		super(message);
		this.lineNumber = lineNumber;
	}

	public TextFileReaderException(final Throwable cause, Integer lineNumber) {
		super(cause);
		this.lineNumber = lineNumber;
	}

	public TextFileReaderException(final String message, final Throwable cause, Integer lineNumber) {
		super(message, cause);
		this.lineNumber = lineNumber;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

}
