package com.pourbaix.infinity.resource.spl;

import java.io.IOException;

import com.pourbaix.infinity.entity.Spell;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.StringResourceException;
import com.pourbaix.infinity.resource.datatype.Flag;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.DynamicArray;

public abstract class SpellFactory {

	public static final String[] s_category = { "None", "Spell protections", "Specific protections", "Illusionary protections", "Magic attack",
			"Divination attack", "Conjuration", "Combat protections", "Contingency", "Battleground", "Offensive damage", "Disabling", "Combination",
			"Non-combat" };
	public static final String[] s_school = { "None", "Abjurer", "Conjurer", "Diviner", "Enchanter", "Illusionist", "Invoker", "Necromancer", "Transmuter",
			"Generalist" };
	private static final String[] s_anim = { "None", "Fire aqua", "Fire blue", "Fire gold", "Fire green", "Fire magenta", "Fire purple", "Fire red",
			"Fire white", "Necromancy", "Alteration", "Enchantment", "Abjuration", "Illusion", "Conjuration", "Invocation", "Divination", "Fountain aqua",
			"Fountain black", "Fountain blue", "Fountain gold", "Fountain green", "Fountain magenta", "Fountain purple", "Fountain red", "Fountain white",
			"Swirl aqua", "Swirl black", "Swirl blue", "Swirl gold", "Swirl green", "Swirl magenta", "Swirl purple", "Swirl red", "Swirl white" };
	private static final String[] s_spellflag = { "No flags set", "", "", "", "", "", "", "", "", "", "", "Hostile/Breaks Invisibility", "No LOS required",
			"Allow spotting", "Outdoors only", "Non-magical ability", "Trigger/Contingency", "Non-combat ability", "", "", "", "", "", "", "",
			"Ex: can target invisible", "Ex: castable when silenced" };
	private static final String[] s_exclude = { "None", "Chaotic", "Evil", "Good", "... Neutral", "Lawful", "Neutral ...", "Abjurer", "Conjurer", "Diviner",
			"Enchanter", "Illusionist", "Invoker", "Necromancer", "Transmuter", "Generalist", "", "", "", "", "", "", "", "", "Elf", "Dwarf", "Half-elf",
			"Halfling", "Human", "Gnome", "", "Cleric", "Druid" };

	public static Spell getSpell(ResourceEntry entry) throws IOException, StringResourceException {
		Spell spell = new Spell();
		spell.setResource(entry.getResourceName());
		byte buffer[] = entry.getResourceData();
		spell.setName(StringResource.getInstance().getStringRef(buffer, 8));

		Flag flag = new Flag(buffer, 24, 4, "Flags", s_spellflag);
		spell.setType(DynamicArray.getShort(buffer, 28));
		Flag excludeFlag = new Flag(buffer, 30, 4, "Exclusion flags", s_exclude);
		// list.add(new Bitmap(buffer, offset + 37, 1, "Primary type (school)", s_school)); // 0x25
		spell.setSchool(DynamicArray.getByte(buffer, 37));
		return spell;
	}

}
