package com.pourbaix.infinity.entity;

public enum AbilityLocationEnum {
	Unknown0((short) 0, "Unknown"), Unknown1((short) 1, "Unknown"), SpellSlot((short) 2, "Spell slots"), Unknown3((short) 3, "Unknown"), InnateSlot((short) 4,
			"Innate slots");

	private short value;
	private String label;

	private AbilityLocationEnum(short value, String label) {
		this.value = value;
		this.label = label;
	}

	public short getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static AbilityLocationEnum valueOf(short value) throws UnknownValueException {
		for (AbilityLocationEnum location : AbilityLocationEnum.values()) {
			if (location.getValue() == value) {
				return location;
			}
		}
		throw new UnknownValueException("no AbilityLocationEnum found for " + value);
	}

}
