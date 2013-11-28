package com.pourbaix.script.creature.keyword;
import java.util.ArrayList;
import java.util.List;

import com.pourbaix.script.creature.generator.GeneratorException;


public class Keyword implements Comparable<Keyword> {
	private String name;
	private KeywordEnum type;
	private List<KeywordValue> values = new ArrayList<KeywordValue>();
	
	public Keyword(String name) throws GeneratorException {
		this.name = name;
		this.type = KeywordEnum.fromString(name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<KeywordValue> getValues() {
		return values;
	}
	
	public String getSingleValue() {
		return this.values.isEmpty() ? "" : this.values.get(0).getName();
	}
	
	public KeywordEnum getType() {
		return type;
	}

	public void setType(KeywordEnum type) {
		this.type = type;
	}

	public int compareTo(Keyword o) {
		return 0;
	}
	
}
