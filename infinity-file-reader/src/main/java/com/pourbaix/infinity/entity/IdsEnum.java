package com.pourbaix.infinity.entity;

public enum IdsEnum {
	Spell("spell.ids"), Projectile("projectl.ids");

	private String resource;

	private IdsEnum(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return this.resource;
	}
}
