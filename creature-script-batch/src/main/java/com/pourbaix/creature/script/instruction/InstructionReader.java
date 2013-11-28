package com.pourbaix.creature.script.instruction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pourbaix.creature.script.generator.GeneratorException;

/**
 * read text and build keywordValue object.
 * 
 */
public class InstructionReader {

	private String text;
	private int position;
	private boolean result;
	private String keyword;
	private String params;
	private static final String NEGATIVE_RESULT = "!";
	private static final String KEYWORD_REGEX = "^!?[\\w\\s%'_-]+";

	public InstructionReader(String text) {
		this.text = text;
		this.position = 0;
	}

	/**
	 * find next keyword in text.
	 * 
	 * @return true is a keyword is found
	 * @throws GeneratorException
	 */
	public boolean next() throws GeneratorException {
		Pattern keywordPattern = Pattern.compile(KEYWORD_REGEX);
		Matcher matcher = keywordPattern.matcher(this.text);
		if (!matcher.find(this.position)) {
			return false;
		}
		this.keyword = matcher.group();
		this.result = !this.keyword.startsWith(NEGATIVE_RESULT);
		if (this.keyword.startsWith(NEGATIVE_RESULT)) {
			this.keyword = this.keyword.substring(1);
		}
		this.position = matcher.end();
		return true;
		// if (position >= text.length()) {
		// return false;
		// }
		// int i = position;
		// boolean found = false;
		// while (i < text.length() && !found) {
		// if (text.charAt(i) == ',' || text.charAt(i) == '(') {
		// found = true;
		// } else if (text.substring(i, i + 1).matches("[\\w\\s%_'!-]")) {
		// i++;
		// } else {
		// throw new GeneratorException("invalid keyword name: " + text.substring(position));
		// }
		// }
		// currentKeywordValueName = text.substring(position, i);
		// currentKeywordValueParams = "";
		// position = i + 1;
		// if (found && text.charAt(i) == '(') {
		// retrieveParameters();
		// }
		// return found;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
