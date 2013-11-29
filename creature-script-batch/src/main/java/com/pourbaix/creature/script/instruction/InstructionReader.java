package com.pourbaix.creature.script.instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * read text and build keywordValue object.
 * 
 */
public class InstructionReader {

	private static final String NEGATIVE_RESULT = "!";
	private static final String KEYWORD_PATTERN = "^!?[\\w\\s%'_-]+";
	private static final String KEYWORD_SEPARATOR_PATTERN = "^\\s*,\\s*";
	private static final char OPENING_PARAM_CHAR = '(';
	private static final char CLOSING_PARAM_CHAR = ')';

	private Pattern keywordPattern = Pattern.compile(KEYWORD_PATTERN);
	private Pattern keywordSeparatorPattern = Pattern.compile(KEYWORD_SEPARATOR_PATTERN);
	private String text;
	private int position;
	private List<RawKeywordParam> keywords = new ArrayList<>();

	public InstructionReader(String text) throws InstructionException {
		this.text = text.trim();
		this.position = 0;
		RawKeywordParam keyword;
		while ((keyword = readKeyword()) != null) {
			keywords.add(keyword);
			skipSeparator();
		}
	}

	private boolean endOfString() {
		return this.position >= this.text.length();
	}

	private RawKeywordParam readKeyword() throws InstructionException {
		Matcher matcher = keywordPattern.matcher(this.text);
		if (!matcher.find(this.position)) {
			return null;
		}
		RawKeywordParam keyword = new RawKeywordParam(matcher.group().trim());
		// this.result = !this.keyword.startsWith(NEGATIVE_RESULT);
		// if (this.keyword.startsWith(NEGATIVE_RESULT)) {
		// this.keyword = this.keyword.substring(1).trim();
		// }
		this.position = matcher.end();
		retrieveParameters();
		return keyword;
	}

	private void skipSeparator() throws InstructionException {
		if (endOfString()) {
			return;
		}
		Matcher matcher = keywordSeparatorPattern.matcher(this.text);
		if (!matcher.find(this.position)) {
			throw new InstructionException(InstructionException.BAD_SEPARATOR_ERROR_MESSAGE + ": " + this.text.substring(this.position));
		}
		this.position = matcher.end();
	}

	private void retrieveParameters() throws InstructionException {
		// this.params = null;
		if (this.position + 3 >= this.text.length()) {
			return;
		}
		if (this.text.charAt(this.position) != OPENING_PARAM_CHAR) {
			return;
		}
		int bracketCount = 0;
		int pos = this.position + 1;
		while (pos < this.text.length()) {
			if (this.text.charAt(pos) == OPENING_PARAM_CHAR) {
				bracketCount++;
			} else if (this.text.charAt(pos) == CLOSING_PARAM_CHAR) {
				if (bracketCount == 0) {
					break;
				}
				bracketCount--;
			}
			pos++;
		}
		if (pos >= this.text.length() || this.text.charAt(pos) != CLOSING_PARAM_CHAR) {
			throw new InstructionException(InstructionException.MATCHING_BRACKET_ERROR_MESSAGE + ": " + this.text.substring(this.position));
		}
		// this.params = this.text.substring(this.position + 1, pos).trim();
	}

}
