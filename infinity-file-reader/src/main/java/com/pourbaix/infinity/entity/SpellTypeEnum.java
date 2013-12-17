package com.pourbaix.infinity.entity;

public enum SpellTypeEnum {
	Special((short) 0, "Special"), Wizard((short) 1, "Wizard"), Cleric((short) 2, "Cleric"), Psionic((short) 3, "Psionic"), Innate((short) 4, "Innate"), Bardsong(
			(short) 5, "Bardsong");

	private short value;
	private String label;

	private SpellTypeEnum(short value, String label) {
		this.value = value;
		this.label = label;
	}

	public short getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static SpellTypeEnum valueOf(short value) throws UnknownValueException {
		for (SpellTypeEnum type : SpellTypeEnum.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		// return null;
		throw new UnknownValueException("no SchoolEnum found for " + value);
	}

}
