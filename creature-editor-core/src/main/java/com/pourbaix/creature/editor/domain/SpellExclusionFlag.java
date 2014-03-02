package com.pourbaix.creature.editor.domain;

public class SpellExclusionFlag {

	private byte bit;
	private SpellExclusionFlagEnum name;
	private String label;

	public SpellExclusionFlag(int bit, SpellExclusionFlagEnum name, String label) {
		this.bit = (byte) bit;
		this.name = name;
		this.label = label;
	}

	public SpellExclusionFlagEnum getName() {
		return name;
	}

	public void setName(SpellExclusionFlagEnum name) {
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
