package com.pourbaix.creature.editor.service;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.pourbaix.creature.editor.domain.SpellDefensiveFlag;
import com.pourbaix.creature.editor.domain.SpellDefensiveFlagEnum;
import com.pourbaix.creature.editor.domain.SpellExclusionFlag;
import com.pourbaix.creature.editor.domain.SpellExclusionFlagEnum;
import com.pourbaix.creature.editor.domain.SpellFlag;
import com.pourbaix.creature.editor.domain.SpellFlagEnum;
import com.pourbaix.creature.editor.domain.SpellOffensiveFlag;
import com.pourbaix.creature.editor.domain.SpellOffensiveFlagEnum;

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

	public static final ImmutableList<SpellOffensiveFlag> offensiveFlags = ImmutableList.of(new SpellOffensiveFlag(1, SpellOffensiveFlagEnum.Crushing,
			"Crushing"), new SpellOffensiveFlag(2, SpellOffensiveFlagEnum.Slashing, "Slashing"), new SpellOffensiveFlag(3, SpellOffensiveFlagEnum.Piercing,
			"Piercing"), new SpellOffensiveFlag(4, SpellOffensiveFlagEnum.Missile, "Missile"), new SpellOffensiveFlag(5, SpellOffensiveFlagEnum.Poison,
			"Poison"), new SpellOffensiveFlag(6, SpellOffensiveFlagEnum.Acid, "Acid"), new SpellOffensiveFlag(7, SpellOffensiveFlagEnum.Fire, "Fire"),
			new SpellOffensiveFlag(8, SpellOffensiveFlagEnum.Cold, "Cold"), new SpellOffensiveFlag(9, SpellOffensiveFlagEnum.Electricity, "Electricity"),
			new SpellOffensiveFlag(10, SpellOffensiveFlagEnum.Magic, "Magic"), new SpellOffensiveFlag(11, SpellOffensiveFlagEnum.MagicFire, "Magic Fire"),
			new SpellOffensiveFlag(12, SpellOffensiveFlagEnum.MagicCold, "Magic Cold"), new SpellOffensiveFlag(13, SpellOffensiveFlagEnum.CharmCreature,
					"Charm creature"), new SpellOffensiveFlag(14, SpellOffensiveFlagEnum.InstantDeath, "Instant death"), new SpellOffensiveFlag(15,
					SpellOffensiveFlagEnum.Slay, "Slay"), new SpellOffensiveFlag(16, SpellOffensiveFlagEnum.PowerWordKill, "Power-word kill"),
			new SpellOffensiveFlag(17, SpellOffensiveFlagEnum.Disintegrate, "Disintegrate"), new SpellOffensiveFlag(18, SpellOffensiveFlagEnum.Panic, "Panic"),
			new SpellOffensiveFlag(19, SpellOffensiveFlagEnum.Silence, "Silence"), new SpellOffensiveFlag(20, SpellOffensiveFlagEnum.Sleep, "Sleep"),
			new SpellOffensiveFlag(21, SpellOffensiveFlagEnum.PowerWordSleep, "Power-word sleep"), new SpellOffensiveFlag(22, SpellOffensiveFlagEnum.Slow,
					"Slow"), new SpellOffensiveFlag(23, SpellOffensiveFlagEnum.Stun, "Stun"), new SpellOffensiveFlag(24, SpellOffensiveFlagEnum.PowerWordStun,
					"Power-word stun"), new SpellOffensiveFlag(25, SpellOffensiveFlagEnum.Blindness, "Blindness"), new SpellOffensiveFlag(26,
					SpellOffensiveFlagEnum.CastingFailure, "Casting failure"), new SpellOffensiveFlag(27, SpellOffensiveFlagEnum.Deafness, "Deafness"),
			new SpellOffensiveFlag(28, SpellOffensiveFlagEnum.Confusion, "Confusion"), new SpellOffensiveFlag(29, SpellOffensiveFlagEnum.Feeblemindedness,
					"Feeblemindedness"), new SpellOffensiveFlag(30, SpellOffensiveFlagEnum.Berserk, "Berserk"), new SpellOffensiveFlag(31,
					SpellOffensiveFlagEnum.Petrification, "Petrification"), new SpellOffensiveFlag(32, SpellOffensiveFlagEnum.Polymorph, "Polymorph"),
			new SpellOffensiveFlag(33, SpellOffensiveFlagEnum.HoldCreature, "Hold creature"), new SpellOffensiveFlag(34, SpellOffensiveFlagEnum.HoldCreature2,
					"Hold creature 2"), new SpellOffensiveFlag(35, SpellOffensiveFlagEnum.Paralyze, "Paralyze"), new SpellOffensiveFlag(36,
					SpellOffensiveFlagEnum.Imprisonment, "Imprisonment"), new SpellOffensiveFlag(37, SpellOffensiveFlagEnum.Maze, "Maze"),
			new SpellOffensiveFlag(38, SpellOffensiveFlagEnum.LevelDrain, "Level drain"));

	public static final ImmutableList<SpellDefensiveFlag> defensiveFlags = ImmutableList.of(new SpellDefensiveFlag(1, SpellDefensiveFlagEnum.CureSleep,
			"Cure Sleep"), new SpellDefensiveFlag(1, SpellDefensiveFlagEnum.CurePoison, "Cure Poison"), new SpellDefensiveFlag(2,
			SpellDefensiveFlagEnum.CureConfusion, "Cure Confusion"), new SpellDefensiveFlag(3, SpellDefensiveFlagEnum.CureStun, "Cure Stun"),
			new SpellDefensiveFlag(4, SpellDefensiveFlagEnum.CureSilence, "Cure Silence"), new SpellDefensiveFlag(5, SpellDefensiveFlagEnum.CureBlindness,
					"Cure Blindness"), new SpellDefensiveFlag(6, SpellDefensiveFlagEnum.CureFeeblemindedness, "Cure Feeblemindedness"), new SpellDefensiveFlag(
					7, SpellDefensiveFlagEnum.CureDisease, "Cure Disease"), new SpellDefensiveFlag(8, SpellDefensiveFlagEnum.CureDeafness, "Cure Deafness"),
			new SpellDefensiveFlag(9, SpellDefensiveFlagEnum.Haste, "Haste"), new SpellDefensiveFlag(10, SpellDefensiveFlagEnum.HPModifier, "HP Modifier"),
			new SpellDefensiveFlag(11, SpellDefensiveFlagEnum.Invisibility, "Invisibility"), new SpellDefensiveFlag(12, SpellDefensiveFlagEnum.ResetMorale,
					"Reset Morale"), new SpellDefensiveFlag(13, SpellDefensiveFlagEnum.RemoveInvisibility, "Remove Invisibility"), new SpellDefensiveFlag(14,
					SpellDefensiveFlagEnum.DispelEffects, "Dispel Effects"), new SpellDefensiveFlag(15, SpellDefensiveFlagEnum.NonDetection, "Non Detection"),
			new SpellDefensiveFlag(16, SpellDefensiveFlagEnum.ProjectileImmunity, "Projectile Immunity"), new SpellDefensiveFlag(17,
					SpellDefensiveFlagEnum.Regeneration, "Regeneration"), new SpellDefensiveFlag(18, SpellDefensiveFlagEnum.SpellLevelImmunity,
					"Spell Level Immunity"), new SpellDefensiveFlag(19, SpellDefensiveFlagEnum.Clairvoyance, "Clairvoyance"), new SpellDefensiveFlag(20,
					SpellDefensiveFlagEnum.MirrorImage, "Mirror Image"),
			new SpellDefensiveFlag(21, SpellDefensiveFlagEnum.WeaponsImmunity, "Weapons Immunity"), new SpellDefensiveFlag(22, SpellDefensiveFlagEnum.Aid,
					"Aid"), new SpellDefensiveFlag(23, SpellDefensiveFlagEnum.Bless, "Bless"),
			new SpellDefensiveFlag(24, SpellDefensiveFlagEnum.Chant, "Chant"), new SpellDefensiveFlag(25, SpellDefensiveFlagEnum.DrawUponHolyMight,
					"Draw Upon Holy Might"), new SpellDefensiveFlag(26, SpellDefensiveFlagEnum.Sanctuary, "Sanctuary"), new SpellDefensiveFlag(27,
					SpellDefensiveFlagEnum.ProtectionFromNormalMissiles, "Protection From Normal Missiles"), new SpellDefensiveFlag(28,
					SpellDefensiveFlagEnum.FreeAction, "Free Action"), new SpellDefensiveFlag(29, SpellDefensiveFlagEnum.InvisibilityDetection,
					"Invisibility Detection"), new SpellDefensiveFlag(30, SpellDefensiveFlagEnum.SpellLevelReflection, "Spell Level Reflection"),
			new SpellDefensiveFlag(31, SpellDefensiveFlagEnum.SpellTurning, "Spell Turning"), new SpellDefensiveFlag(32,
					SpellDefensiveFlagEnum.SpellDeflection, "Spell Deflection"), new SpellDefensiveFlag(33, SpellDefensiveFlagEnum.Freedom, "Freedom"),
			new SpellDefensiveFlag(34, SpellDefensiveFlagEnum.StoneskinEffect, "Stoneskin Effect"), new SpellDefensiveFlag(35,
					SpellDefensiveFlagEnum.Restoration, "Restoration"), new SpellDefensiveFlag(36, SpellDefensiveFlagEnum.SweetAirZone, "Sweet Air Zone"),
			new SpellDefensiveFlag(37, SpellDefensiveFlagEnum.BackstabEveryHit, "Backstab Every Hit"), new SpellDefensiveFlag(38,
					SpellDefensiveFlagEnum.DetectInvisible, "Detect Invisible")

	);
}
