package com.pourbaix.infinity.datatype;

public enum IdsEnum {
	Spell("spell.ids"), Projectile("projectl.ids"), State("state.ids"), Animation("animate.ids"), Allegiance("ea.ids"), General("general.ids"), Race("race.ids"), Class(
			"class.ids"), Specific("specific.ids"), Sex("gender.ids"), Gender("gender.ids"), Alignment("alignmen.ids");

	private String resource;

	private IdsEnum(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return this.resource;
	}
}
