package com.pourbaix.infinity.domain;

import java.util.List;

import com.pourbaix.infinity.datatype.Flag;

public class RawCreature {
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
	private int animation;
	private String animationLabel;
	private byte effectFlag; // 0: Version 1 EFF, 1: Version 2 EFF
	private byte reputation;
	private byte hideInShadows;
	private byte detectIllusion;
	private byte setTraps;
	private byte disarmTraps;
	private byte lockpicking;
	private byte stealth;
	private byte pickPocket;
	private byte lore;
	private byte fatigue;
	private byte intoxication;
	private byte luck;
	private short naturalAC;
	private short effectiveAC;
	private short crushingAC;
	private short missileAC;
	private short piercingAC;
	private short slashingAC;
	private byte thac0;
	private byte attackNumber;
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
	private byte largeSwordProficiency;
	private byte smallSwordProficiency;
	private byte bowProficiency;
	private byte spearProficiency;
	private byte bluntProficiency;
	private byte spikedProficiency;
	private byte axeProficiency;
	private byte missileProficiency;
	private byte turnUndeadLevel;
	private byte trackingSkill;
	private String trackingTarget;
	private byte level1;
	private byte level2;
	private byte level3;
	private int sex;
	private String sexLabel;
	private byte strength;
	private byte strengthBonus;
	private byte intelligence;
	private byte wisdom;
	private byte dexterity;
	private byte constitution;
	private byte charisma;
	private byte morale;
	private byte moraleBreak;
	private int racialEnemy;
	private String racialEnemyLabel;
	private short moraleRecoveryTime;
	private long kit;
	private String kitLabel;
	private String scriptOverride;
	private String scriptClass;
	private String scriptRace;
	private String scriptGeneral;
	private String scriptDefault;
	private int allegiance;
	private String allegianceLabel;
	private int general;
	private String generalLabel;
	private int race;
	private String raceLabel;
	private int classe;
	private String classLabel;
	private int specific;
	private String specificLabel;
	private int gender;
	private String genderLabel;
	private int alignment;
	private String alignmentLabel;
	private short globalIdentifier;
	private short localIdentifier;
	private String deathVariable;
	private String dialogFile;

	private List<Effect> effects;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(resource);
		sb.append(", name: ").append(name).append("/").append(tooltip);
		sb.append(", experience value: ").append(experienceValue);
		sb.append(", power level/xp: ").append(powerLevelOrExperience);
		sb.append(", levels: ").append(level1).append("/").append(level2).append("/").append(level3);
		sb.append(", status: ").append(status);
		sb.append(", hit points: ").append(currentHitPoint).append("/").append(maximumHitPoint);
		sb.append(", animation: ").append(animationLabel);
		sb.append(", hideInShadows: ").append(hideInShadows);
		sb.append(", AC: ").append(naturalAC).append("/").append(effectiveAC);
		sb.append(", AC/type: ").append(crushingAC).append("/").append(missileAC).append("/").append(piercingAC).append("/").append(slashingAC);
		sb.append(", thac0: ").append(thac0);
		sb.append(", #attacks: ").append(attackNumber);
		sb.append(", saves: ").append(saveDeath).append("/").append(saveWand).append("/").append(savePolymorph).append("/").append(saveBreath).append("/")
				.append(saveSpell);
		sb.append(", magical resists: ").append(resistFire).append("/").append(resistCold).append("/").append(resistElectricity).append("/").append(resistAcid)
				.append("/").append(resistMagic).append("/").append(resistMagicFire).append("/").append(resistMagicCold);
		sb.append(", physical resists: ").append(resistSlashing).append("/").append(resistCrushing).append("/").append(resistPiercing).append("/")
				.append(resistMissile);
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

