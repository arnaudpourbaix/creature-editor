package com.pourbaix.infinity.resource.spl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pourbaix.infinity.entity.Flag;
import com.pourbaix.infinity.entity.IdentifierEntry;
import com.pourbaix.infinity.entity.SchoolEnum;
import com.pourbaix.infinity.entity.Spell;
import com.pourbaix.infinity.entity.SpellSecondaryTypeEnum;
import com.pourbaix.infinity.entity.SpellTypeEnum;
import com.pourbaix.infinity.entity.UnknownValueException;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.StringResourceException;
import com.pourbaix.infinity.resource.ids.IdentifierFactory;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.DynamicArray;

public abstract class SpellFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(SpellFactory.class);

	private static final String[] s_spellflag = { "None", "", "", "", "", "", "", "", "", "", "", "Hostile/Breaks Invisibility", "No LOS required",
			"Allow spotting", "Outdoors only", "Non-magical ability", "Trigger/Contingency", "Non-combat ability", "", "", "", "", "", "", "",
			"Ex: can target invisible", "Ex: castable when silenced" };
	private static final String[] s_exclude = { "None", "Chaotic", "Evil", "Good", "... Neutral", "Lawful", "Neutral ...", "Abjurer", "Conjurer", "Diviner",
			"Enchanter", "Illusionist", "Invoker", "Necromancer", "Transmuter", "Generalist", "", "", "", "", "", "", "", "", "Elf", "Dwarf", "Half-elf",
			"Halfling", "Human", "Gnome", "", "Cleric", "Druid" };

	public static Spell getSpell(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException("Entry doesn't exist: " + entryName);
		}
		return getSpell(entry);
	}

	public static Spell getSpell(ResourceEntry entry) throws FactoryException {
		try {
			Spell spell = new Spell();
			spell.setResource(entry.getResourceName());
			byte buffer[] = entry.getResourceData();
			spell.setName(StringResource.getInstance().getStringRef(buffer, 8));
			spell.setFlags(new Flag((long) DynamicArray.getInt(buffer, 24), 4, s_spellflag));
			spell.setExclusionFlags(new Flag((long) DynamicArray.getInt(buffer, 30), 4, s_exclude));
			spell.setType(SpellTypeEnum.valueOf(DynamicArray.getShort(buffer, 28)));
			spell.setSchool(SchoolEnum.valueOf(DynamicArray.getByte(buffer, 37)));
			spell.setSecondaryType(SpellSecondaryTypeEnum.valueOf(DynamicArray.getByte(buffer, 39)));
			spell.setLevel(DynamicArray.getInt(buffer, 52));
			spell.setDescription(StringResource.getInstance().getStringRef(buffer, 80));
			IdentifierEntry identifierEntry = IdentifierFactory.getSpellIdentifierByResource(spell.getResource());
			spell.setIdentifier(identifierEntry.getFirstValue());
			fetchSpellAbilities(buffer);
			return spell;
		} catch (UnknownValueException | IOException | StringResourceException e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}

	public static void fetchSpellAbilities(byte buffer[]) {
		int abil_offset = DynamicArray.getInt(buffer, 100);
		int abil_count = DynamicArray.getShort(buffer, 104);
		com.pourbaix.infinity.entity.Ability abilities[] = new com.pourbaix.infinity.entity.Ability[abil_count];
		for (int i = 0; i < abilities.length; i++) {
			abilities[i] = new com.pourbaix.infinity.entity.Ability(buffer, abil_offset, i);
			// abil_offset = abilities[i].getEndOffset();
			break;
		}
	}

}
