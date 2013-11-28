package com.pourbaix.script.creature.trigger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pourbaix.script.creature.target.OutputTargetEnum;

public class Trigger {

	private ShortcutEnum shortcut;
	private TriggerEnum type;
	private final Map<String, String> params = new HashMap<String, String>();
	private OutputTargetEnum target;
	private boolean result = true;

	private final List<Trigger> triggers = new LinkedList<Trigger>();

	public Trigger() {

	}

	public Trigger(final TriggerEnum type, final OutputTargetEnum target, final boolean result) {
		this.type = type;
		this.target = target;
		this.result = result;
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}

	public void addTriggers(final List<Trigger> triggers) {
		this.triggers.addAll(triggers);
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(final boolean result) {
		this.result = result;
	}

	public TriggerEnum getType() {
		return type;
	}

	public void setType(final TriggerEnum type) {
		this.type = type;
	}

	public OutputTargetEnum getTarget() {
		return target;
	}

	public void setTarget(final OutputTargetEnum target) {
		this.target = target;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public ShortcutEnum getShortcut() {
		return shortcut;
	}

	public void setShortcut(final ShortcutEnum shortcut) {
		this.shortcut = shortcut;
	}

}
