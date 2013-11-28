package old;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum ConditionEnum {
	OR, HPLT, HPGT, Level, LevelLT, LevelGT, CheckStat, CheckStatLT, CheckStatGT, Class, General, Race, Range, See, RandomNumGT, HitBy, AttackedBy, ActionListEmpty, StateCheck, Allegiance, GlobalTimerNotExpired, HPPercentLT, HPPercentGT, Detect, SpellCastOnMe, AreaType, Gender, Die, ModalState, HasItem, HasItemEquiped, HasBounceEffects, HasImmunityEffects, HaveSpell, HaveSpellRES, Kit, HasItemSlot, Global, GlobalTimerExpired, Heard, Alignment, TargetUnreachable, HaveAnySpells, HPLostLT, HPLostGT, HPLost, MovementRate, MovementRateLT, MovementRateGT;

	public static ConditionEnum fromString(String text) throws GeneratorException {
		if (text != null) {
			for (ConditionEnum v : ConditionEnum.values()) {
				if (text.equalsIgnoreCase(v.toString())) {
					return v;
				}
			}
		}
		throw new GeneratorException("Unknown condition: " + text);
	}

}
