package old;
import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class ComparatorCondition implements Comparator {

	public int compare(Object arg0, Object arg1) {
		Condition c1 = (Condition) arg0;
		Condition c2 = (Condition) arg1;
		if (!c1.isCanBeSorted() || !c2.isCanBeSorted())
			return 0;
		if (c1.getName().startsWith("HaveSpell"))
			return -1;
		else if (c2.getName().startsWith("HaveSpell"))
			return 1;
		if (c1.getName().startsWith("HasItem"))
			return -1;
		else if (c2.getName().startsWith("HasItem"))
			return 1;
		else if (c1.getName().startsWith("Allegiance(Myself,"))
			return -1;
		else if ((c2.getName().startsWith("Allegiance(Myself,")))
			return 1;
		else if (c1.getCode() == ConditionEnum.RandomNumGT)
			return 1;
		else if (c2.getCode() == ConditionEnum.RandomNumGT)
			return -1;
		else if (c1.getCode() == ConditionEnum.See)
			return 1;
		else if (c2.getCode() == ConditionEnum.See)
			return -1;
		return 0;
	}

}
