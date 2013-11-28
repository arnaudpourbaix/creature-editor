package com.pourbaix.script.creature.spell;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pourbaix.script.creature.context.GlobalContext;
import com.pourbaix.script.creature.context.LocalContext;
import com.pourbaix.script.creature.generator.GeneratorException;
import com.pourbaix.script.creature.utils.Tools;

@Service
public class SpellFactory {

	@Resource
	private GlobalContext globalContext;

	public Spell getSpell(final String name) throws GeneratorException {
		final LocalContext context = LocalContext.getInstance();
		Spell spell = null;
		try {
			if (context.getCasterClass() != null) {
				spell = getSpell(name, context.getCasterClass().toString());
				if (spell == null) {
					spell = getSpell(name, context.getCasterClass().getGenericClass());
				}
			}
			if (spell == null) {
				spell = getSpell(name, ClassEnum.UNKNOWN.toString());
			}
		} catch (final GeneratorException exc) {
			throw exc;
		}
		if (spell == null) {
			throw new GeneratorException("Spell not found : " + name);
		}
		return spell;
	}

	public Spell getSpell(final String name, final String casterClass) throws GeneratorException {
		Spell spell = null;
		int count = 0;
		for (final Spell s : globalContext.getSpells()) {
			if (casterClass.isEmpty() || casterClass.equals(s.getCasterClass().toString())) {
				if (Tools.equalsIgnoreCase(s.getName(), name.trim()) || Tools.equalsIgnoreCase(s.getIdentifier(), name.trim())
						|| Tools.equalsIgnoreCase(s.getResource(), name.trim())) {
					spell = s;
					count++;
				}
			}
		}
		if (count > 1) {
			throw new GeneratorException("Spell found several times: " + name.trim());
		}
		return spell;
	}

}
