package com.pourbaix.infinity.entity;

import java.util.HashSet;
import java.util.Set;

public class IdentifierFile {
	private final Set<IdentifierEntry> entries = new HashSet<>();

	public Set<IdentifierEntry> getEntries() {
		return entries;
	}

}
