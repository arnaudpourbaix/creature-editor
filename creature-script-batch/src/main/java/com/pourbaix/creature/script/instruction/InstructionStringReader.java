package com.pourbaix.creature.script.instruction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionStringReader {

	private static final String KEYWORD_PATTERN = "^(!?)([\\w\\s%'_-]+)";
	private static final String KEYWORD_SEPARATOR_PATTERN = "^\\s*,\\s*";
	private static final char OPENING_PARAM_CHAR = '(';
	private static final char CLOSING_PARAM_CHAR = ')';

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
		readKeywordParameters(keyword);
		positionToNextKeyword();
		return keyword;
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
		if (pos >= currentText.length() || currentText.charAt(pos) != CLOSING_PARAM_CHAR) {
			throw new InstructionException(InstructionException.MATCHING_BRACKET_ERROR_MESSAGE + ": " + currentText);
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
			throw new InstructionException(InstructionException.BAD_SEPARATOR_ERROR_MESSAGE + ": " + currentText);
		}
		position += matcher.end();
	}

}
