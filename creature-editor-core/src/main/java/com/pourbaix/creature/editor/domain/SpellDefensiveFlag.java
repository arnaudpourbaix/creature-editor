package com.pourbaix.creature.editor.domain;

public class SpellDefensiveFlag {

	private byte bit;
	private SpellDefensiveFlagEnum name;
	private String label;

	public SpellDefensiveFlag(int bit, SpellDefensiveFlagEnum name, String label) {
		this.bit = (byte) bit;
		this.name = name;
		this.label = label;
	}

	public SpellDefensiveFlagEnum getName() {
		return name;
	}

	public void setName(SpellDefensiveFlagEnum name) {
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
