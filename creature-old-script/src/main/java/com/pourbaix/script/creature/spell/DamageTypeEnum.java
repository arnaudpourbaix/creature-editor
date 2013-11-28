package com.pourbaix.script.creature.spell;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum DamageTypeEnum {
	FIRE, COLD, ELECTRICITY, ACID, MAGIC, POISON, CRUSHING, SLASHING, PIERCING;

	public static DamageTypeEnum fromString(final String text) throws GeneratorException {
		if (text != null) {
			for (final DamageTypeEnum v : DamageTypeEnum.values()) {
				if (text.trim().equalsIgnoreCase(v.name())) {
					return v;
				}
			}
		}
		throw new GeneratorException("Unknown damage type: " + text);
	}

}
