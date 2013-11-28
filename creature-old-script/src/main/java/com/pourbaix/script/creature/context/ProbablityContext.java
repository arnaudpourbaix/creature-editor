package com.pourbaix.script.creature.context;

import org.springframework.beans.factory.annotation.Value;

public class ProbablityContext {

	@Value("${probability.action.spell}")
	private int actionSpell;

	@Value("${probability.action.item}")
	private int actionItem;

	public int getActionSpell() {
		return actionSpell;
	}

	public void setActionSpell(int actionSpell) {
		this.actionSpell = actionSpell;
	}

	public int getActionItem() {
		return actionItem;
	}

	public void setActionItem(int actionItem) {
		this.actionItem = actionItem;
	}

}
