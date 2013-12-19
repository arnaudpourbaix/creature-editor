package com.pourbaix.infinity.entity;

import java.util.ArrayList;
import java.util.List;

public class IdentifierEntry {
	private final Long key;
	private final List<String> values;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifierEntry other = (IdentifierEntry) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	public IdentifierEntry(Long key) {
		this.key = key;
		this.values = new ArrayList<>();
	}

	public void addValue(String value) {
		values.add(value);
	}

	public String getFirstValue() {
		return values.isEmpty() ? null : values.get(0);
	}

	public boolean contains(String value) {
		return values.contains(value);
	}

	public Long getKey() {
		return key;
	}

	public List<String> getValues() {
		return values;
	}

}
