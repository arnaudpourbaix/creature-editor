package com.pourbaix.script.creature.trigger;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum StateEnum {
	STATE_HARMLESS(""), STATE_CONFUSED("CONFUSED"), STATE_FEEBLEMINDED("CONFUSED"), STATE_PANIC(""), STATE_NOT_VISIBLE(""), STATE_SILENCED(""), STATE_CHARMED(
			""), STATE_POISONED(""), STATE_DRAWUPONHOLYMIGHT(""), STATE_HASTED(""), STATE_ENCHANTED(""), STATE_ILLUSIONS(""), STATE_BLUR(""), STATE_BLESS(""), STATE_MIRRORIMAGE(
			""), STATE_STUNNED(""), STATE_INVISIBLE(""), STATE_SLEEPING(""), STATE_SLOWED(""), STATE_BLIND(""), STATE_IMPROVEDINVISIBILITY(""), STATE_DISABLED(
			""), STATE_REALLY_DEAD(""), STATE_OUT_OF_ACTION("");

	private String shortcut;

	private StateEnum(final String shortcut) {
		this.shortcut = shortcut;
	}

	public String getShortcut() {
		return shortcut;
	}

	public static StateEnum fromString(final String text) throws GeneratorException {
		if (text != null) {
			for (StateEnum value : StateEnum.values()) {
				if (text.trim().equalsIgnoreCase(value.toString()) || text.trim().equalsIgnoreCase(value.getShortcut())) {
					return value;
				}
			}
		}
		throw new GeneratorException("Unknown state: " + text);
	}

}
