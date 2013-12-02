package com.pourbaix.creature.script.instruction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstructionStringReader {

	private static final Logger logger = LoggerFactory.getLogger(InstructionStringReader.class);

	private static final String KEYWORD_PATTERN = "^(!?)([\\w\\s%'_-]+)";
	private static final String KEYWORD_SEPARATOR_PATTERN = "^\\s*,\\s*";
	private static final char OPENING_PARAM_CHAR = '(';
	private static final char CLOSING_PARAM_CHAR = ')';
	private static final char SINGLE_PARAM_CHAR = '=';

	private final String text;
	private int position;

	public InstructionStringReader(String text) {
		this.text = text.trim();
		this.position = 0;
	}

	public RawKeywordParam getKeyword() throws InstructionException {
		String currentText = text.substring(position);
		Pattern keywordPattern = Pattern.compile(KEYWORD_PATTERN);
		Matcher matcher = keywordPattern.matcher(currentText);
		if (!matcher.find()) {
			return null;
		}
		RawKeywordParam keyword = new RawKeywordParam(matcher.group(2).trim(), matcher.group(1).isEmpty());
		position += matcher.end();
		readKeywordSingleParameter(keyword);
		readKeywordParameters(keyword);
		positionToNextKeyword();
		logger.debug(keyword.toString());
		return keyword;
	}

	private void readKeywordSingleParameter(final RawKeywordParam keyword) throws InstructionException {
		String currentText = text.substring(position);
		if (currentText.isEmpty() || currentText.charAt(0) != SINGLE_PARAM_CHAR) {
			return;
		}
		int bracketCount = 0;
		int pos = 0;
		Pattern keywordSeparatorPattern = Pattern.compile(KEYWORD_SEPARATOR_PATTERN);
		while (pos < currentText.length()) {
			if (currentText.charAt(pos) == OPENING_PARAM_CHAR) {
				bracketCount++;
			} else if (currentText.charAt(pos) == CLOSING_PARAM_CHAR) {
				bracketCount--;
			} else if (bracketCount == 0) {
				Matcher matcher = keywordSeparatorPattern.matcher(currentText.substring(pos));
				if (matcher.find()) {
					break;
				}
			}
			pos++;
		}
		if (bracketCount > 0) {
			throw new InstructionException(InstructionException.INVALID_INSTRUCTION_ERROR_MESSAGE + ": " + currentText);
		}
		String param = currentText.substring(1, pos).trim();
		if (!param.isEmpty()) {
			keyword.setParam(param);
		}
		position += pos;
	}

	private void readKeywordParameters(final RawKeywordParam keyword) throws InstructionException {
		String currentText = text.substring(position);
		if (currentText.isEmpty() || currentText.charAt(0) != OPENING_PARAM_CHAR) {
			return;
		}
		int bracketCount = 0;
		int pos = 0;
		while (pos < currentText.length()) {
			if (currentText.charAt(pos) == OPENING_PARAM_CHAR) {
				bracketCount++;
			} else if (currentText.charAt(pos) == CLOSING_PARAM_CHAR) {
				bracketCount--;
				if (bracketCount == 0) {
					break;
				}
			}
			pos++;
		}
		if (bracketCount != 0) {
			throw new InstructionException(InstructionException.INVALID_INSTRUCTION_ERROR_MESSAGE + ": " + currentText);
		}
		String param = currentText.substring(1, pos).trim();
		if (!param.isEmpty()) {
			keyword.setParam(param);
		}
		position += pos + 1;
	}

	private void positionToNextKeyword() throws InstructionException {
		String currentText = text.substring(position);
		if (currentText.isEmpty()) {
			return;
		}
		Pattern keywordSeparatorPattern = Pattern.compile(KEYWORD_SEPARATOR_PATTERN);
		Matcher matcher = keywordSeparatorPattern.matcher(currentText);
		if (!matcher.find()) {
			throw new InstructionException(InstructionException.INVALID_INSTRUCTION_ERROR_MESSAGE + ": " + currentText);
		}
		position += matcher.end();
	}

}
