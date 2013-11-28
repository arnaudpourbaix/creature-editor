package old;

import com.pourbaix.script.creature.generator.GeneratorException;

public class Spell {

	private String name;
	private String identifier;
	private String resource;
	private int level;
	private String target;
	private String defaultTarget;
	private String selfConditions;
	private String targetConditions;
	private String type;
	private String damageType;
	private int range;
	private int radius = 0;
	private boolean canHurtAllies = false;
	private SchoolEnum school;
	private String casterType;
	private String detectableStat;
	private String detectableValue;
	
	public boolean isTeleport() {
		return "TELEPORT".equalsIgnoreCase(type);
	}

	public String getCastAction(String object) {
		return getCastAction(Constant.SpellAction.SPELL, object, false);
	}

	public String getCastAction(String spellAction, String object) {
		return getCastAction(spellAction, object, false);
	}
	
	public String getCastAction(String spellAction, String object, boolean forceTargetObject) {
		if (!forceTargetObject && isTargetSelf())
			object = TargetEnum.ALLY.getCode();
		if (!identifier.isEmpty())
			return Tools.generateString(spellAction, object, identifier);
		else
			return Tools.generateString(spellAction + "RES", Tools.encloseString(resource), object); 
	}

	public Condition getHaveSpellCondition() throws GeneratorException {
		return getHaveSpellCondition(true);
	}
	
	public Condition getHaveSpellCondition(boolean result) throws GeneratorException {
		if (!identifier.isEmpty())
			return new Condition(ConditionEnum.HaveSpell, result, identifier);
		else
			return new Condition(ConditionEnum.HaveSpellRES, result, Tools.encloseString(resource));
	}

	public boolean isTargetSelf() {
		return Tools.equalsIgnoreCase(target, "self");
	}
	
	public Condition getDetectableCondition(String object) throws GeneratorException {
		if (detectableStat == null) {
			throw new GeneratorException("no stat in spell " + resource);
		}
		if (isTargetSelf())
			object = TargetEnum.ALLY.getCode();
		return new Condition(ConditionEnum.CheckStat, false, object, detectableValue, detectableStat);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSelfConditions() {
		return selfConditions;
	}
	public void setSelfConditions(String selfConditions) {
		this.selfConditions = selfConditions;
	}
	public String getTargetConditions() {
		return targetConditions;
	}
	public void setTargetConditions(String targetConditions) {
		this.targetConditions = targetConditions;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDamageType() {
		return damageType;
	}
	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public boolean isCanHurtAllies() {
		return canHurtAllies;
	}
	public void setCanHurtAllies(boolean canHurtAllies) {
		this.canHurtAllies = canHurtAllies;
	}
	public SchoolEnum getSchool() {
		return school;
	}
	public void setSchool(SchoolEnum school) {
		this.school = school;
	}
	public String getDefaultTarget() {
		return defaultTarget;
	}
	public void setDefaultTarget(String defaultTarget) {
		this.defaultTarget = defaultTarget;
	}
	public String getCasterType() {
		return casterType;
	}
	public void setCasterType(String casterType) {
		this.casterType = casterType;
	}

	public String getDetectableStat() {
		return detectableStat;
	}

	public void setDetectableStat(String detectableStat) {
		this.detectableStat = detectableStat;
	}

	public String getDetectableValue() {
		return detectableValue;
	}

	public void setDetectableValue(String detectableValue) {
		this.detectableValue = detectableValue;
	}
		
}
