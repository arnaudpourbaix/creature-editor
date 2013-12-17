package com.pourbaix.infinity.entity;

public enum SchoolEnum {
	None((byte) 0, "None"), Abjurer((byte) 1, "Abjurer"), Conjurer((byte) 2, "Conjurer"), Diviner((byte) 3, "Diviner"), Enchanter((byte) 4, "Enchanter"), Illusionist(
			(byte) 5, "Illusionist"), Invoker((byte) 6, "Invoker"), Necromancer((byte) 7, "Necromancer"), Transmuter((byte) 8, "Transmuter"), Generalist(
			(byte) 9, "Generalist"), Unknown((byte) 10, "Unknown");

	private byte value;
	private String label;

	private SchoolEnum(byte value, String label) {
		this.value = value;
		this.label = label;
	}

	public byte getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static SchoolEnum valueOf(byte value) throws UnknownValueException {
		for (SchoolEnum school : SchoolEnum.values()) {
			if (school.getValue() == value) {
				return school;
			}
		}
		// return null;
		throw new UnknownValueException("no SchoolEnum found for " + value);
	}

}
