package com.pourbaix.script.creature.spell;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum SchoolEnum {
	Unknown, Abjurer, Conjurer, Diviner, Enchanter, Illusionist, Invoker, Necromancer, Transmuter, Generalist;

	public static SchoolEnum fromString(final String text) throws GeneratorException {
		if (text != null) {
			for (final SchoolEnum v : SchoolEnum.values()) {
				if (text.trim().equalsIgnoreCase(v.name())) {
					return v;
				}
			}
		}
		throw new GeneratorException("Unknown school: " + text);
	}

}
