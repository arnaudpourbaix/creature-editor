package com.pourbaix.infinity.domain;

import java.util.List;

import com.pourbaix.infinity.datatype.AbilityLocationEnum;
import com.pourbaix.infinity.datatype.AbilityTargetEnum;
import com.pourbaix.infinity.datatype.AbilityTypeEnum;

public class Ability {
	private AbilityTypeEnum type;
	private AbilityLocationEnum location;
	private AbilityTargetEnum target;
	private byte targetCount;
	private short range;
	private byte level;
	private byte castingTime;
	private Projectile projectile;
	private List<Effect> effects;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("type: ").append(type.getLabel());
		sb.append(", location: ").append(location.getLabel());
		sb.append(", target: ").append(target.getLabel());
		sb.append(", target count: ").append(targetCount);
		sb.append(", range(feet): ").append(range);
		sb.append(", level: ").append(level);
		sb.append(", casting time: ").append(castingTime);
		if (projectile != null) {
			sb.append(", projectile: ").append(projectile.toString());
		}
		return sb.toString();
	}

	public AbilityTypeEnum getType() {
		return type;
	}

	public void setType(AbilityTypeEnum type) {
		this.type = type;
	}

	public AbilityLocationEnum getLocation() {
		return location;
	}

	public void setLocation(AbilityLocationEnum location) {
		this.location = location;
	}

	public AbilityTargetEnum getTarget() {
		return target;
	}

	public void setTarget(AbilityTargetEnum target) {
		this.target = target;
	}

	public byte getTargetCount() {
		return targetCount;
	}

	public void setTargetCount(byte targetCount) {
		this.targetCount = targetCount;
	}

	public short getRange() {
		return range;
	}

	public void setRange(short range) {
		this.range = range;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public byte getCastingTime() {
		return castingTime;
	}

	public void setCastingTime(byte castingTime) {
		this.castingTime = castingTime;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	public List<Effect> getEffects() {
		return effects;
	}

	public void setEffects(List<Effect> effects) {
		this.effects = effects;
	}

}
