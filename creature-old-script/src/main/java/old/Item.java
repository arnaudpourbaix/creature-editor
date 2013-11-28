package old;

import com.pourbaix.script.creature.generator.GeneratorException;

public class Item {

	private String name;
	private String resource;
	private String target;
	private String selfConditions;
	private String targetConditions;
	private String type;
	private int level;
	private String damageType;
	private boolean onlyBG2;
	private int radius = 0;
	private boolean canHurtAllies = false;

	public Condition getHasItemCondition(boolean equiped, boolean tutu) throws GeneratorException {
		String r = this.resource;
		if (tutu && !r.startsWith("_"))
			r = "_" + resource;
		if (equiped)
			return new Condition(ConditionEnum.HasItemEquiped, Tools.encloseString(r), TargetEnum.MYSELF.getCode());
		else
			return new Condition(ConditionEnum.HasItem, Tools.encloseString(r), TargetEnum.MYSELF.getCode());
	}

	public String getUseAction(String object) {
		return getUseAction(object, false);
	}
	
	public String getUseAction(String object, boolean tutu) {
		if (isTargetSelf())
			object = TargetEnum.MYSELF.getCode();
		String r = this.resource;
		if (tutu && !r.startsWith("_"))
			r = "_" + resource;
		return Tools.generateString(ActionEnum.UseItem.toString(), Tools.encloseString(r), object); 
	}
	
	public boolean isTargetSelf() {
		return Tools.equalsIgnoreCase(target, "self");
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
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
	public boolean isOnlyBG2() {
		return onlyBG2;
	}
	public void setOnlyBG2(boolean onlyBG2) {
		this.onlyBG2 = onlyBG2;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
		
}
