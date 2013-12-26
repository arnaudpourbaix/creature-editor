package com.pourbaix.infinity.datatype;


public enum TimingEnum {
	Duration((byte) 0, "Duration"), Permanent((byte) 1, "Permanent"), WhileEquipped((byte) 2, "While equipped"), DelayedDuration((byte) 3, "Delayed Duration"), DelayedPermanent(
			(byte) 4, "Delayed / Permanent"), DelayedWhileEquipped((byte) 5, "Delayed / While equipped"), LimitedAfterDuration((byte) 6,
			"Limited after duration"), PermanentAfterDuration((byte) 7, "Permanent after duration"), EquipedAfterDuration((byte) 8, "Equiped after duration"), InstantPermanent(
			(byte) 9, "Instant / Permanent"), InstantLimitedTick((byte) 10, "Instant / Limited (ticks)");

	private byte value;
	private String label;

	private TimingEnum(byte value, String label) {
		this.value = value;
		this.label = label;
	}

	public byte getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static TimingEnum valueOf(byte value) throws UnknownValueException {
		for (TimingEnum timing : TimingEnum.values()) {
			if (timing.getValue() == value) {
				return timing;
			}
		}
		throw new UnknownValueException("no TimingEnum found for " + value);
	}

}
