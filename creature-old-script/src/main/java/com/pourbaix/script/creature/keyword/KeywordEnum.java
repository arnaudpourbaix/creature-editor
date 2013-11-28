package com.pourbaix.script.creature.keyword;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum KeywordEnum {
	CLASS("CLASS", 0),
	CASTER_LEVEL("CASTER_LEVEL", 0),
	MAX_SPELL_LEVEL("MAX_SPELL_LEVEL", 0),
	INCLUDE("include", 0),
	CONTINGENCY("contingency", 0),
	MINOR_SEQUENCER("minorsequencer", 0),
	SPELL("spell", 0),
	OFFENSIVE_SPELLS("offensive_spells", 0),
	
	REQUIRE("require", 1),
	REQUIRE_SELF("requireSelf", 2),
	TARGET("target", 3),
	TARGET_REQUIRE("requireTarget", 4),
	ACTION("action", 5),
	ACTION_REQUIRE("requireAction", 6)
	;
	
	private String shortcut;
	private int priority;
	
	private KeywordEnum(String shortcut, int priority) {
		this.shortcut = shortcut;
		this.priority = priority;
	}
	
	public String getShortcut() {
		return this.shortcut;
	}

	public int getPriority() {
		return this.priority;
	}
	
	public static KeywordEnum fromString(String text) throws GeneratorException {
	    if (text != null) {
	    	for (KeywordEnum v : KeywordEnum.values()) {
		        if (text.trim().equalsIgnoreCase(v.shortcut)) {
		        	return v;
		        }
	    	}
	    }
	    throw new GeneratorException("Undefined KeywordEnum: " + text);
	}

}
