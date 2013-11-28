package com.pourbaix.script.creature.context;

import com.pourbaix.script.creature.spell.ClassEnum;

public class LocalContext {

	private static LocalContext instance;

	private ClassEnum casterClass;
	private int casterLevel;
	private int casterMaxSpellLevel;

	private LocalContext() {

	}

	public static LocalContext getInstance() {
		if (instance == null) {
			instance = new LocalContext();
		}
		return instance;
	}

	public ClassEnum getCasterClass() {
		return casterClass;
	}

	public void setCasterClass(final ClassEnum casterClass) {
		this.casterClass = casterClass;
	}

	public int getCasterLevel() {
		return casterLevel;
	}

	public void setCasterLevel(int casterLevel) {
		this.casterLevel = casterLevel;
	}

	public int getCasterMaxSpellLevel() {
		return casterMaxSpellLevel;
	}

	public void setCasterMaxSpellLevel(int casterMaxSpellLevel) {
		this.casterMaxSpellLevel = casterMaxSpellLevel;
	}
	
}
