package com.pourbaix.infinity.domain;

import java.util.List;

import com.pourbaix.infinity.datatype.Flag;

public class Creature {
	private String resource;
	private String name;
	private String tooltip;
	private Flag flags;
	private int experienceValue;
	private int powerLevelOrExperience;
	private int gold;
	private String status;
	private short currentHitPoint;
	private short maximumHitPoint;
	private int animationId;
	private String animationLabel;
	private byte effectFlag; // 0: Version 1 EFF, 1: Version 2 EFF
	private short reputation;
	private short hideInShadows;
	private short naturalAC;
	private short effectiveAC;
	private short crushingAC;
	private short missileAC;
	private short piercingAC;
	private short slashingAC;
	private byte thac0;
	private float attackCount;

	private byte saveDeath;
	private byte saveWand;
	private byte savePolymorph;
	private byte saveBreath;
	private byte saveSpell;

	private byte resistFire;
	private byte resistCold;
	private byte resistElectricity;
	private byte resistAcid;
	private byte resistMagic;
	private byte resistMagicFire;
	private byte resistMagicCold;
	private byte resistSlashing;
	private byte resistCrushing;
	private byte resistPiercing;
	private byte resistMissile;

	private List<Effect> effects;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(resource);
		sb.append(", name: ").append(name).append("/").append(tooltip);
		// sb.append(", flags: ").append(flags);
		sb.append(", experience value: ").append(experienceValue);
		sb.append(", power level/xp: ").append(powerLevelOrExperience);
		sb.append(", status: ").append(status);
		sb.append(", hit points: ").append(currentHitPoint).append("/").append(maximumHitPoint);
		sb.append(", animation: ").append(animationLabel);
		sb.append(", hideInShadows: ").append(hideInShadows);
		sb.append(", animation: ").append(animationLabel);
		sb.append(", AC: ").append(naturalAC).append("/").append(effectiveAC);
		sb.append(", AC/type: ").append(crushingAC).append("/").append(missileAC).append("/").append(piercingAC).append("/").append(slashingAC);
		sb.append(", thac0: ").append(thac0);
		return sb.toString();
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public Flag getFlags() {
		return flags;
	}

	public void setFlags(Flag flags) {
		this.flags = flags;
	}

	public int getExperienceValue() {
		return experienceValue;
	}

	public void setExperienceValue(int experienceValue) {
		this.experienceValue = experienceValue;
	}

	public int getPowerLevelOrExperience() {
		return powerLevelOrExperience;
	}

	public void setPowerLevelOrExperience(int powerLevelOrExperience) {
		this.powerLevelOrExperience = powerLevelOrExperience;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public short getCurrentHitPoint() {
		return currentHitPoint;
	}

	public void setCurrentHitPoint(short currentHitPoint) {
		this.currentHitPoint = currentHitPoint;
	}

	public short getMaximumHitPoint() {
		return maximumHitPoint;
	}

	public void setMaximumHitPoint(short maximumHitPoint) {
		this.maximumHitPoint = maximumHitPoint;
	}

	public int getAnimationId() {
		return animationId;
	}

	public void setAnimationId(int animationId) {
		this.animationId = animationId;
	}

	public String getAnimationLabel() {
		return animationLabel;
	}

	public void setAnimationLabel(String animationLabel) {
		this.animationLabel = animationLabel;
	}

	public List<Effect> getEffects() {
		return effects;
	}

	public void setEffects(List<Effect> effects) {
		this.effects = effects;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte getEffectFlag() {
		return effectFlag;
	}

	public void setEffectFlag(byte effectFlag) {
		this.effectFlag = effectFlag;
	}

	public short getReputation() {
		return reputation;
	}

	public void setReputation(short reputation) {
		this.reputation = reputation;
	}

	public short getHideInShadows() {
		return hideInShadows;
	}

	public void setHideInShadows(short hideInShadows) {
		this.hideInShadows = hideInShadows;
	}

	public short getNaturalAC() {
		return naturalAC;
	}

	public void setNaturalAC(short naturalAC) {
		this.naturalAC = naturalAC;
	}

	public short getEffectiveAC() {
		return effectiveAC;
	}

	public void setEffectiveAC(short effectiveAC) {
		this.effectiveAC = effectiveAC;
	}

	public short getCrushingAC() {
		return crushingAC;
	}

	public void setCrushingAC(short crushingAC) {
		this.crushingAC = crushingAC;
	}

	public short getMissileAC() {
		return missileAC;
	}

	public void setMissileAC(short missileAC) {
		this.missileAC = missileAC;
	}

	public short getPiercingAC() {
		return piercingAC;
	}

	public void setPiercingAC(short piercingAC) {
		this.piercingAC = piercingAC;
	}

	public short getSlashingAC() {
		return slashingAC;
	}

	public void setSlashingAC(short slashingAC) {
		this.slashingAC = slashingAC;
	}

	public byte getThac0() {
		return thac0;
	}

	public void setThac0(byte thac0) {
		this.thac0 = thac0;
	}

}
