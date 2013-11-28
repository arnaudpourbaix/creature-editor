package com.pourbaix.script.creature.trigger;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum TriggerEnum {
	OR("", "I:OrCount"), False("", ""), Delay("", "I:Delay"), ActionListEmpty("empty", ""), ActuallyInCombat("", ""), NumTimesTalkedTo("", "I:Num"), NumTimesTalkedToGT(
			"", "I:Num"), NumTimesTalkedToLT("", "I:Num"), NumberOfTimesTalkedTo("", "I:Num"), AttackedBy("", "O:Object, I:StyleAStyles"), HitBy("",
			"O:Object, I:DameTypeDamages"), TookDamage("", ""), DamageTaken("", "I:Amount"), DamageTakenGT("", "I:Amount"), DamageTakenLT("", "I:Amount"), Help(
			"", "O:Object"), TurnedBy("", "O:Object"), Heard("", "O:Object, I:IDSHOUTIDS"), Alignment("", "O:Object, I:AlignmentAlign"), Allegiance("",
			"O:Object, I:AllegienceEA"), Class("", "O:Object, I:ClassClass"), Gender("", "O:Object, I:SexGender"), Kit("", "O:Object, I:KitKIT"), General("",
			"O:Object, I:GeneralGeneral"), Race("", "O:Object, I:RaceRace"), Specifics("", "O:Object, I:SpecificsSpecific"), Level("", "O:Object, I:Level"), LevelGT(
			"", "O:Object, I:Level"), LevelLT("", "O:Object, I:Level"), Summoned("", "O:Object"), ModalState("", "I:StateMODAL"), Global("",
			"S:Name, S:Area, I:Value"), GlobalGT("", "S:Name, S:Area, I:Value"), GlobalLT("", "S:Name, S:Area, I:Value"), GlobalTimerExpired("",
			"S:Name, S:Area"), GlobalTimerNotExpired("", "S:Name, S:Area"), GlobalsEqual("", "S:Name1, S:Name2"), GlobalsGT("", "S:Name1, S:Name2"), GlobalsLT(
			"", "S:Name1, S:Name2"), LocalsEqual("", "S:Name1, S:Name2"), LocalsGT("", "S:Name1, S:Name2"), LocalsLT("", "S:Name1, S:Name2"), HP("",
			"O:Object, I:Hit Points"), HPGT("", "O:Object, I:Hit Points"), HPLT("", "O:Object, I:Hit Points"), HPPercent("hp%", "O:Object, I:Hit Points"), HPPercentLT(
			"hp%lt", "O:Object, I:Hit Points"), HPPercentGT("hp%gt", "O:Object, I:Hit Points"), HPLost("", "O:Object, I:Hit Points"), HPLostLT("",
			"O:Object, I:Hit Points"), HPLostGT("", "O:Object, I:Hit Points"), LOS("", "O:Object, I:Range"), Range("", "O:Object, I:Range"), See("", "O:Object"), Detect(
			"", "O:Object"), Exists("", "O:Object"), TargetUnreachable("", "O:Object"), InWeaponRange("", "O:Object"), Die("", ""), Died("", "O:Object"), Killed(
			"", "O:Object"), Dead("", "S:Name"), BecameVisible("", ""), InParty("", "O:Object"), InPartyAllowDead("", "O:Object"), CheckStat("stat",
			"O:Object, I:Value, I:StatNumStats"), CheckStatGT("statgt", "O:Object, I:Value, I:StatNumStats"), CheckStatLT("statlt",
			"O:Object, I:Value, I:StatNumStats"), StateCheck("", "O:Object, I:StateState"), NotStateCheck("", "O:Object, I:StateState"), RandomNum("",
			"I:Range, I:Value"), RandomNumGT("", "I:Range, I:Value"), RandomNumLT("", "I:Range, I:Value"), HasItem("item", "S:ResRef, O:Object"), HasItemSlot(
			"", "O:Object, I:Slot"), HasItemEquiped("", "S:ResRef, O:Object"), NumCreature("", "O:Object, I:Number"), NumCreatureLT("", "O:Object, I:Number"), NumCreatureGT(
			"", "O:Object, I:Number"), NumInParty("", "I:Num"), NumInPartyGT("", "I:Num"), NumInPartyLT("", "I:Num"), NumCreatureVsParty("",
			"O:Object, I:Number"), NumCreatureVsPartyLT("", "O:Object, I:Number"), NumCreatureVsPartyGT("", "O:Object, I:Number"), NumInPartyAlive("", "I:Num"), NumInPartyAliveGT(
			"", "I:Num"), NumInPartyAliveLT("", "I:Num"), HasBounceEffects("", "O:Object"), HasImmunityEffects("", "O:Object"), InMyGroup("", "O:Object"), InMyArea(
			"", "O:Object"), AreaType("", "I:NumberAREATYPE"), HaveSpell("", "I:SpellSpell"), HaveSpellRES("", "S:Spell"), HaveAnySpells("", ""), SpellCastOnMe(
			"", "O:Caster, I:SpellSpell"), SpellCast("", "O:Object, I:SpellSpell"), MovementRate("movement", "O:Object, I:Value"), MovementRateGT("movementgt",
			"O:Object, I:Value"), MovementRateLT("movementlt", "O:Object, I:Value"), NumMirrorImages("", "O:Object, I:Value"), NumMirrorImagesGT("",
			"O:Object, I:Value"), NumMirrorImagesLT("", "O:Object, I:Value"), BouncingSpellLevel("", "O:Object, I:Level"), ImmuneToSpellLevel("",
			"O:Object, I:Level");

	private String shortcut;
	private String params;
	private int paramCount;

	private TriggerEnum(final String shortcut, final String params) {
		this.shortcut = shortcut;
		this.params = params;
		this.paramCount = params.isEmpty() ? 0 : params.split(",").length + 1;
	}

	public String getParams() {
		return params;
	}

	public String getShortcut() {
		return shortcut;
	}

	public int getParamCount() {
		return paramCount;
	}

	public static TriggerEnum fromString(final String text) throws GeneratorException {
		if (text != null) {
			for (TriggerEnum value : TriggerEnum.values()) {
				if (text.trim().equalsIgnoreCase(value.toString()) || text.trim().equalsIgnoreCase(value.getShortcut())) {
					return value;
				}
			}
		}
		throw new GeneratorException("Unknown trigger: " + text);
	}

}
