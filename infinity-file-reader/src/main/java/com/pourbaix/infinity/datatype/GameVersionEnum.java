package com.pourbaix.infinity.datatype;


public enum GameVersionEnum {
	BG1EE("Baldur's Gate - Enhanced Edition"), BG2EE("Baldur's Gate II - Enhanced Edition"), TUTU("Baldur's Gate - Tutu"), BGT("Baldur's Gate - Trilogy");

	private String name;

	private GameVersionEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