	public int getAnimation() {
		return animation;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
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

	public byte getReputation() {
		return reputation;
	}

	public void setReputation(byte reputation) {
		this.reputation = reputation;
	}

	public byte getHideInShadows() {
		return hideInShadows;
	}

	public void setHideInShadows(byte hideInShadows) {
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

	public byte getAttackNumber() {
		return attackNumber;
	}

	public void setAttackNumber(byte attackNumber) {
		this.attackNumber = attackNumber;
	}

	public byte getSaveDeath() {
		return saveDeath;
	}

	public void setSaveDeath(byte saveDeath) {
		this.saveDeath = saveDeath;
	}

	public byte getSaveWand() {
		return saveWand;
	}

	public void setSaveWand(byte saveWand) {
		this.saveWand = saveWand;
	}

	public byte getSavePolymorph() {
		return savePolymorph;
	}

	public void setSavePolymorph(byte savePolymorph) {
		this.savePolymorph = savePolymorph;
	}

	public byte getSaveBreath() {
		return saveBreath;
	}

	public void setSaveBreath(byte saveBreath) {
		this.saveBreath = saveBreath;
	}

	public byte getSaveSpell() {
		return saveSpell;
	}

	public void setSaveSpell(byte saveSpell) {
		this.saveSpell = saveSpell;
	}

	public byte getResistFire() {
		return resistFire;
	}

	public void setResistFire(byte resistFire) {
		this.resistFire = resistFire;
	}

	public byte getResistCold() {
		return resistCold;
	}

	public void setResistCold(byte resistCold) {
		this.resistCold = resistCold;
	}

	public byte getResistElectricity() {
		return resistElectricity;
	}

	public void setResistElectricity(byte resistElectricity) {
		this.resistElectricity = resistElectricity;
	}

	public byte getResistAcid() {
		return resistAcid;
	}

	public void setResistAcid(byte resistAcid) {
		this.resistAcid = resistAcid;
	}

	public byte getResistMagic() {
		return resistMagic;
	}

	public void setResistMagic(byte resistMagic) {
		this.resistMagic = resistMagic;
	}

	public byte getResistMagicFire() {
		return resistMagicFire;
	}

	public void setResistMagicFire(byte resistMagicFire) {
		this.resistMagicFire = resistMagicFire;
	}

	public byte getResistMagicCold() {
		return resistMagicCold;
	}

	public void setResistMagicCold(byte resistMagicCold) {
		this.resistMagicCold = resistMagicCold;
	}

	public byte getResistSlashing() {
		return resistSlashing;
	}

	public void setResistSlashing(byte resistSlashing) {
		this.resistSlashing = resistSlashing;
	}

	public byte getResistCrushing() {
		return resistCrushing;
	}

	public void setResistCrushing(byte resistCrushing) {
		this.resistCrushing = resistCrushing;
	}

	public byte getResistPiercing() {
		return resistPiercing;
	}

	public void setResistPiercing(byte resistPiercing) {
		this.resistPiercing = resistPiercing;
	}

	public byte getResistMissile() {
		return resistMissile;
	}

	public void setResistMissile(byte resistMissile) {
		this.resistMissile = resistMissile;
	}

	public byte getDetectIllusion() {
		return detectIllusion;
	}

	public void setDetectIllusion(byte detectIllusion) {
		this.detectIllusion = detectIllusion;
	}

	public byte getSetTraps() {
		return setTraps;
	}

	public void setSetTraps(byte setTraps) {
		this.setTraps = setTraps;
	}

	public byte getDisarmTraps() {
		return disarmTraps;
	}

	public void setDisarmTraps(byte disarmTraps) {
		this.disarmTraps = disarmTraps;
	}

	public byte getLockpicking() {
		return lockpicking;
	}

	public void setLockpicking(byte lockpicking) {
		this.lockpicking = lockpicking;
	}

	public byte getStealth() {
		return stealth;
	}

	public void setStealth(byte stealth) {
		this.stealth = stealth;
	}

	public byte getPickPocket() {
		return pickPocket;
	}

	public void setPickPocket(byte pickPocket) {
		this.pickPocket = pickPocket;
	}

	public byte getLore() {
		return lore;
	}

	public void setLore(byte lore) {
		this.lore = lore;
	}

	public byte getFatigue() {
		return fatigue;
	}

	public void setFatigue(byte fatigue) {
		this.fatigue = fatigue;
	}

	public byte getIntoxication() {
		return intoxication;
	}

	public void setIntoxication(byte intoxication) {
		this.intoxication = intoxication;
	}

	public byte getLuck() {
		return luck;
	}

	public void setLuck(byte luck) {
		this.luck = luck;
	}

	public byte getLargeSwordProficiency() {
		return largeSwordProficiency;
	}

	public void setLargeSwordProficiency(byte largeSwordProficiency) {
		this.largeSwordProficiency = largeSwordProficiency;
	}

	public byte getSmallSwordProficiency() {
		return smallSwordProficiency;
	}

	public void setSmallSwordProficiency(byte smallSwordProficiency) {
		this.smallSwordProficiency = smallSwordProficiency;
	}

	public byte getBowProficiency() {
		return bowProficiency;
	}

	public void setBowProficiency(byte bowProficiency) {
		this.bowProficiency = bowProficiency;
	}

	public byte getSpearProficiency() {
		return spearProficiency;
	}

	public void setSpearProficiency(byte spearProficiency) {
		this.spearProficiency = spearProficiency;
	}

	public byte getBluntProficiency() {
		return bluntProficiency;
	}

	public void setBluntProficiency(byte bluntProficiency) {
		this.bluntProficiency = bluntProficiency;
	}

	public byte getSpikedProficiency() {
		return spikedProficiency;
	}

	public void setSpikedProficiency(byte spikedProficiency) {
		this.spikedProficiency = spikedProficiency;
	}

	public byte getAxeProficiency() {
		return axeProficiency;
	}

	public void setAxeProficiency(byte axeProficiency) {
		this.axeProficiency = axeProficiency;
	}

	public byte getMissileProficiency() {
		return missileProficiency;
	}

	public void setMissileProficiency(byte missileProficiency) {
		this.missileProficiency = missileProficiency;
	}

	public byte getTurnUndeadLevel() {
		return turnUndeadLevel;
	}

	public void setTurnUndeadLevel(byte turnUndeadLevel) {
		this.turnUndeadLevel = turnUndeadLevel;
	}

	public byte getTrackingSkill() {
		return trackingSkill;
	}

	public void setTrackingSkill(byte trackingSkill) {
		this.trackingSkill = trackingSkill;
	}

	public String getTrackingTarget() {
		return trackingTarget;
	}

	public void setTrackingTarget(String trackingTarget) {
		this.trackingTarget = trackingTarget;
	}

	public byte getLevel1() {
		return level1;
	}

	public void setLevel1(byte level1) {
		this.level1 = level1;
	}

	public byte getLevel2() {
		return level2;
	}

	public void setLevel2(byte level2) {
		this.level2 = level2;
	}

	public byte getLevel3() {
		return level3;
	}

	public void setLevel3(byte level3) {
		this.level3 = level3;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public byte getStrength() {
		return strength;
	}

	public void setStrength(byte strength) {
		this.strength = strength;
	}

	public byte getStrengthBonus() {
		return strengthBonus;
	}

	public void setStrengthBonus(byte strengthBonus) {
		this.strengthBonus = strengthBonus;
	}

	public byte getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(byte intelligence) {
		this.intelligence = intelligence;
	}

	public byte getWisdom() {
		return wisdom;
	}

	public void setWisdom(byte wisdom) {
		this.wisdom = wisdom;
	}

	public byte getDexterity() {
		return dexterity;
	}

	public void setDexterity(byte dexterity) {
		this.dexterity = dexterity;
	}

	public byte getConstitution() {
		return constitution;
	}

	public void setConstitution(byte constitution) {
		this.constitution = constitution;
	}

	public byte getCharisma() {
		return charisma;
	}

	public void setCharisma(byte charisma) {
		this.charisma = charisma;
	}

	public byte getMorale() {
		return morale;
	}

	public void setMorale(byte morale) {
		this.morale = morale;
	}

	public byte getMoraleBreak() {
		return moraleBreak;
	}

	public void setMoraleBreak(byte moraleBreak) {
		this.moraleBreak = moraleBreak;
	}

	public int getRacialEnemy() {
		return racialEnemy;
	}

	public void setRacialEnemy(int racialEnemy) {
		this.racialEnemy = racialEnemy;
	}

	public short getMoraleRecoveryTime() {
		return moraleRecoveryTime;
	}

	public void setMoraleRecoveryTime(short moraleRecoveryTime) {
		this.moraleRecoveryTime = moraleRecoveryTime;
	}

	public long getKit() {
		return kit;
	}

	public void setKit(long kit) {
		this.kit = kit;
	}

	public String getKitLabel() {
		return kitLabel;
	}

	public void setKitLabel(String kitLabel) {
		this.kitLabel = kitLabel;
	}

	public String getScriptOverride() {
		return scriptOverride;
	}

	public void setScriptOverride(String scriptOverride) {
		this.scriptOverride = scriptOverride;
	}

	public String getScriptClass() {
		return scriptClass;
	}

	public void setScriptClass(String scriptClass) {
		this.scriptClass = scriptClass;
	}

	public String getScriptRace() {
		return scriptRace;
	}

	public void setScriptRace(String scriptRace) {
		this.scriptRace = scriptRace;
	}

	public String getScriptGeneral() {
		return scriptGeneral;
	}

	public void setScriptGeneral(String scriptGeneral) {
		this.scriptGeneral = scriptGeneral;
	}

	public String getScriptDefault() {
		return scriptDefault;
	}

	public void setScriptDefault(String scriptDefault) {
		this.scriptDefault = scriptDefault;
	}

	public int getAllegiance() {
		return allegiance;
	}

	public void setAllegiance(int allegiance) {
		this.allegiance = allegiance;
	}

	public int getGeneral() {
		return general;
	}

	public void setGeneral(int general) {
		this.general = general;
	}

	public int getRace() {
		return race;
	}

	public void setRace(int race) {
		this.race = race;
	}

	public int getClasse() {
		return classe;
	}

	public void setClasse(int classe) {
		this.classe = classe;
	}

	public int getSpecific() {
		return specific;
	}

	public void setSpecific(int specific) {
		this.specific = specific;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public short getGlobalIdentifier() {
		return globalIdentifier;
	}

	public void setGlobalIdentifier(short globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public short getLocalIdentifier() {
		return localIdentifier;
	}

	public void setLocalIdentifier(short localIdentifier) {
		this.localIdentifier = localIdentifier;
	}

	public String getDeathVariable() {
		return deathVariable;
	}

	public void setDeathVariable(String deathVariable) {
		this.deathVariable = deathVariable;
	}

	public String getDialogFile() {
		return dialogFile;
	}

	public void setDialogFile(String dialogFile) {
		this.dialogFile = dialogFile;
	}

	public String getSexLabel() {
		return sexLabel;
	}

	public void setSexLabel(String sexLabel) {
		this.sexLabel = sexLabel;
	}

	public String getRacialEnemyLabel() {
		return racialEnemyLabel;
	}

	public void setRacialEnemyLabel(String racialEnemyLabel) {
		this.racialEnemyLabel = racialEnemyLabel;
	}

	public String getAllegianceLabel() {
		return allegianceLabel;
	}

	public void setAllegianceLabel(String allegianceLabel) {
		this.allegianceLabel = allegianceLabel;
	}

	public String getGeneralLabel() {
		return generalLabel;
	}

	public void setGeneralLabel(String generalLabel) {
		this.generalLabel = generalLabel;
	}

	public String getRaceLabel() {
		return raceLabel;
	}

	public void setRaceLabel(String raceLabel) {
		this.raceLabel = raceLabel;
	}

	public String getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}

	public String getSpecificLabel() {
		return specificLabel;
	}

	public void setSpecificLabel(String specificLabel) {
		this.specificLabel = specificLabel;
	}

	public String getGenderLabel() {
		return genderLabel;
	}

	public void setGenderLabel(String genderLabel) {
		this.genderLabel = genderLabel;
	}

	public String getAlignmentLabel() {
		return alignmentLabel;
	}

	public void setAlignmentLabel(String alignmentLabel) {
		this.alignmentLabel = alignmentLabel;
	}

}
