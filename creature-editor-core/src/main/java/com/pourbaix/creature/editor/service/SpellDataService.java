package com.pourbaix.creature.editor.service;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.pourbaix.creature.editor.domain.SpellFlag;
import com.pourbaix.creature.editor.domain.SpellFlagEnum;

@Service
public class SpellDataService {

	public static final ImmutableList<SpellFlag> flags = ImmutableList.of(new SpellFlag(12, SpellFlagEnum.HostileBreakInvisibility,
			"Hostile/Breaks Invisibility"), new SpellFlag(13, SpellFlagEnum.NoLOS, "No LOS required"), new SpellFlag(14, SpellFlagEnum.AllowSpotting,
			"Allow spotting"), new SpellFlag(15, SpellFlagEnum.OutdoorsOnly, "Outdoors only"), new SpellFlag(16, SpellFlagEnum.NonMagicalAbility,
			"Non-magical ability"), new SpellFlag(17, SpellFlagEnum.TriggerContingency, "Trigger/Contingency"), new SpellFlag(18,
			SpellFlagEnum.NonCombatAbility, "Non-combat ability"), new SpellFlag(26, SpellFlagEnum.CanTargetInvisible, "Can target invisible"), new SpellFlag(
			27, SpellFlagEnum.CastableWhenSilenced, "Castable when silenced"));

	public static final ImmutableList<SpellFlag> exclusionFlags = ImmutableList.of(new SpellFlag(12, SpellFlagEnum.HostileBreakInvisibility,
			"Hostile/Breaks Invisibility"), new SpellFlag(13, SpellFlagEnum.NoLOS, "No LOS required"), new SpellFlag(14, SpellFlagEnum.AllowSpotting,
			"Allow spotting"), new SpellFlag(15, SpellFlagEnum.OutdoorsOnly, "Outdoors only"), new SpellFlag(16, SpellFlagEnum.NonMagicalAbility,
			"Non-magical ability"), new SpellFlag(17, SpellFlagEnum.TriggerContingency, "Trigger/Contingency"), new SpellFlag(18,
			SpellFlagEnum.NonCombatAbility, "Non-combat ability"), new SpellFlag(26, SpellFlagEnum.CanTargetInvisible, "Can target invisible"), new SpellFlag(
			27, SpellFlagEnum.CastableWhenSilenced, "Castable when silenced"));

}
