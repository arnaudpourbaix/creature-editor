package com.pourbaix.infinity.entity;

public class Projectile {

	private long reference;
	private String resource;
	private ProjectileTypeEnum type;
	private int speed;
	private Flag behaviorFlags;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("resource: ").append(resource);
		sb.append(", reference: ").append(reference);
		sb.append(", type: ").append(type.getLabel());
		sb.append(", speed: ").append(speed);
		sb.append(", behavior: ").append(behaviorFlags.toString());
		return sb.toString();
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

}
