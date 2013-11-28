package com.pourbaix.creature.script.context;

import org.springframework.beans.factory.annotation.Value;

public class TargetContext {

	@Value("${target.allegiance}")
	private String allegiance;

	@Value("${target.nearestCount}")
	private int nearestCount;

	@Value("${target.typeCount}")
	private int typeCount;

	@Value("${target.random}")
	private boolean random;

	public String getAllegiance() {
		return allegiance;
	}

	public void setAllegiance(String allegiance) {
		this.allegiance = allegiance;
	}

	public int getNearestCount() {
		return nearestCount;
	}

	public void setNearestCount(int nearestCount) {
		this.nearestCount = nearestCount;
	}

	public int getTypeCount() {
		return typeCount;
	}

	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

}
