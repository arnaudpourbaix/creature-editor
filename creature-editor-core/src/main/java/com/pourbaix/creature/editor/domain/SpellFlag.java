package com.pourbaix.creature.editor.domain;

public class SpellFlag {

	private byte bit;
	private SpellFlagEnum name;
	private String label;

	public SpellFlag(int bit, SpellFlagEnum name, String label) {
		this.bit = (byte) bit;
		this.name = name;
		this.label = label;
	}

	public SpellFlagEnum getName() {
		return name;
	}

	public void setName(SpellFlagEnum name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public byte getBit() {
		return bit;
	}

	public void setBit(byte bit) {
		this.bit = bit;
	}

}
