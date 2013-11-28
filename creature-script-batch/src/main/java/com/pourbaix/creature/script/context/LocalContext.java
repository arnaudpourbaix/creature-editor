package com.pourbaix.creature.script.context;


public class LocalContext {

	private static LocalContext instance;

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
