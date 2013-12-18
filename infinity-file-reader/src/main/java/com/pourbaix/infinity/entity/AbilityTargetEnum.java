package com.pourbaix.infinity.entity;

public enum AbilityTargetEnum {
	Invalid((byte) 0, "Invalid"), Creature((byte) 1, "Creature"), Inventory((byte) 2, "Inventory"), Portrait((byte) 3, "Character portrait"), Area((byte) 4,
			"Area"), Self((byte) 5, "Self"), Unknown6((byte) 6, "Unknown"), None((byte) 7, "None (self, ignores game pause)");

	private byte value;
	private String label;

	private AbilityTargetEnum(byte value, String label) {
		this.value = value;
		this.label = label;
	}

	public byte getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static AbilityTargetEnum valueOf(byte value) throws UnknownValueException {
		for (AbilityTargetEnum target : AbilityTargetEnum.values()) {
			if (target.getValue() == value) {
				return target;
			}
		}
		throw new UnknownValueException("no AbilityTargetEnum found for " + value);
	}

}
