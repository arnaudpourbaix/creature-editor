package com.pourbaix.script.creature.action;

import java.util.HashMap;
import java.util.Map;

import com.pourbaix.script.creature.target.OutputTargetEnum;

public class BasicAction implements Action {

	private ActionEnum name;
	private final Map<String, String> params = new HashMap<String, String>();
	private OutputTargetEnum target;

	public BasicAction(final ActionEnum name) {
		this.name = name;
	}

	@Override
	public ActionEnum getName() {
		return name;
	}

	@Override
	public void setName(final ActionEnum name) {
		this.name = name;
	}

	@Override
	public Map<String, String> getParams() {
		return params;
	}

	public OutputTargetEnum getTarget() {
		return target;
	}

	public void setTarget(final OutputTargetEnum target) {
		this.target = target;
	}

}
