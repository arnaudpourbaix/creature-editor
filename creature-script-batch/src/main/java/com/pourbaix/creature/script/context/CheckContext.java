package com.pourbaix.creature.script.context;

import org.springframework.beans.factory.annotation.Value;

public class CheckContext {

	@Value("${check.spellProtection}")
	private boolean spellProtection;

	@Value("${check.weaponProtection}")
	private boolean weaponProtection;

	@Value("${check.mindImmunity}")
	private boolean mindImmunity;

	@Value("${check.deathImmunity}")
	private boolean deathImmunity;

	@Value("${check.elementalResistance}")
	private boolean elementalResistance;

	@Value("${check.poisonResistance}")
	private boolean poisonResistance;

	@Value("${check.magicDamageResistance}")
	private boolean magicDamageResistance;

	@Value("${check.magicResistance}")
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
