package com.pourbaix.infinity.resource.spl;

import java.io.IOException;

import com.pourbaix.infinity.entity.Ids;
import com.pourbaix.infinity.entity.SchoolEnum;
import com.pourbaix.infinity.entity.Spell;
import com.pourbaix.infinity.entity.SpellSecondaryTypeEnum;
import com.pourbaix.infinity.entity.SpellTypeEnum;
import com.pourbaix.infinity.entity.UnknownValueException;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.StringResourceException;
import com.pourbaix.infinity.resource.datatype.Flag;
import com.pourbaix.infinity.resource.ids.IdsFactory;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.DynamicArray;

public abstract class SpellFactory {

	private static final String[] s_spellflag = { "No flags set", "", "", "", "", "", "", "", "", "", "", "Hostile/Breaks Invisibility", "No LOS required",
			"Allow spotting", "Outdoors only", "Non-magical ability", "Trigger/Contingency", "Non-combat ability", "", "", "", "", "", "", "",
			"Ex: can target invisible", "Ex: castable when silenced" };
	private static final String[] s_exclude = { "None", "Chaotic", "Evil", "Good", "... Neutral", "Lawful", "Neutral ...", "Abjurer", "Conjurer", "Diviner",
			"Enchanter", "Illusionist", "Invoker", "Necromancer", "Transmuter", "Generalist", "", "", "", "", "", "", "", "", "Elf", "Dwarf", "Half-elf",
			"Halfling", "Human", "Gnome", "", "Cleric", "Druid" };

	private static Ids spellIdentifier = null;

	public static Spell getSpell(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException("Entry doesn't exist: " + entryName);
		}
		return getSpell(entry);
	}

	public static Spell getSpell(ResourceEntry entry) throws FactoryException {
		if (spellIdentifier == null) {
			spellIdentifier = IdsFactory.getIds("spell.ids");
		}
		try {
			Spell spell = new Spell();
			spell.setResource(entry.getResourceName());
			byte buffer[] = entry.getResourceData();
			spell.setName(StringResource.getInstance().getStringRef(buffer, 8));

			Flag flag = new Flag(buffer, 24, 4, "Flags", s_spellflag);
			Flag excludeFlag = new Flag(buffer, 30, 4, "Exclusion flags", s_exclude);
			spell.setType(SpellTypeEnum.valueOf(DynamicArray.getShort(buffer, 28)));
			spell.setSchool(SchoolEnum.valueOf(DynamicArray.getByte(buffer, 37)));
			spell.setSecondaryType(SpellSecondaryTypeEnum.valueOf(DynamicArray.getByte(buffer, 39)));
			spell.setLevel(DynamicArray.getInt(buffer, 52));
			spell.setDescription(StringResource.getInstance().getStringRef(buffer, 80));
			// 1 SPPR
			// 2 SPWI
			// 3 SPIN
			// 4 SPCL
			return spell;
		} catch (UnknownValueException | IOException | StringResourceException e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}

}
