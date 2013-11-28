package old;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pourbaix.script.creature.generator.GeneratorException;

public class ConditionService {

	@SuppressWarnings("unchecked")
	public static List<Condition> getOthersConditions(final List<Condition> conditions) {
		List<Condition> lst = new ArrayList<Condition>();
		for (Condition c : conditions) {
			if (!c.getName().contains(TargetEnum.ENEMY.getCode())) {
				lst.add(c);
			}
		}
		if (ConditionService.isListCanBeSorted(lst)) {
			Collections.sort(lst, new ComparatorCondition());
		}
		return lst;
	}

	@SuppressWarnings("unchecked")
	public static List<Condition> getEnemyConditions(final List<Condition> conditions) {
		List<Condition> lst = new ArrayList<Condition>();
		for (Condition c : conditions) {
			if (c.getName().contains(TargetEnum.ENEMY.getCode())) {
				lst.add(c);
			}
		}
		if (ConditionService.isListCanBeSorted(lst)) {
			Collections.sort(lst, new ComparatorCondition());
		}
		return lst;
	}

	public static boolean isListCanBeSorted(final List<Condition> conditions) {
		boolean sort = true;
		for (Condition c : conditions) {
			if (!c.isCanBeSorted()) {
				sort = false;
				break;
			}
		}
		return sort;
	}

	public static List<Condition> mergeList(final List<Condition> lst1, final List<Condition> lst2) {
		return mergeList(lst1, lst2, true);
	}

	@SuppressWarnings("unchecked")
	public static List<Condition> mergeList(final List<Condition> lst1, final List<Condition> lst2, final boolean sort) {
		List<Condition> lstCondition = new ArrayList<Condition>(lst1);
		lstCondition.addAll(lst2);
		if (sort) {
			if (ConditionService.isListCanBeSorted(lstCondition)) {
				Collections.sort(lstCondition, new ComparatorCondition());
			}
		}
		checkList(lstCondition);
		return lstCondition;
	}

	public static void checkList(final List<Condition> list) {
		for (int i = list.size() - 1; i >= 0; i--) {
			Condition c1 = list.get(i);
			for (int j = 0; j < list.size() - 1; j++) {
				Condition c2 = list.get(j);
				if (i != j) {
					if (c1.getName().equals(c2.getName())) {
						list.remove(i);
						break;
					} else if (c1.getCode() == ConditionEnum.StateCheck && c1.getName().contains(StateEnum.STATE_CHARMED.toString())
							&& c2.getName().contains(StateEnum.STATE_DISABLED.toString())) {
						list.remove(i);
						break;
					}
				}
			}
		}
	}

	public static void filterList(final List<Condition> list, final Target target) {
		for (int i = list.size() - 1; i >= 0; i--) {
			if (Tools.equalsIgnoreCase(target.getName(), "PLAYER") && list.get(i).getAffect() == Constant.Affect.SUMMON) {
				list.remove(i);
			}
			if (Tools.containsIgnoreCase(target.getType(), "SUMMONED") && list.get(i).getAffect() == Constant.Affect.PLAYER) {
				list.remove(i);
			}
			if (list.get(i).getCode() == ConditionEnum.RandomNumGT) {
				list.remove(i);
			}
		}
	}

	public static void addEmptyAction(final List<Condition> list) throws GeneratorException {
		boolean found = false;
		for (Condition c : list) {
			if (c.getCode() == ConditionEnum.ActionListEmpty) {
				found = true;
			}
		}
		if (!found) {
			list.add(new Condition(ConditionEnum.ActionListEmpty));
		}
	}

	public static Condition getAllegiance(final Context context, final boolean result) throws GeneratorException {
		return getAllegiance(context, result, TargetEnum.MYSELF.getCode());
	}

	public static Condition getAllegiance(final Context context, final boolean result, final String target) throws GeneratorException {
		if (context.getScriptContext().getTargetAllegiance().equals(Constant.Allegiance.EVILCUTOFF)) {
			return new Condition(ConditionEnum.Allegiance, result, target, Constant.Allegiance.ALLY);
		} else {
			return new Condition(ConditionEnum.Allegiance, result, target, Constant.Allegiance.ENEMY);
		}
	}

