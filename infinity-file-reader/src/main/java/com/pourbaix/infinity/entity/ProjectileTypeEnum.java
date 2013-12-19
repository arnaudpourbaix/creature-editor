package com.pourbaix.infinity.entity;

public enum ProjectileTypeEnum {

	Unknown(0L, "Unknown"), NoBAM(1L, "No BAM"), SingleTarget(2L, "Single target"), AreaOfEffect(3L, "Area of effect");

	private long value;
	private String label;

	private ProjectileTypeEnum(long value, String label) {
		this.value = value;
		this.label = label;
	}

	public long getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static ProjectileTypeEnum valueOf(long value) throws UnknownValueException {
		for (ProjectileTypeEnum type : ProjectileTypeEnum.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new UnknownValueException("no ProjectileTypeEnum found for " + value);
	}

}
