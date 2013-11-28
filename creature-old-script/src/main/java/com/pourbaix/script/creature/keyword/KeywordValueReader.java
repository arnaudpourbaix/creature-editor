package com.pourbaix.script.creature.keyword;

import com.pourbaix.script.creature.generator.GeneratorException;

/**
 * read text and build keywordValue object.
 * 
 */
public class KeywordValueReader {

	private String text;
	private int position;
	private String currentKeywordValueName;
	private String currentKeywordValueParams;

	public KeywordValueReader(String text) {
		this.text = text;
		this.position = 0;
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

	public String getCurrentKeywordValueName() {
		return currentKeywordValueName;
	}

	public void setCurrentKeywordValueName(String currentKeywordValueName) {
		this.currentKeywordValueName = currentKeywordValueName;
	}

	public String getCurrentKeywordValueParams() {
		return currentKeywordValueParams;
	}

	public void setCurrentKeywordValueParams(String currentKeywordValueParams) {
		this.currentKeywordValueParams = currentKeywordValueParams;
	}

	/**
	 * get a keywordValue object from current cursor.
	 * 
	 * @return keywordValue object
	 * @throws GeneratorException
	 */
	public KeywordValue getValue() throws GeneratorException {
		KeywordValue keywordValue = new KeywordValue(currentKeywordValueName);
		if (!currentKeywordValueParams.isEmpty()) {
			KeywordValueReader reader = new KeywordValueReader(currentKeywordValueParams);
			while (reader.next()) {
				keywordValue.addValue(reader.getValue());
			}
		}
		return keywordValue;
	}

	/**
	 * find next keywordValue in text.
	 * 
	 * @return true is a keywordValue is found
	 * @throws GeneratorException
	 */
	public boolean next() throws GeneratorException {
		if (position >= text.length()) {
			return false;
		}
		// find name
		int i = position;
		boolean found = false;
		while (i < text.length() && !found) {
			if (text.charAt(i) == ',' || text.charAt(i) == '(') {
				found = true;
			} else if (text.substring(i, i + 1).matches("[\\w\\s%_'!-]")) {
				i++;
			} else {
				throw new GeneratorException("invalid keyword name: " + text.substring(position));
			}
		}
		currentKeywordValueName = text.substring(position, i);
		currentKeywordValueParams = "";
		position = i + 1;
		if (found && text.charAt(i) == '(') {
			retrieveParameters();
		}
		return true;
	}

	/**
	 * retrieve parameters' text from a keywordValue.
	 * 
	 * @throws GeneratorException
	 */
	private void retrieveParameters() throws GeneratorException {
		int parentheses = 1;
		int i = position;
		while (i < text.length() && parentheses > 0) {
			if (text.charAt(i) == '(') {
				parentheses++;
			} else if (text.charAt(i) == ')') {
				parentheses--;
			}
			i++;
		}
		if (parentheses != 0) {
			throw new GeneratorException("parantheses don't match for " + currentKeywordValueName + ": " + text.substring(position));
		}
		currentKeywordValueParams = text.substring(position, i - 1);
		position = i + 1;
	}

}
