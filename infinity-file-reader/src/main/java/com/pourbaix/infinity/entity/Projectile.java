package com.pourbaix.infinity.entity;

import com.pourbaix.infinity.util.Constant;

public class Projectile {

	private long reference;
	private String resource;
	private ProjectileTypeEnum type;
	private int speed;
	private Flag behaviorFlags;
	private Flag areaOfEffectFlags;
	private int triggerRadius;
	private int areaOfEffectRadius;
	private int coneWidth;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("resource: ").append(resource);
		sb.append(", reference: ").append(reference);
		sb.append(", type: ").append(type.getLabel());
		sb.append(", speed: ").append(speed);
		sb.append(", behavior: ").append(behaviorFlags.toString());
		sb.append(", area of effect flags: ").append(areaOfEffectFlags.toString());
		sb.append(", affect allies: ").append(affectAllies());
		sb.append(", trigger radius (feet): ").append(getFeetTriggerRadius());
		sb.append(", area of effect radius (feet): ").append(getFeetAreaOfEffectRadius());
		sb.append(", cone width (feet): ").append(getFeetConeWidth());
		return sb.toString();
	}

	public boolean affectAllies() {
		return !areaOfEffectFlags.isFlagSet(6);
	}

	public int getFeetTriggerRadius() {
		return (int) (triggerRadius / Constant.RADIUS_TO_FEET_DIVIDER);
	}

	public int getFeetAreaOfEffectRadius() {
		return (int) (areaOfEffectRadius / Constant.RADIUS_TO_FEET_DIVIDER);
	}

	public int getFeetConeWidth() {
		return (int) (coneWidth / Constant.RADIUS_TO_FEET_DIVIDER);
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public long getReference() {
		return reference;
	}

	public void setReference(long reference) {
		this.reference = reference;
	}

	public ProjectileTypeEnum getType() {
		return type;
	}

	public void setType(ProjectileTypeEnum type) {
		this.type = type;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Flag getBehaviorFlags() {
		return behaviorFlags;
	}

	public void setBehaviorFlags(Flag behaviorFlags) {
		this.behaviorFlags = behaviorFlags;
	}

	public Flag getAreaOfEffectFlags() {
		return areaOfEffectFlags;
	}

	public void setAreaOfEffectFlags(Flag areaOfEffectFlags) {
		this.areaOfEffectFlags = areaOfEffectFlags;
	}

	public int getTriggerRadius() {
		return triggerRadius;
	}

	public void setTriggerRadius(int triggerRadius) {
		this.triggerRadius = triggerRadius;
	}

	public int getAreaOfEffectRadius() {
		return areaOfEffectRadius;
	}

	public void setAreaOfEffectRadius(int areaOfEffectRadius) {
		this.areaOfEffectRadius = areaOfEffectRadius;
	}

	public int getConeWidth() {
		return coneWidth;
	}

	public void setConeWidth(int coneWidth) {
		this.coneWidth = coneWidth;
	}

}
