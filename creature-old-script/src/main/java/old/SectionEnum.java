package old;

import com.pourbaix.script.creature.generator.GeneratorException;

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
		        if (text.equalsIgnoreCase(v.name())) {
		        	return v;
		        }
	    	}
	    }
	    throw new GeneratorException("Unknown section: " + text);
	}
	    
}
