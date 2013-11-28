package old;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum SchoolEnum {
	Unknown,
	Abjurer,
	Conjurer,
	Diviner,
	Enchanter,
	Illusionist,
	Invoker,
	Necromancer,
	Transmuter,
	Generalist
	;
	
	public static SchoolEnum fromString(String text) throws GeneratorException {
	    if (text != null) {
	    	for (SchoolEnum v : SchoolEnum.values()) {
		        if (text.equalsIgnoreCase(v.name())) {
		        	return v;
		        }
	    	}
	    }
	    throw new GeneratorException("Unknown school: " + text);
	}
	    
}