	public static void addConditions(final Block block, final Context context, final List<Condition> list, final String conditions, final String target)
			throws GeneratorException {
		if (conditions == null || conditions.isEmpty()) {
			return;
		}
		Pattern p = Pattern.compile("([^,(]+([(][^)]*[)])?)");
		Matcher m = p.matcher(conditions);
		while (m.find()) {
			Entity entity = Tools.getEntity(m.group(1));
			boolean result = true;
			String name = entity.getName();
			if (name.startsWith("!")) {
				result = false;
				name = name.substring(1);
			}
			String param1 = entity.getParams().size() > 0 ? entity.getParams().get(0).getValue() : null;
			String param2 = entity.getParams().size() > 1 ? entity.getParams().get(1).getValue() : null;
			String param3 = entity.getParams().size() > 2 ? entity.getParams().get(2).getValue() : null;
			if (Tools.equalsIgnoreCase(name, "OR")) {
				list.addAll(getConditions(block, context, false, name, result, target, String.valueOf(entity.getParams().size())));
				for (Param param : entity.getParams()) {
					if (param.getName() == null) {
						list.addAll(getConditions(block, context, false, param.getValue(), true, target));
					} else {
						if (param.getValue().contains("/")) {
							param1 = param.getValue().split("/").length > 0 ? param.getValue().split("/")[0] : null;
							param2 = param.getValue().split("/").length > 1 ? param.getValue().split("/")[1] : null;
							param3 = param.getValue().split("/").length > 2 ? param.getValue().split("/")[2] : null;
							list.addAll(getConditions(block, context, false, param.getName(), true, target, param1, param2, param3));
						} else {
							list.addAll(getConditions(block, context, false, param.getName(), true, target, param.getValue()));
						}
					}
				}
			} else {
				list.addAll(getConditions(block, context, true, name, result, target, param1, param2, param3));
			}
		}
	}

	public static List<Condition> getConditions(final Block block, final Context context, final boolean canBeSorted, final String name, final boolean result,
			final String target) throws GeneratorException {
		return getConditions(block, context, canBeSorted, name, result, target, null, null, null);
	}

	public static List<Condition> getConditions(final Block block, final Context context, final boolean canBeSorted, final String name, final boolean result,
			final String target, final String param1) throws GeneratorException {
		return getConditions(block, context, canBeSorted, name, result, target, param1, null, null);
	}

	public static List<Condition> getConditions(final Block block, final Context context, final boolean canBeSorted, final String name, final boolean result,
			final String target, final String param1, final String param2) throws GeneratorException {
		return getConditions(block, context, canBeSorted, name, result, target, param1, param2, null);
	}

