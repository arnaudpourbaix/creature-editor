package old;

import com.pourbaix.script.creature.generator.GeneratorException;

public class Condition extends Entity implements Cloneable  {
	private ConditionEnum code;
	private String[] values;
	private boolean result;
	private boolean targetMyself;
	private int affect = Constant.Affect.ALL;
	private boolean canBeSorted = true;

	public Condition() {
		
	}
	
	public Condition(ConditionEnum condition, String ... params) throws GeneratorException {
		this(true, condition, true, params);
	}

	public Condition(ConditionEnum condition, boolean result, String ... params) throws GeneratorException {
		this(true, condition, result, Constant.Affect.ALL, params);
	}
	
	public Condition(boolean canBeSorted, ConditionEnum condition, String ... params) throws GeneratorException {
		this(canBeSorted, condition, true, params);
	}

	public Condition(boolean canBeSorted, ConditionEnum condition, boolean result, String ... params) throws GeneratorException {
		this(canBeSorted, condition, result, Constant.Affect.ALL, params);
	}

	public Condition(ConditionEnum condition, boolean result, int affect, String ... params) throws GeneratorException {
		this(true, condition, result, affect, params);
	}
	
	public Condition(boolean canBeSorted, ConditionEnum condition, boolean result, int affect, String ... params) throws GeneratorException {
		this.canBeSorted = canBeSorted;
		if (condition == ConditionEnum.OR) {
			setCondition(condition, result, affect, params[0]);
		} else if (condition == ConditionEnum.GlobalTimerNotExpired || condition == ConditionEnum.GlobalTimerExpired) {
			setCondition(condition, result, affect, Tools.encloseString(params[0]), Tools.encloseString("LOCALS"));
		} else if (condition == ConditionEnum.Global) {
			setCondition(condition, result, affect, Tools.encloseString(params[0]), Tools.encloseString("LOCALS"), params[1]);
		} else  {
			if (params.length == 3)
				setCondition(condition, result, affect, params[0], params[1], params[2]);
			else if (params.length == 2)
				setCondition(condition, result, affect, params[0], params[1]);
			else if (params.length == 1)
				setCondition(condition, result, affect, params[0]);
			else if (params.length == 0)
				setCondition(condition, result, affect);
		}
		this.targetMyself = false;
		for (String param : params) {
			if (TargetEnum.MYSELF.getCode().equalsIgnoreCase(param)) {
				this.targetMyself = true;
				break;
			}
		}
	}
	
	public void setCondition(ConditionEnum condition, boolean result, int affect) {
		setCondition(condition, result, affect, null, null, null);
	}
	
	public void setCondition(ConditionEnum condition, boolean result, int affect, String param1) {
		setCondition(condition, result, affect, param1, null, null);
	}

	public void setCondition(ConditionEnum condition, boolean result, int affect, String param1, String param2) {
		setCondition(condition, result, affect, param1, param2, null);
	}
	
	public void setCondition(ConditionEnum condition, boolean result, int affect, String param1, String param2, String param3) {
		this.code = condition;
		this.result = result;
		this.affect = affect;
		name = result ? "" : "!";
		if (param3 != null) {
			name += String.format("%s(%s,%s,%s)", code.toString(), param1, param2, param3);
			values = new String[3];
			values[0] = param1;
			values[1] = param2;
			values[2] = param3;
			
		} else if (param2 != null) {
			name += String.format("%s(%s,%s)", code.toString(), param1, param2);
			values = new String[2];
			values[0] = param1;
			values[1] = param2;
		} else if (param1 != null) {
			name += String.format("%s(%s)", code.toString(), param1);
			values = new String[1];
			values[0] = param1;
		} else {
			name += String.format("%s()", code.toString());
		}
	}
	
	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getAffect() {
		return affect;
	}

	public void setAffect(int affect) {
		this.affect = affect;
	}

	public ConditionEnum getCode() {
		return code;
	}

	public void setCode(ConditionEnum code) {
		this.code = code;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public boolean isTargetMyself() {
		return targetMyself;
	}

	public void setTargetMyself(boolean targetMyself) {
		this.targetMyself = targetMyself;
	}

	public boolean isCanBeSorted() {
		return canBeSorted;
	}

	public void setCanBeSorted(boolean canBeSorted) {
		this.canBeSorted = canBeSorted;
	}
	
	public Condition clone() {
		Condition condition = new Condition();
		condition.code = this.code;
		condition.result = this.result;
		condition.values = this.values;
		condition.targetMyself = this.targetMyself;
		condition.affect = this.affect;
		condition.canBeSorted = this.canBeSorted;
		condition.name = this.name;
		return condition;
	}
}
