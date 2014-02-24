package com.pourbaix.creature.editor.domain;

public enum SpellFlagEnum {
	HostileBreakInvisibility("Hostile/Breaks Invisibility"), NoLOS("No LOS required"), AllowSpotting("Allow spotting"), OutdoorsOnly("Outdoors only"), NonMagicalAbility(
			"Non-magical ability"), TriggerContingency("Trigger/Contingency"), NonCombatAbility("Non-combat ability"), CanTargetInvisible(
			"Can target invisible"), CastableWhenSilenced("Castable when silenced");

	private String label;

	private SpellFlagEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
