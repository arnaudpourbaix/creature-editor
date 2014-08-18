package com.pourbaix.creature.editor.domain;

public class SpellOffensiveFlag {

	private byte bit;
	private SpellOffensiveFlagEnum name;
	private String label;

	public SpellOffensiveFlag(int bit, SpellOffensiveFlagEnum name, String label) {
		this.bit = (byte) bit;
		this.name = name;
		this.label = label;
	}

	public SpellOffensiveFlagEnum getName() {
		return name;
	}

	public void setName(SpellOffensiveFlagEnum name) {
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
