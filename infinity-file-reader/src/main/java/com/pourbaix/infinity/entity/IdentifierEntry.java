package com.pourbaix.infinity.entity;

import java.util.ArrayList;
import java.util.List;

public class IdentifierEntry {
	private final String key;
	private final List<String> values;

	public IdentifierEntry(String key) {
		this.key = key;
		this.values = new ArrayList<>();
	}

	public void addValue(String value) {
		values.add(value);
	}

	public String getFirstValue() {
		return values.isEmpty() ? null : values.get(0);
	}

	public String getKey() {
		return key;
	}

	public List<String> getValues() {
		return values;
	}

}
