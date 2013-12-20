package com.pourbaix.infinity.entity;

public enum ResistanceEnum {
	NonMagical((byte) 0, "Non magical"), DispelResistance((byte) 1, "Can be dispelled/Affected by resistance"), NoDispelNoResistance((byte) 2,
			"Cannot be dispelled/Ignores resistance"), DispelNoResistance((byte) 3, "Can be dispelled/Ignores resistance");

	private byte value;
	private String label;

	private ResistanceEnum(byte value, String label) {
		this.value = value;
		this.label = label;
	}

	public byte getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static ResistanceEnum valueOf(byte value) throws UnknownValueException {
		for (ResistanceEnum resistance : ResistanceEnum.values()) {
			if (resistance.getValue() == value) {
				return resistance;
			}
		}
		throw new UnknownValueException("no ResistanceEnum found for " + value);
	}

}
