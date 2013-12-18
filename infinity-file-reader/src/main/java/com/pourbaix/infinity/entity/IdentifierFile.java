package com.pourbaix.infinity.entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class IdentifierFile {
	private final LinkedHashMap<String, IdentifierEntry> entries = new LinkedHashMap<>();

	public LinkedHashMap<String, IdentifierEntry> getEntries() {
		return entries;
	}

	public void addEntry(IdentifierEntry entry) {
		entries.put(entry.getKey(), entry);
	}

	public IdentifierEntry getEntryByKey(String key) {
		return entries.get(key);
	}

	public IdentifierEntry getEntryByValue(String value) {
		IdentifierEntry result = null;
		for (Map.Entry<String, IdentifierEntry> entry : entries.entrySet()) {
			if (entry.getValue().contains(value)) {
				result = entry.getValue();
				break;
			}
		}
		return result;
	}

}
