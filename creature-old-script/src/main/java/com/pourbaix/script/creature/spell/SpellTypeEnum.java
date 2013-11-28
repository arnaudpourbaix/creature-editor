package com.pourbaix.script.creature.spell;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum SpellTypeEnum {
	BLIND, CHARM, CONFUSION, DEATH, DISEASE, ENTANGLE, FAILURE, FEAR, HOLD, PETRIFICATION, SAVE, SLEEP, STUN, SILENCE, TELEPORT;

	public static SpellTypeEnum fromString(final String text) throws GeneratorException {
		if (text != null) {
			for (final SpellTypeEnum v : SpellTypeEnum.values()) {
				if (text.trim().equalsIgnoreCase(v.name())) {
					return v;
				}
			}
		}
		throw new GeneratorException("Unknown spell type: " + text);
	}

}
