package com.pourbaix.infinity.datatype;

public enum DimensionalArrayEnum {
	Kit("KITLIST.2DA");

	private String resource;

	private DimensionalArrayEnum(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return this.resource;
	}
}
