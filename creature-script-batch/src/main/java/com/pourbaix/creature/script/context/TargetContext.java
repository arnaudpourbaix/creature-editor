package com.pourbaix.creature.script.context;


public class TargetContext {

	private String allegiance;
	private int nearestCount;
	private int typeCount;
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
