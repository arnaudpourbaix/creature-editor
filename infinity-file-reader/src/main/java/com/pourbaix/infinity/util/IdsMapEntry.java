package com.pourbaix.infinity.util;

public final class IdsMapEntry implements Comparable<IdsMapEntry> {
	private final String string;
	private final String parameters;
	private final long id;

	public IdsMapEntry(long id, String string, String parameters) {
		this.id = id;
		this.string = string;
		this.parameters = parameters;
	}

	@Override
	public int compareTo(IdsMapEntry o) {
		return toString().compareToIgnoreCase(o.toString());
	}

	@Override
	public String toString() {
		if (parameters == null)
			return string + " - " + id;
		return string + parameters + ") - " + id;
	}

	public long getID() {
		return id;
	}

	public String getParameters() {
		return parameters;
	}

	public String getString() {
		return string;
	}
}
