package com.pourbaix.script.creature.action;

import com.pourbaix.script.creature.spell.Spell;


public class SpellAction extends BasicAction implements Action {

	private Spell spell;
	private boolean identifier;

	public SpellAction(final ActionEnum name) {
		super(name);
	}

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(final Spell spell) {
		this.spell = spell;
	}

	public boolean isIdentifier() {
		return identifier;
	}

	public void setIdentifier(final boolean identifier) {
		this.identifier = identifier;
	}

}
