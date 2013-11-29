package com.pourbaix.creature.script.instruction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * read text and build keywordValue object.
 * 
 */
public class InstructionReader {

	private static final String KEYWORD_PATTERN = "^(!?)([\\w\\s%'_-]+)";
	private static final String KEYWORD_SEPARATOR_PATTERN = "^\\s*,\\s*";
	private static final char OPENING_PARAM_CHAR = '(';
	private static final char CLOSING_PARAM_CHAR = ')';

	private Pattern keywordPattern = Pattern.compile(KEYWORD_PATTERN);
	private Pattern keywordSeparatorPattern = Pattern.compile(KEYWORD_SEPARATOR_PATTERN);
	private String text;
	private int position;
	private final List<RawKeywordParam> keywords = new ArrayList<>();
	private final Iterator<RawKeywordParam> keywordIterator;

	public InstructionReader(String text) throws InstructionException {
		this.text = text.trim();
		this.position = 0;
		RawKeywordParam keyword;
		while ((keyword = readKeyword()) != null) {
			keywords.add(keyword);
			nextKeyword();
		}
		keywordIterator = this.keywords.iterator();
	}
	
	public boolean hasNext() {
		return this.keywordIterator.hasNext();
	}
	
	public RawKeywordParam next() {
		return this.keywordIterator.next();
	}
	
	private RawKeywordParam readKeyword() throws InstructionException {
		Matcher matcher = keywordPattern.matcher(this.text);
		if (!matcher.find(this.position)) {
			return null;
		}
		RawKeywordParam keyword = new RawKeywordParam(matcher.group(2).trim(), matcher.group(1).isEmpty());
		this.position = matcher.end();
		readKeywordParameters(keyword);
		//System.out.println(keyword.toString());
		return keyword;
	}

	private void readKeywordParameters(final RawKeywordParam keyword) throws InstructionException {
		int bracketCount = 0;
		int pos = this.position;
		if (endOfString() || this.text.charAt(pos) != OPENING_PARAM_CHAR) {
			return;
		}
		while (!endOfString(pos)) {
			if (this.text.charAt(pos) == OPENING_PARAM_CHAR) {
				bracketCount++;
			} else if (this.text.charAt(pos) == CLOSING_PARAM_CHAR) {
				bracketCount--;
				if (bracketCount == 0) {
					break;
				}
			}
			pos++;
		}
		if (endOfString(pos) || this.text.charAt(pos) != CLOSING_PARAM_CHAR) {
			throw new InstructionException(InstructionException.MATCHING_BRACKET_ERROR_MESSAGE + ": " + this.text.substring(this.position));
		}
		String param = this.text.substring(this.position + 1, pos).trim();
		if (!param.isEmpty()) {
			keyword.setParam(param);
		}
		this.position = pos + 1;
	}
	
	private void nextKeyword() throws InstructionException {
		if (endOfString()) {
			return;
		}
		this.text = this.text.substring(this.position);
		this.position = 0;
		Matcher matcher = keywordSeparatorPattern.matcher(this.text);
		if (!matcher.find()) {
			throw new InstructionException(InstructionException.BAD_SEPARATOR_ERROR_MESSAGE + ": " + this.text);
		}
		this.text = this.text.substring(matcher.end());
	}

	private boolean endOfString() {
		return endOfString(this.position);
	}

	private boolean endOfString(int pos) {
		return pos >= this.text.length();
	}
	
}
