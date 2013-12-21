package com.pourbaix.infinity.datatype;


public enum AbilityTypeEnum {
	Unknown((short) 0, "Unknown"), Melee((short) 1, "Melee"), Ranged((short) 2, "Ranged"), Magical((short) 3, "Magical"), Launcher((short) 4, "Launcher");

	private short value;
	private String label;

	private AbilityTypeEnum(short value, String label) {
		this.value = value;
		this.label = label;
	}

	public short getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static AbilityTypeEnum valueOf(short value) throws UnknownValueException {
		for (AbilityTypeEnum type : AbilityTypeEnum.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new UnknownValueException("no AbilityTypeEnum found for " + value);
	}

}
