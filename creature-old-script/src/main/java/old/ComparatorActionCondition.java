package old;
import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class ComparatorActionCondition implements Comparator {

	public int compare(Object arg0, Object arg1) {
		Condition c1 = (Condition) arg0;
		Condition c2 = (Condition) arg1;
		if (!c1.isCanBeSorted() || !c2.isCanBeSorted())
			return 0;
		if ((c1.getCode() == ConditionEnum.HaveSpell || c1.getCode() == ConditionEnum.HaveSpellRES) && c1.isResult())
			return -1;
		else if ((c2.getCode() == ConditionEnum.HaveSpell || c2.getCode() == ConditionEnum.HaveSpellRES) && c2.isResult())
			return 1;
		else if ((c1.getCode() == ConditionEnum.HasItem || c1.getCode() == ConditionEnum.HasItemEquiped) && c1.isResult())
			return -1;
		else if ((c2.getCode() == ConditionEnum.HasItem || c2.getCode() == ConditionEnum.HasItemEquiped) && c2.isResult())
			return 1;
		else if ((c1.getCode() == ConditionEnum.Global || c1.getCode() == ConditionEnum.Global) && c1.isResult())
			return -1;
		else if ((c2.getCode() == ConditionEnum.Global || c2.getCode() == ConditionEnum.Global) && c2.isResult())
			return 1;
		else if (c1.getCode() == ConditionEnum.RandomNumGT)
			return 1;
		else if (c2.getCode() == ConditionEnum.RandomNumGT)
			return -1;
		return 0;
	}

}