	public static List<Condition> getConditions(final Block block, final Context context, final boolean canBeSorted, String name, boolean result,
			String target, final String param1, final String param2, final String param3) throws GeneratorException {
		List<Condition> list = new ArrayList<Condition>();
		try {
			if (name.startsWith("!")) {
				result = false;
				name = name.substring(1);
			}
			if (name.equals("DISABLE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_DISABLED.toString()));
			} else if (name.equals("OUTOFACTION")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_OUT_OF_ACTION.toString()));
			} else if (name.equals("STUN")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_STUNNED.toString()));
			} else if (name.equals("PANIC")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_PANIC.toString()));
			} else if (name.equals("CONFUSED")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_CONFUSED.toString()));
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_FEEBLEMINDED.toString()));
			} else if (name.equals("CHARM")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_CHARMED.toString()));
			} else if (name.equals("SLOW")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_SLOWED.toString()));
			} else if (name.equals("HASTE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_HASTED.toString()));
			} else if (name.equals("BLIND")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_BLIND.toString()));
			} else if (name.equals("MIRROR")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_MIRRORIMAGE.toString()));
			} else if (name.equals("HELPLESS")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_HARMLESS.toString()));
			} else if (name.equals("POISON")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_POISONED.toString()));
			} else if (name.equals("SILENCE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_SILENCED.toString()));
			} else if (name.equals("SLEEP")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_SLEEPING.toString()));
			} else if (name.equals("IMPROVEDINVISIBLE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_IMPROVEDINVISIBILITY.toString()));
			} else if (name.equals("INVISIBLE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, StateEnum.STATE_INVISIBLE.toString()));

			} else if (name.equals("EMPTY")) {
				list.add(new Condition(canBeSorted, ConditionEnum.ActionListEmpty));
			} else if (name.equals("OUTDOOR")) {
				list.add(new Condition(canBeSorted, ConditionEnum.AreaType, result, "OUTDOOR"));
			} else if (name.equals("HOLD")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.HELD.toString()));
			} else if (name.equals("MOVEMENT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.MovementRate, result, target, param1));
			} else if (name.equals("MOVEMENTGT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.MovementRateGT, result, target, param1));
			} else if (name.equals("MOVEMENTLT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.MovementRateLT, result, target, param1));
			} else if (name.equals("FAILURE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "49", StatEnum.SPELLFAILUREMAGE.toString()));
			} else if (name.equals("HP50")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPPercentLT, result, target, "51"));
			} else if (name.equals("HP25")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPPercentLT, result, target, "26"));
			} else if (name.equals("HP10")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPPercentLT, result, target, "11"));
			} else if (name.equals("DETECTENEMY")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Detect, result, Tools.encloseString(context.getScriptContext().getTargetAllegiance(), '[')));
			} else if (name.equals("SEEENEMY")) {
				list.add(new Condition(canBeSorted, ConditionEnum.See, result, Tools.encloseString(context.getScriptContext().getTargetAllegiance(), '[')));
			} else if (name.equals("SEENEAREST")) {
				list.add(new Condition(canBeSorted, ConditionEnum.See, result, TargetEnum.NEARESTENEMY.getCode()));
			} else if (name.equals("MAGICALWEAPONITEM")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HasItemSlot, result, target, "SLOT_MISC19"));
			} else if (name.equals("VALID")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, !result, Constant.Affect.PLAYER, target, "0", StatEnum.SANCTUARY.toString()));
				if (!context.getScriptContext().isSeeInvisible()) {
					list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, !result, target, StateEnum.STATE_IMPROVEDINVISIBILITY.toString()));
				}
				list.add(new Condition(canBeSorted, ConditionEnum.See, result, target));
			} else if (name.equals("SANCTUARY")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.SANCTUARY.toString()));
			} else if (name.equals("SEE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.See, result, target));
			} else if (name.equals("ATTACKED")) {
				list.add(new Condition(canBeSorted, ConditionEnum.AttackedBy, result, String.format("[%s]", context.getScriptContext().getTargetAllegiance()),
						"DEFAULT"));
			} else if (name.equals("HIT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HitBy, result, "[ANYONE]", "CRUSHING"));
			} else if (name.equals("RANGEHIT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HitBy, result, "[ANYONE]", "PIERCING"));
			} else if (name.equals("WEAPONIMMUNITY")) {
				list.add(new Condition(canBeSorted, ConditionEnum.General, result, Constant.Affect.SUMMON, target, Constant.General.WEAPON)); // Wizard
																																				// eye
																																				// and
																																				// Mordenkainen
																																				// sword
			} else if (name.equals("WEAPONPROTECTION")) {
				if (context.isCheckWeaponProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.WIZARD_PROTECTION_FROM_NORMAL_WEAPONS
							.toString()));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.WIZARD_PROTECTION_FROM_MAGIC_WEAPONS
							.toString()));
				}
			} else if (name.equals("MAGICWEAPONPROTECTION")) {
				if (context.isCheckWeaponProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.WIZARD_PROTECTION_FROM_MAGIC_WEAPONS
							.toString()));
				}
			} else if (name.equals("SPELLPROTECTION")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					if (result) {
						list.add(new Condition(false, ConditionEnum.OR, "5"));
					}
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "1", StatEnum.CLERIC_SHIELD_OF_THE_ARCHONS.toString()));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "1", StatEnum.WIZARD_SPELL_TRAP.toString()));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.WIZARD_SPELL_TURNING.toString()));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.WIZARD_SPELL_DEFLECTION.toString()));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.GLOBE_OF_INVULNERABILITY.toString()));
				}
			} else if (name.equals("ENCHANTED")) {
				list.add(new Condition(false, ConditionEnum.OR, "13"));
				list.add(new Condition(false, ConditionEnum.StateCheck, result, target, StateEnum.STATE_DRAWUPONHOLYMIGHT.toString()));
				list.add(new Condition(false, ConditionEnum.StateCheck, result, target, StateEnum.STATE_HASTED.toString()));
				list.add(new Condition(false, ConditionEnum.StateCheck, result, target, StateEnum.STATE_ENCHANTED.toString()));
				list.add(new Condition(false, ConditionEnum.StateCheck, result, target, StateEnum.STATE_ILLUSIONS.toString()));
				list.add(new Condition(false, ConditionEnum.StateCheck, result, target, StateEnum.STATE_BLUR.toString()));
				list.add(new Condition(false, ConditionEnum.StateCheck, result, target, StateEnum.STATE_BLESS.toString()));
				list.add(new Condition(false, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.GLOBE_OF_INVULNERABILITY.toString()));
				list.add(new Condition(false, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.SHAPECHANGE.toString()));
				list.add(new Condition(false, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.POLYMORPH_SELF.toString()));
				list.add(new Condition(false, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.WIZARD_TENSERS_TRANSFORMATION.toString()));
				list.add(new Condition(false, ConditionEnum.CheckStatGT, result, target, "20", StatEnum.STR.toString()));
				list.add(new Condition(false, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.STONESKINS.toString()));
				list.add(new Condition(false, ConditionEnum.HasBounceEffects, result, target));
				// list.add(new Condition(false,
				// ConditionEnum.HasImmunityEffects, result, target));
			} else if (name.equals("ILLUSION")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Detect, true, target));
				list.add(new Condition(false, ConditionEnum.OR, "3"));
				list.add(new Condition(false, ConditionEnum.See, false, target));
				list.add(new Condition(false, ConditionEnum.StateCheck, true, target, StateEnum.STATE_MIRRORIMAGE.toString()));
				list.add(new Condition(false, ConditionEnum.StateCheck, true, target, StateEnum.STATE_IMPROVEDINVISIBILITY.toString()));
			} else if (name.equals("CANCAST")) {
				if (!result) {
					list.add(new Condition(false, ConditionEnum.OR, "6"));
				}
				list.add(new Condition(false, ConditionEnum.StateCheck, !result, target, StateEnum.STATE_SILENCED.toString()));
				list.add(new Condition(false, ConditionEnum.CheckStatGT, !result, target, "49", StatEnum.SPELLFAILUREMAGE.toString()));
				list.add(new Condition(false, ConditionEnum.CheckStat, !result, target, "1", StatEnum.WIZARD_TENSERS_TRANSFORMATION.toString()));
				list.add(new Condition(false, ConditionEnum.CheckStat, result, target, "0", StatEnum.SHAPESHIFT.toString()));
				list.add(new Condition(false, ConditionEnum.Global, result, Constant.Variable.SHAPESHIFT, "0"));
				list.add(new Condition(false, ConditionEnum.HaveAnySpells, result));
			} else if (name.equals("PLAYERRANGE")) {
				list.add(new Condition(false, ConditionEnum.OR, "6"));
				list.add(new Condition(false, ConditionEnum.Range, true, TargetEnum.PLAYER1.getCode(), param1));
				list.add(new Condition(false, ConditionEnum.Range, true, TargetEnum.PLAYER2.getCode(), param1));
				list.add(new Condition(false, ConditionEnum.Range, true, TargetEnum.PLAYER3.getCode(), param1));
				list.add(new Condition(false, ConditionEnum.Range, true, TargetEnum.PLAYER4.getCode(), param1));
				list.add(new Condition(false, ConditionEnum.Range, true, TargetEnum.PLAYER5.getCode(), param1));
				list.add(new Condition(false, ConditionEnum.Range, true, TargetEnum.PLAYER6.getCode(), param1));
			} else if (name.equals("AOE")) {
				String source = "[ANYONE]";
				list.add(new Condition(false, ConditionEnum.OR, "8"));
				list.add(new Condition(false, ConditionEnum.SpellCastOnMe, result, source, "TRAP_CLOUDKILL"));
				list.add(new Condition(false, ConditionEnum.SpellCastOnMe, result, source, "GOLEM_GASCLOUD"));
				list.add(new Condition(false, ConditionEnum.SpellCastOnMe, result, source, "MEPHIT_STINKING_CLOUD"));
				list.add(new Condition(false, ConditionEnum.SpellCastOnMe, result, source, "POISONOUS_CLOUD"));
				list.add(new Condition(false, ConditionEnum.SpellCastOnMe, result, source, "WIZARD_STINKING_CLOUD"));
				list.add(new Condition(false, ConditionEnum.SpellCastOnMe, result, source, "WIZARD_INCENDIARY_CLOUD"));
				list.add(new Condition(false, ConditionEnum.SpellCastOnMe, result, source, "WIZARD_DEATH_FOG"));
				list.add(new Condition(false, ConditionEnum.SpellCastOnMe, result, source, "WIZARD_CLOUDKILL"));
			} else if (name.equals("POISONIMMUNITY")) {
				if (context.isCheckPoisonImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "99", StatEnum.RESISTPOISON.toString()));
				}
			} else if (name.equals("SLEEPIMMUNITY")) {
				if (context.isCheckSleepImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.General, result, Constant.Affect.SUMMON, target, Constant.General.UNDEAD));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.SUMMON, target, Constant.Race.SKELETON));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.SUMMON, target, Constant.Race.ELEMENTAL));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.GOLEM));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.SLIME));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.PLAYER, target, Constant.Race.ELF));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, Constant.Affect.PLAYER, target, "1", StatEnum.SCRIPTINGSTATE4
							.toString()));
				}
			} else if (name.equals("HOLDIMMUNITY")) {
				if (context.isCheckHoldImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.General, result, Constant.Affect.SUMMON, target, Constant.General.UNDEAD));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.SUMMON, target, Constant.Race.SKELETON));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.SUMMON, target, Constant.Race.ELEMENTAL));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.GOLEM));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.SLIME));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, Constant.Affect.PLAYER, target, "1", StatEnum.SCRIPTINGSTATE4
							.toString()));
				}
			} else if (name.equals("CHARMIMMUNITY")) {
				if (context.isCheckCharmImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.Kit, result, Constant.Affect.PLAYER, target, Constant.Kit.INQUISITOR));
					list.add(new Condition(canBeSorted, ConditionEnum.Kit, result, Constant.Affect.PLAYER, target, Constant.Kit.CAVALIER));
					list.add(new Condition(canBeSorted, ConditionEnum.Class, result, Constant.Affect.PLAYER, target, Constant.Class.MONK));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.PLAYER, target, Constant.Race.ELF));

					list.add(new Condition(canBeSorted, ConditionEnum.General, result, Constant.Affect.SUMMON, target, Constant.General.UNDEAD));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.SUMMON, target, Constant.Race.SKELETON));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.GOLEM));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.SLIME));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, Constant.Affect.PLAYER, target, "1", StatEnum.SCRIPTINGSTATE4
							.toString()));
				}
			} else if (name.equals("STUNIMMUNITY")) {
				if (context.isCheckStunImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.General, result, Constant.Affect.SUMMON, target, Constant.General.UNDEAD));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.SUMMON, target, Constant.Race.SKELETON));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.SUMMON, target, Constant.Race.ELEMENTAL));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.GOLEM));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, Constant.Affect.PLAYER, target, "1", StatEnum.SCRIPTINGSTATE4
							.toString()));
				}
			} else if (name.equals("DEATHIMMUNITY")) {
				if (context.isCheckDeathImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.General, result, target, Constant.General.UNDEAD));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, target, Constant.Race.SKELETON));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, target, Constant.Race.GOLEM)));
				}
			} else if (name.equals("FEARIMMUNITY")) {
				if (context.isCheckFearImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.General, result, Constant.Affect.SUMMON, target, Constant.General.UNDEAD));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.SUMMON, target, Constant.Race.SKELETON));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.GOLEM)));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.SLIME)));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, Constant.Affect.PLAYER, target, "1", StatEnum.SCRIPTINGSTATE4
							.toString()));
					list.add(new Condition(canBeSorted, ConditionEnum.Kit, result, Constant.Affect.PLAYER, target, Constant.Kit.CAVALIER));
				}
			} else if (name.equals("CONFUSIONIMMUNITY")) {
				if (context.isCheckConfusionImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.General, result, Constant.Affect.SUMMON, target, Constant.General.UNDEAD));
					list.add(new Condition(canBeSorted, ConditionEnum.Race, result, Constant.Affect.SUMMON, target, Constant.Race.SKELETON));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.GOLEM));
					// list.add(new Condition(canBeSorted, ConditionEnum.Race,
					// result, Constant.Affect.SUMMON, target,
					// Constant.Race.SLIME));
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, Constant.Affect.PLAYER, target, "1", StatEnum.SCRIPTINGSTATE4
							.toString()));
				}
			} else if (name.equals("FIREIMMUNITY")) {
				if (context.isCheckElementalImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "99", StatEnum.RESISTFIRE.toString()));
					// list.add(new Condition(canBeSorted,
					// ConditionEnum.CheckStatGT, result, target, "99",
					// StatEnum.RESISTMAGICFIRE.toString()));
				}
			} else if (name.equals("COLDIMMUNITY")) {
				if (context.isCheckElementalImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "99", StatEnum.RESISTCOLD.toString()));
					// list.add(new Condition(canBeSorted,
					// ConditionEnum.CheckStatGT, result, target, "99",
					// StatEnum.RESISTMAGICCOLD.toString()));
				}
			} else if (name.equals("ELECTRICITYIMMUNITY")) {
				if (context.isCheckElementalImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "99", StatEnum.RESISTELECTRICITY.toString()));
				}
			} else if (name.equals("ACIDIMMUNITY")) {
				if (context.isCheckElementalImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "99", StatEnum.RESISTACID.toString()));
				}
			} else if (name.equals("MAGICDAMAGEIMMUNITY")) {
				if (context.isCheckElementalImmunity() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "99", StatEnum.MAGICDAMAGERESISTANCE.toString()));
				}
			} else if (name.equals("MAGICIMMUNITY")) {
				if (context.isCheckMagicResistance() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "99", StatEnum.RESISTMAGIC.toString()));
				}
			} else if (name.equals("INVOCATIONIMMUNITY")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "6", StatEnum.WIZARD_SPELL_IMMUNITY.toString()));
				}
			} else if (name.equals("CONJURATIONIMMUNITY")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "2", StatEnum.WIZARD_SPELL_IMMUNITY.toString()));
				}
			} else if (name.equals("DIVINATIONIMMUNITY")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "3", StatEnum.WIZARD_SPELL_IMMUNITY.toString()));
				}
			} else if (name.equals("ENCHANTMENTIMMUNITY")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "4", StatEnum.WIZARD_SPELL_IMMUNITY.toString()));
				}
			} else if (name.equals("NECROMANCYIMMUNITY")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "7", StatEnum.WIZARD_SPELL_IMMUNITY.toString()));
				}
			} else if (name.equals("ALTERATIONIMMUNITY")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "8", StatEnum.WIZARD_SPELL_IMMUNITY.toString()));
				}
			} else if (name.equals("VISIBLE")) {
				if (!target.equals(TargetEnum.MYSELF.getCode()) || (target.equals(TargetEnum.MYSELF.getCode()) && context.getScriptContext().isPriest())) {
					if (!result) {
						list.add(new Condition(false, ConditionEnum.OR, "2"));
					}
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, !result, Constant.Affect.PLAYER, target, "0", StatEnum.SANCTUARY.toString()));
				}
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, !result, target, StateEnum.STATE_NOT_VISIBLE.toString()));
			} else if (name.equals("ELF")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Race, result, target, Constant.Race.ELF));
			} else if (name.equals("MONK")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Class, result, target, Constant.Class.MONK));
			} else if (name.equals("CASTER")) {
				list.add(new Condition(false, ConditionEnum.OR, "4"));
				list.add(new Condition(false, ConditionEnum.Class, result, target, Constant.Class.MAGE_ALL));
				list.add(new Condition(false, ConditionEnum.Class, result, target, Constant.Class.CLERIC_ALL));
				list.add(new Condition(false, ConditionEnum.Class, result, target, Constant.Class.DRUID_ALL));
				list.add(new Condition(false, ConditionEnum.Class, result, target, Constant.Class.BARD_ALL));
			} else if (name.equals("FIGHTER")) {
				list.add(new Condition(false, ConditionEnum.OR, "3"));
				list.add(new Condition(false, ConditionEnum.Class, result, target, Constant.Class.FIGHTER_ALL));
				list.add(new Condition(false, ConditionEnum.Class, result, target, Constant.Class.PALADIN_ALL));
				list.add(new Condition(false, ConditionEnum.Class, result, target, Constant.Class.RANGER_ALL));
			} else if (name.equals("INQUISITOR")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Kit, result, target, Constant.Kit.INQUISITOR));
			} else if (name.equals("ANIMAL")) {
				list.add(new Condition(canBeSorted, ConditionEnum.General, result, target, Constant.General.ANIMAL));
			} else if (name.equals("UNDEAD")) {
				list.add(new Condition(canBeSorted, ConditionEnum.General, result, target, Constant.General.UNDEAD));
			} else if (name.equals("SUMMON")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Gender, result, target, Constant.Gender.SUMMONED));
			} else if (name.equals("MINORGLOBE")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.GLOBE_OF_INVULNERABILITY.toString()));
				}
			} else if (name.equals("GLOBE")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "1", StatEnum.GLOBE_OF_INVULNERABILITY.toString()));
				}
			} else if (name.equals("MAJORGLOBE")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "2", StatEnum.GLOBE_OF_INVULNERABILITY.toString()));
				}
			} else if (name.equals("FREEACTION")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "1", StatEnum.CLERIC_FREE_ACTION.toString()));
			} else if (name.equals("SHIELD")) {
				if (context.isCheckSpellProtections() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "2", StatEnum.SCRIPTINGSTATE5.toString()));
				}
			} else if (name.equals("DOOM")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "1", StatEnum.WIZARD_GREATER_MALISON.toString()));
			} else if (name.equals("PFE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "1", StatEnum.PROTECTION_FROM_EVIL.toString()));
			} else if (name.equals("RAGE")) {
				if (context.isCheckRage() || target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, Constant.Affect.PLAYER, target, "1", StatEnum.SCRIPTINGSTATE4
							.toString()));
				}
			} else if (name.equals("KAI")) {
				if (target.equals(TargetEnum.MYSELF.getCode())) {
					list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, "2", StatEnum.SCRIPTINGSTATE4.toString()));
				}
			} else if (name.equals("ALIGN")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Alignment, result, target, param1));
			} else if (name.equals("STATE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.StateCheck, result, target, param1));
			} else if (name.equals("ALLEGIANCE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Allegiance, result, target, param1));
			} else if (name.equals("ITEM")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HasItem, result, Tools.encloseString(param1), target));
			} else if (name.equals("MIST")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HasItem, result, Tools.encloseString("ja#mist"), target));
			} else if (name.equals("STAT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStat, result, target, param1, param2));
			} else if (name.equals("STATLT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStatLT, result, target, param1, param2));
			} else if (name.equals("STATGT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, param1, param2));
			} else if (name.equals("HP%LT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPPercentLT, result, target, param1));
			} else if (name.equals("HP%GT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPPercentGT, result, target, param1));
			} else if (name.equals("HPLT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPLT, result, target, param1));
			} else if (name.equals("HPGT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPGT, result, target, param1));
			} else if (name.equals("HPLOSTLT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPLostLT, result, target, param1));
			} else if (name.equals("HPLOSTGT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPLostGT, result, target, param1));
			} else if (name.equals("HPLOST")) {
				list.add(new Condition(canBeSorted, ConditionEnum.HPLost, result, target, param1));
			} else if (name.equals("LEVELLT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.LevelLT, result, target, param1));
			} else if (name.equals("LEVELGT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.LevelGT, result, target, param1));
			} else if (name.equals("SPELL")) {
				Spell spell = Tools.getSpell(context, param1);
				list.add(spell.getHaveSpellCondition(result));
			} else if (name.equals("NORANDOM")) {
				block.setRandom(false);
				for (Action action : block.getActions()) {
					for (int i = action.getConditions().size() - 1; i >= 0; i--) {
						if (action.getConditions().get(i).getCode() == ConditionEnum.RandomNumGT) {
							action.getConditions().remove(i);
						}
					}
				}
			} else if (name.equals("NOMEM")) {
				block.setMemorized(false);
				for (Action action : block.getActions()) {
					for (int i = action.getConditions().size() - 1; i >= 0; i--) {
						if (action.getConditions().get(i).getCode() == ConditionEnum.HaveSpell
								|| action.getConditions().get(i).getCode() == ConditionEnum.HaveSpellRES) {
							action.getConditions().remove(i);
						}
					}
				}
				for (int i = block.getConditions().size() - 1; i >= 0; i--) {
					if (block.getConditions().get(i).getCode() == ConditionEnum.HaveSpell
							|| block.getConditions().get(i).getCode() == ConditionEnum.HaveSpellRES) {
						block.getConditions().remove(i);
					}
				}
			} else if (name.equals("NOROUND")) {
				block.setAffectedByRound(false);
			} else if (name.equals("NODISABLECHECK")) {
				block.setUseDisableSpellcasting(false);
				for (int i = block.getConditions().size() - 1; i >= 0; i--) {
					if (block.getConditions().get(i).getName().contains(Constant.Variable.DISABLE_SPELLCASTING)) {
						block.getConditions().remove(i);
					}
				}
				for (Action action : block.getActions()) {
					for (int i = action.getConditions().size() - 1; i >= 0; i--) {
						if (action.getConditions().get(i).getName().contains(Constant.Variable.DISABLE_SPELLCASTING)) {
							action.getConditions().remove(i);
						}
					}
				}
			} else if (name.equals("STONESKIN")) {
				list.add(new Condition(canBeSorted, ConditionEnum.CheckStatGT, result, target, "0", StatEnum.STONESKINS.toString()));
				// System.out.println(block.getInput() + " : " + name + " : " +
				// result);
			} else if (name.equals("RANDOM") || name.equals("RANDOM3")) {
				RandomNum rdm = new RandomNum(Integer.valueOf(param1), name.equals("RANDOM") ? 100 : 1000);
				for (int i = block.getAction().getConditions().size() - 1; i >= 0; i--) {
					if (block.getAction().getConditions().get(i).getCode() == ConditionEnum.RandomNumGT) {
						block.getAction().getConditions().remove(i);
					}
				}
				if (rdm.getNum() < rdm.getMax()) {
					list.add(new Condition(canBeSorted, ConditionEnum.RandomNumGT, result, rdm.getMax().toString(), rdm.getDiff().toString()));
				}
			} else if (name.equals("GLOBAL")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Global, result, param1, param2));
			} else if (name.equals("TIMER")) {
				list.add(new Condition(canBeSorted, ConditionEnum.GlobalTimerNotExpired, !result, param1));
				if (param2 != null) {
					block.getAction().addLine(ActionEnum.SetGlobalTimer, param1, param2);
				}
			} else if (name.equals("ROUND")) {
				list.add(new Condition(canBeSorted, ConditionEnum.GlobalTimerNotExpired, !result, Constant.Variable.ROUND));
				block.getAction().addLine(ActionEnum.SetGlobalTimer, Constant.Variable.ROUND, "6");
			} else if (name.equals("CLASS")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Class, result, target, param1));
			} else if (name.equals("RACE")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Race, result, target, param1));
			} else if (name.equals("GENERAL")) {
				list.add(new Condition(canBeSorted, ConditionEnum.General, result, target, param1));
			} else if (name.equals("KIT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Kit, result, target, param1));
			} else if (name.equals("DETECT")) {
				list.add(new Condition(canBeSorted, ConditionEnum.Detect, result, target));
			} else if (name.equals("RANGE")) {
				if (param2 != null && param2.equalsIgnoreCase(TargetEnum.NEARESTENEMY.getShortcut())) {
					target = TargetEnum.NEARESTENEMY.getCode();
				} else if (param2 != null && param2.equalsIgnoreCase(TargetEnum.LASTATTACKER.getShortcut())) {
					target = TargetEnum.LASTATTACKER.getCode();
				}
				list.add(new Condition(canBeSorted, ConditionEnum.Range, result, target, param1));
			} else {
				list.add(new Condition(canBeSorted, ConditionEnum.fromString(name), param1, param2, param3));
			}
		} catch (Exception exc) {
			System.out.println("condition " + name + ": " + exc.getMessage());
			throw new GeneratorException(exc);
		}
		return list;
	}

	public static boolean find(final List<Condition> lst, final String text) {
		for (Condition c : lst) {
			if (c.getName().toUpperCase().contains(text.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	public static void removeCondition(final ConditionEnum condition, final List<Condition> lst) {
		for (Iterator<Condition> iterator = lst.iterator(); iterator.hasNext();) {
			Condition c = iterator.next();
			if (c.getCode() == condition) {
				iterator.remove();
			}
		}
	}

}
