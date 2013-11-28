package com.pourbaix.script.creature.spell;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum ClassEnum {
	UNKNOWN("UNKNOWN"),
	
	MAGE_ALL("WIZARD"), BARD_ALL("WIZARD"), CLERIC_ALL("PRIEST"), DRUID_ALL("PRIEST"),

	FIGHTER_ALL("FIGHTER"), PALADIN_ALL("FIGHTER"), RANGER_ALL("FIGHTER"),

	FIGHTER("FIGHTER"), PALADIN("FIGHTER"), RANGER("FIGHTER"),

	SORCERER("WIZARD"), MAGE("WIZARD"), BARD("WIZARD"),

	CLERIC("PRIEST"), DRUID("PRIEST"),

	MONK("MONK"), THIEF("THIEF"),

	PRIEST("PRIEST"), WIZARD("WIZARD");

	private String genericClass;

	private ClassEnum(
			final String genericClass) {
		this.genericClass = genericClass;
	}

	public static ClassEnum fromString(final String text) throws GeneratorException {
		if (text != null) {
			for (final ClassEnum v : ClassEnum.values()) {
				if (text.trim().equalsIgnoreCase(v.name())) {
					return v;
				}
			}
		}
		//return ClassEnum.UNKNOWN;
		throw new GeneratorException("Unknown class: " + text);
	}

	public String getGenericClass() {
		return genericClass;
	}

}
