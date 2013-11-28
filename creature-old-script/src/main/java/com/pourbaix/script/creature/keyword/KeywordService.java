package com.pourbaix.script.creature.keyword;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pourbaix.script.creature.generator.GeneratorException;

/**
 * Service related to keyword operations.
 * 
 */
@Service
public class KeywordService {

	/**
	 * Get a structured list of keywords from a text that contains keywords separated by a semicolon<br />
	 * text format: name=keyword-value 1,...,keyword-value n;[...] <br />
	 * 
	 * a keyword is composed of a name and a list of keyword-values separated by a comma (it must have at least one keyword-value)<br />
	 * see {@link KeywordService#getKeywordValues(String)} for keyword-value composition.<br />
	 * 
	 * @param text
	 * @return list of keywords
	 */
	public List<Keyword> getKeywords(String text) throws GeneratorException {
		List<Keyword> keywords = new ArrayList<Keyword>();
		if (!text.startsWith("[") || !text.endsWith("]")) {
			return keywords;
		}
		for (String s : text.substring(1, text.length() - 1).split(";")) {
			Keyword keyword = new Keyword(s.split("=")[0]);
			keyword.getValues().addAll(getKeywordValues(s.split("=")[1]));
			keywords.add(keyword);
		}
		return keywords;
	}

	/**
	 * Get a list of keyword-values from a text <br />
	 * text format: name(keyword-value 1,...,keyword-value n),[...] <br />
	 * 
	 * a keyword-value is composed of a name and an optional list of keyword-values <br />
	 * (if there is no keyword-value associated, parentheses can be omitted) <br />
	 * 
	 * @param line
	 * @return list of keyword-values
	 */
	public List<KeywordValue> getKeywordValues(String text) throws GeneratorException {
		List<KeywordValue> values = new ArrayList<KeywordValue>();
		KeywordValueReader reader = new KeywordValueReader(text);
		while (reader.next()) {
			values.add(reader.getValue());
		}
		return values;
	}

}
