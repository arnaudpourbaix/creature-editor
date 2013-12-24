package com.pourbaix.infinity.datatype;

public enum IdsEnum {
	Spell("spell.ids"), Projectile("projectl.ids"), State("state.ids"), Animation("animate.ids");

	private String resource;

	private IdsEnum(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return this.resource;
	}
}
