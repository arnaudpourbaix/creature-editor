package com.pourbaix.creature.script.context;

import com.pourbaix.creature.script.generator.GeneratorException;

public enum SectionEnum {
	BEFOREINIT,
	AFTERINIT,
	BEFORETRACKING,
	TRACKING,
	AFTERTRACKING,
	BEFORECOMBAT,
	COMBAT,
	AFTERCOMBAT;
	
	public static SectionEnum fromString(String text) throws GeneratorException {
	    if (text != null) {
	    	for (SectionEnum v : SectionEnum.values()) {
		        if (text.trim().equalsIgnoreCase(v.name())) {
		        	return v;
		        }
	    	}
	    }
	    throw new GeneratorException("Unknown section: " + text);
	}
	    
}
