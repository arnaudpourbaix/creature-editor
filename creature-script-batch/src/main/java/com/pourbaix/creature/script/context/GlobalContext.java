package com.pourbaix.creature.script.context;

import javax.annotation.Resource;

public class GlobalContext {

	private boolean loaded = false;
	@Resource
	private TargetContext target;
	@Resource
	private ProbablityContext probability;
	@Resource
	private CheckContext checkContext;
	private boolean tobEx;

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public TargetContext getTarget() {
		return target;
	}

	public void setTarget(TargetContext target) {
		this.target = target;
	}

	public ProbablityContext getProbability() {
		return probability;
	}

	public void setProbability(ProbablityContext probability) {
		this.probability = probability;
	}

	public CheckContext getCheckContext() {
		return checkContext;
	}

	public void setCheckContext(CheckContext checkContext) {
		this.checkContext = checkContext;
	}

	public boolean isTobEx() {
		return tobEx;
	}

	public void setTobEx(boolean tobEx) {
		this.tobEx = tobEx;
	}
	
}
