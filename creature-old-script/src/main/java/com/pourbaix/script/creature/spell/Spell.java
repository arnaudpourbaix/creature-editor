package com.pourbaix.script.creature.spell;

public class Spell {

	private String name;
	private String identifier;
	private String resource;
	private int level;

	private SpellTypeEnum type;
	private DamageTypeEnum damageType;

	private int range;
	private int radius = 0;
	private boolean canHurtAllies = false;

	private SchoolEnum school;
	private ClassEnum casterClass = ClassEnum.UNKNOWN;

	private String detectableStat;
	private int detectableValue;

	private boolean targetEnemy;
	private String selfConditions;
	private String defaultTarget;
	private String targetConditions;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(final String resource) {
		this.resource = resource;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	public SpellTypeEnum getType() {
		return type;
	}

	public void setType(final SpellTypeEnum type) {
		this.type = type;
	}

	public DamageTypeEnum getDamageType() {
		return damageType;
	}

	public void setDamageType(final DamageTypeEnum damageType) {
		this.damageType = damageType;
	}

	public int getRange() {
		return range;
	}

	public void setRange(final int range) {
		this.range = range;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(final int radius) {
		this.radius = radius;
	}

	public boolean isCanHurtAllies() {
		return canHurtAllies;
	}

	public void setCanHurtAllies(final boolean canHurtAllies) {
		this.canHurtAllies = canHurtAllies;
	}

	public SchoolEnum getSchool() {
		return school;
	}

	public void setSchool(final SchoolEnum school) {
		this.school = school;
	}

	public ClassEnum getCasterClass() {
		return casterClass;
	}

	public void setCasterClass(final ClassEnum casterClass) {
		this.casterClass = casterClass;
	}

	public String getDetectableStat() {
		return detectableStat;
	}

	public void setDetectableStat(final String detectableStat) {
		this.detectableStat = detectableStat;
	}

	public int getDetectableValue() {
		return detectableValue;
	}

	public void setDetectableValue(final int detectableValue) {
		this.detectableValue = detectableValue;
	}

	public boolean isTargetEnemy() {
		return targetEnemy;
	}

	public void setTargetEnemy(final boolean targetEnemy) {
		this.targetEnemy = targetEnemy;
	}

	public String getSelfConditions() {
		return selfConditions;
	}

	public void setSelfConditions(final String selfConditions) {
		this.selfConditions = selfConditions;
	}

	public String getDefaultTarget() {
		return defaultTarget;
	}

	public void setDefaultTarget(final String defaultTarget) {
		this.defaultTarget = defaultTarget;
	}

	public String getTargetConditions() {
		return targetConditions;
	}

	public void setTargetConditions(final String targetConditions) {
		this.targetConditions = targetConditions;
	}

}
