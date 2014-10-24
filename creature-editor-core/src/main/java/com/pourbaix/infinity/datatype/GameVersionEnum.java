package com.pourbaix.infinity.datatype;

public enum GameVersionEnum {
	BG1("Baldur's Gate"), TUTU("Baldur's Gate Tutu"), BGT("Baldur's Gate Trilogy"), BG1EE("Baldur's Gate Enhanced Edition"), BG2("Baldur's Gate II"), BG2EE(
			"Baldur's Gate II Enhanced Edition");

	private String name;

	private GameVersionEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
