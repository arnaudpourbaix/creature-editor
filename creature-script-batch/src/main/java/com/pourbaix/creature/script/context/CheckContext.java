package com.pourbaix.creature.script.context;


public class CheckContext {

	private boolean spellProtection;
	private boolean weaponProtection;
	private boolean mindImmunity;
	private boolean deathImmunity;
	private boolean elementalResistance;
	private boolean poisonResistance;
	private boolean magicDamageResistance;
	private boolean magicResistance;

	public boolean isSpellProtection() {
		return spellProtection;
	}

	public void setSpellProtection(boolean spellProtection) {
		this.spellProtection = spellProtection;
	}

	public boolean isWeaponProtection() {
		return weaponProtection;
	}

	public void setWeaponProtection(boolean weaponProtection) {
		this.weaponProtection = weaponProtection;
	}

	public boolean isMindImmunity() {
		return mindImmunity;
	}

	public void setMindImmunity(boolean mindImmunity) {
		this.mindImmunity = mindImmunity;
	}

	public boolean isDeathImmunity() {
		return deathImmunity;
	}

	public void setDeathImmunity(boolean deathImmunity) {
		this.deathImmunity = deathImmunity;
	}

	public boolean isElementalResistance() {
		return elementalResistance;
	}

	public void setElementalResistance(boolean elementalResistance) {
		this.elementalResistance = elementalResistance;
	}

	public boolean isPoisonResistance() {
		return poisonResistance;
	}

	public void setPoisonResistance(boolean poisonResistance) {
		this.poisonResistance = poisonResistance;
	}

	public boolean isMagicDamageResistance() {
		return magicDamageResistance;
	}

	public void setMagicDamageResistance(boolean magicDamageResistance) {
		this.magicDamageResistance = magicDamageResistance;
	}

	public boolean isMagicResistance() {
		return magicResistance;
	}

	public void setMagicResistance(boolean magicResistance) {
		this.magicResistance = magicResistance;
	}

}
