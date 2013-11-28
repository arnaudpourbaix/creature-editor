package com.pourbaix.script.creature.action;

import java.util.Map;

import com.pourbaix.script.creature.target.OutputTargetEnum;

public interface Action {

	public ActionEnum getName();

	public void setName(ActionEnum name);

	public Map<String, String> getParams();

	public OutputTargetEnum getTarget();

	public void setTarget(final OutputTargetEnum target);

}
