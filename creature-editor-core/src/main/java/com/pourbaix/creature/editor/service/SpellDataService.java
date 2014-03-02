package com.pourbaix.creature.editor.service;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.pourbaix.creature.editor.domain.SpellExclusionFlag;
import com.pourbaix.creature.editor.domain.SpellExclusionFlagEnum;
import com.pourbaix.creature.editor.domain.SpellFlag;
import com.pourbaix.creature.editor.domain.SpellFlagEnum;

@Service
public class SpellDataService {

	public static final ImmutableList<SpellFlag> flags = ImmutableList.of(new SpellFlag(10, SpellFlagEnum.HostileBreakInvisibility,
			"Hostile/Breaks Invisibility"), new SpellFlag(11, SpellFlagEnum.NoLOS, "No LOS required"), new SpellFlag(12, SpellFlagEnum.AllowSpotting,
			"Allow spotting"), new SpellFlag(13, SpellFlagEnum.OutdoorsOnly, "Outdoors only"), new SpellFlag(14, SpellFlagEnum.NonMagicalAbility,
			"Non-magical ability"), new SpellFlag(15, SpellFlagEnum.TriggerContingency, "Trigger/Contingency"), new SpellFlag(16,
			SpellFlagEnum.NonCombatAbility, "Non-combat ability"), new SpellFlag(24, SpellFlagEnum.CanTargetInvisible, "Can target invisible"), new SpellFlag(
			25, SpellFlagEnum.CastableWhenSilenced, "Castable when silenced"));

	public static final ImmutableList<SpellExclusionFlag> exclusionFlags = ImmutableList.of(new SpellExclusionFlag(0, SpellExclusionFlagEnum.ChaoticPriests,
			"Chaotic Priests"), new SpellExclusionFlag(1, SpellExclusionFlagEnum.EvilPriests, "Evil Priests"), new SpellExclusionFlag(2,
			SpellExclusionFlagEnum.GoodPriests, "Good Priests"), new SpellExclusionFlag(3, SpellExclusionFlagEnum.GENeutralPriests, "GE Neutral Priests"),
			new SpellExclusionFlag(4, SpellExclusionFlagEnum.LawfulPriests, "Lawful Priests"), new SpellExclusionFlag(5,
					SpellExclusionFlagEnum.LCNeutralPriests, "LC Neutral Priests"), new SpellExclusionFlag(6, SpellExclusionFlagEnum.Abjurers, "Abjurers"),
			new SpellExclusionFlag(7, SpellExclusionFlagEnum.Conjurers, "Conjurers"), new SpellExclusionFlag(8, SpellExclusionFlagEnum.Diviners, "Diviners"),
			new SpellExclusionFlag(9, SpellExclusionFlagEnum.Enchanters, "Enchanters"), new SpellExclusionFlag(10, SpellExclusionFlagEnum.Illusionists,
					"Illusionists"), new SpellExclusionFlag(11, SpellExclusionFlagEnum.Invokers, "Invokers"), new SpellExclusionFlag(12,
					SpellExclusionFlagEnum.Necromancers, "Necromancers"), new SpellExclusionFlag(13, SpellExclusionFlagEnum.Transmuters, "Transmuters"),
			new SpellExclusionFlag(14, SpellExclusionFlagEnum.Generalists, "Wild Magic (excludes Generalists)"), new SpellExclusionFlag(30,
					SpellExclusionFlagEnum.ClericPaladin, "Cleric/Paladin"), new SpellExclusionFlag(31, SpellExclusionFlagEnum.DruidRanger, "Druid/Ranger"));

}
