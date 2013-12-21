package com.pourbaix.infinity.datatype;


public enum TargetTypeEnum {
	None((byte) 0, "None"), Self((byte) 1, "Self"), PresetTarget((byte) 2, "Preset target"), Party((byte) 3, "Party"), Everyone((byte) 4, "Everyone"), EveryoneExceptParty(
			(byte) 5, "Everyone except party"), CasterGroup((byte) 6, "Caster group"), TargetGroup((byte) 7, "Target group"), EveryoneExceptSelf((byte) 8,
			"Everyone except self"), OriginalCaster((byte) 9, "Original caster");

	private byte value;
	private String label;

	private TargetTypeEnum(byte value, String label) {
		this.value = value;
		this.label = label;
	}

	public byte getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static TargetTypeEnum valueOf(byte value) throws UnknownValueException {
		for (TargetTypeEnum type : TargetTypeEnum.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new UnknownValueException("no TargetTypeEnum found for " + value);
	}

}
