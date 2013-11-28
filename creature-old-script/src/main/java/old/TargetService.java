package old;
import java.util.List;

import com.pourbaix.script.creature.generator.GeneratorException;



public class TargetService {

	public static void addTargets(Context context, List<Target> list, Entity entity) throws GeneratorException {
		if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.CASTER.getShortcut())) {
//			for (int i = Constant.CASTERS.length - 1; i >= 0; i--) {
//				list.add(getTarget(context, entity, TargetEnum.NEARESTENEMYOFTYPE.getCode(), context.getDefaultTargetTypeCount(), context.getScriptContext().getTargetAllegiance(), "0", "0", Constant.CASTERS[i], "0", "0"));
//			}
			int count = context.getDefaultTargetTypeCount() * Constant.CASTERS.length;
			Target target = getTarget(context, entity, TargetEnum.NEARESTENEMYOFTYPE.getCode(), count, context.getScriptContext().getTargetAllegiance(), "0", "0", "<CLASS>", "0", "0");
			String[] array = new String[count];
			int i = 0;
			for(String s : Constant.CASTERS) {
				for (int j = 0; j < context.getDefaultTargetTypeCount(); j++) {
					array[i++] = Constant.NEARESTOFTYPE[j].replace("<TYPE>", target.getType().replace("<CLASS>", s));
				}
			}
			target.setClasses(array);
			list.add(target);
		} else if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.FIGHTER.getShortcut())) {
//			for (int i = Constant.FIGHTERS.length - 1; i >= 0; i--) {
//				list.add(getTarget(context, entity, TargetEnum.NEARESTENEMYOFTYPE.getCode(), context.getDefaultTargetTypeCount(), context.getScriptContext().getTargetAllegiance(), "0", "0", Constant.FIGHTERS[i], "0", "0"));
//			}
			int count = context.getDefaultTargetTypeCount() * Constant.FIGHTERS.length;
			Target target = getTarget(context, entity, TargetEnum.NEARESTENEMYOFTYPE.getCode(), count, context.getScriptContext().getTargetAllegiance(), "0", "0", "<CLASS>", "0", "0");
			String[] array = new String[count];
			int i = 0;
			for(String s : Constant.FIGHTERS) {
				for (int j = 0; j < context.getDefaultTargetTypeCount(); j++) {
					array[i++] = Constant.NEARESTOFTYPE[j].replace("<TYPE>", target.getType().replace("<CLASS>", s));
				}
			}
			target.setClasses(array);
			list.add(target);
		} else if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.MAGE.getShortcut())) {
			list.add(getTarget(context, entity, TargetEnum.NEARESTENEMYOFTYPE.getCode(), context.getScriptContext().getTargetAllegiance(), "0", "0", Constant.Class.MAGE_ALL, "0", "0"));
		} else if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.SUMMON.getShortcut())) {
			list.add(getTarget(context, entity, TargetEnum.NEARESTENEMYOFTYPE.getCode(), context.getScriptContext().getTargetAllegiance(), "0", "0", "0", "0", Constant.Gender.SUMMONED));
		} else if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.UNDEAD.getShortcut())) {
			list.add(getTarget(context, entity, TargetEnum.NEARESTENEMYOFTYPE.getCode(), context.getScriptContext().getTargetAllegiance(), Constant.General.UNDEAD, "0", "0", "0", Constant.Gender.SUMMONED));
		} else if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.ELF.getShortcut())) {
			list.add(getTarget(context, entity, TargetEnum.NEARESTENEMYOFTYPE.getCode(), context.getScriptContext().getTargetAllegiance(), Constant.General.HUMANOID, Constant.Race.ELF, "0", "0", "0"));
		} else if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.ANIMAL.getShortcut())) {
			list.add(getTarget(context, entity, TargetEnum.NEARESTENEMYOFTYPE.getCode(), context.getScriptContext().getTargetAllegiance(), Constant.General.ANIMAL, "0", "0", "0", Constant.Gender.SUMMONED));
		} else if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.ALLY.getShortcut())) {
			String[] params = entity.getParams().get(0).getValue().split("[.]");
			String allegiance = params.length > 0 ? params[0] : "0";
			String general = params.length > 1 ? params[1] : "0";
			String race = params.length > 2 ? params[2] : "0";
			String classe = params.length > 3 ? params[3] : "0";
			String specific = params.length > 4 ? params[4] : "0";
			String gender = params.length > 5 ? params[5] : "0";
			list.add(getTarget(context, entity, TargetEnum.ALLY.getCode(), 0, allegiance, general, race, classe, specific, gender));
		} else if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.CRE.getShortcut())) {
			Target target = new Target(entity.getName(), entity.getParams().get(0).getValue(), 0, context.isDefaultTargetRandom(), TargetEnum.CRE.getCode());
			list.add(target);
		} else {
			list.add(getTarget(context, entity)); 
		}
	}

	public static Target getTarget(Context context, Entity entity) throws GeneratorException {
		Target target = new Target(entity.getName(), null, context.getDefaultTargetCount(), context.isDefaultTargetRandom(), entity.getName());
		if (Tools.equalsIgnoreCase(entity.getName(), TargetEnum.PLAYER.getShortcut())) {
			target.getConditions().add(new Condition(ConditionEnum.StateCheck, false, TargetEnum.ENEMY.getCode(), StateEnum.STATE_CHARMED.toString()));
			target.getConditions().add(new Condition(ConditionEnum.StateCheck, false, TargetEnum.ENEMY.getCode(), StateEnum.STATE_REALLY_DEAD.toString()));
			if (target.getCount() > 6)
				target.setCount(6);
		}
		processTargetParams(context, entity, target);
		return target;
	}

	public static Target getTarget(Context context, String name, int count, String allegiance, String general, String race, String classe, String specific, String gender) throws GeneratorException {
		String type = String.format("[%s.%s.%s.%s.%s.%s]", allegiance, general, race, classe, specific, gender);
		Target target = new Target("", type, count, context.isDefaultTargetRandom(), name);
		return target;
	}

	public static Target getTarget(Context context, Entity entity, String name, String allegiance, String general, String race, String classe, String specific, String gender) throws GeneratorException {
		return getTarget(context, entity, name, context.getDefaultTargetCount(), allegiance, general, race, classe, specific, gender);
	}
	
	public static Target getTarget(Context context, Entity entity, String name, int count, String allegiance, String general, String race, String classe, String specific, String gender) throws GeneratorException {
		String type = String.format("[%s.%s.%s.%s.%s.%s]", allegiance, general, race, classe, specific, gender);
		Target target = new Target(entity.getName(), type, count, context.isDefaultTargetRandom(), name);
		processTargetParams(context, entity, target);
		return target;
	}
	
	public static void processTargetParams(Context context, Entity entity, Target target) throws GeneratorException {
		for(Param param : entity.getParams()) {
			if (Constant.Condition.RANDOM.equals(param.getName())) {
				target.setRandom(Boolean.parseBoolean(param.getValue()));
			} else if (Constant.Condition.NUM.equals(param.getName())) {
				target.setCount(Integer.valueOf(param.getValue()));
			} else {
				//throw new GeneratorException("Unknow target parameter: " + param.getName() + "," + param.getValue());
			}
		}
	}
	
}
