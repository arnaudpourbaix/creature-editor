package com.pourbaix.infinity.entity;

public enum SpellSecondaryTypeEnum {
	None((byte) 0, "None"), SpellProtections((byte) 1, "Spell protections"), SpecificProtections((byte) 2, "Specific protections"), IllusionaryProtections(
			(byte) 3, "Illusionary protections"), MagicAttack((byte) 4, "Magic attack"), DivinationAttack((byte) 5, "Divination attack"), Conjuration((byte) 6,
			"Conjuration"), CombatProtections((byte) 7, "Combat protections"), Contingency((byte) 8, "Contingency"), Battleground((byte) 9, "Battleground"), OffensiveDamage(
			(byte) 10, "Offensive damage"), Disabling((byte) 11, "Disabling"), Combination((byte) 12, "Combination"), NonCombat((byte) 13, "Non-combat"), Unknown(
			(byte) 14, "Unknown");

	private byte value;
	private String label;

	private SpellSecondaryTypeEnum(byte value, String label) {
		this.value = value;
		this.label = label;
	}

	public byte getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static SpellSecondaryTypeEnum valueOf(byte value) throws UnknownValueException {
		for (SpellSecondaryTypeEnum type : SpellSecondaryTypeEnum.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		// return null;
		throw new UnknownValueException("no SpellSecondaryTypeEnum found for " + value);
	}

}
