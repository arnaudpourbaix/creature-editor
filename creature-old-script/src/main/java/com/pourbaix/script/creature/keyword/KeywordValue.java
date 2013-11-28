package com.pourbaix.script.creature.keyword;
import java.util.ArrayList;
import java.util.List;


public class KeywordValue {
	private String name;
	private List<KeywordValue> values = new ArrayList<KeywordValue>();
	
	public KeywordValue(String name) {
		this.name = name;
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
	
	public void addValue(KeywordValue value) {
		this.values.add(value);
	}
	
}
