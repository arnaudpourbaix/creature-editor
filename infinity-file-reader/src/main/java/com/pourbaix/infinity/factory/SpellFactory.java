package com.pourbaix.infinity.factory;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.datatype.Flag;
import com.pourbaix.infinity.datatype.SchoolEnum;
import com.pourbaix.infinity.datatype.SpellSecondaryTypeEnum;
import com.pourbaix.infinity.datatype.SpellTypeEnum;
import com.pourbaix.infinity.datatype.UnknownValueException;
import com.pourbaix.infinity.domain.IdentifierEntry;
import com.pourbaix.infinity.domain.RawSpell;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.StringResourceException;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.DynamicArray;

@Service
public class SpellFactory {

	@Autowired
	private IdentifierFactory identifierFactory;

	@Autowired
	private AbilityFactory abilityFactory;

	@Autowired
	private EffectFactory effectFactory;

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(SpellFactory.class);

	private static final String[] FLAGS = { "None", "", "", "", "", "", "", "", "", "", "", "Hostile/Breaks Invisibility", "No LOS required", "Allow spotting",
			"Outdoors only", "Non-magical ability", "Trigger/Contingency", "Non-combat ability", "", "", "", "", "", "", "", "Ex: can target invisible",
			"Ex: castable when silenced" };
	private static final String[] s_exclude = { "None", "Chaotic", "Evil", "Good", "... Neutral", "Lawful", "Neutral ...", "Abjurer", "Conjurer", "Diviner",
			"Enchanter", "Illusionist", "Invoker", "Necromancer", "Transmuter", "Generalist", "", "", "", "", "", "", "", "", "Elf", "Dwarf", "Half-elf",
			"Halfling", "Human", "Gnome", "", "Cleric", "Druid" };

	public RawSpell getSpell(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException("Entry doesn't exist: " + entryName);
		}
		return getSpell(entry);
	}

	public RawSpell getSpell(ResourceEntry entry) throws FactoryException {
		try {
			RawSpell spell = new RawSpell();
			spell.setResource(entry.getResourceName());
			byte buffer[] = entry.getResourceData();
			spell.setName(StringResource.getInstance().getStringRef(buffer, 8));
			spell.setFlags(new Flag((long) DynamicArray.getInt(buffer, 24), 4, FLAGS));
			spell.setExclusionFlags(new Flag((long) DynamicArray.getInt(buffer, 30), 4, s_exclude));
			spell.setType(SpellTypeEnum.valueOf(DynamicArray.getShort(buffer, 28)));
			spell.setSchool(SchoolEnum.valueOf(DynamicArray.getByte(buffer, 37)));
			spell.setSecondaryType(SpellSecondaryTypeEnum.valueOf(DynamicArray.getByte(buffer, 39)));
			spell.setLevel((byte) DynamicArray.getInt(buffer, 52));
			spell.setDescription(StringResource.getInstance().getStringRef(buffer, 80));
			IdentifierEntry identifierEntry = identifierFactory.getSpellIdentifierByResource(spell.getResource());
			if (identifierEntry != null) {
				spell.setIdentifier(identifierEntry.getFirstValue());
			}
			int effectOffset = DynamicArray.getInt(buffer, 106);
			fetchSpellAbilities(spell, buffer, effectOffset);
			fetchSpellEffects(spell, buffer, effectOffset);
			return spell;
		} catch (UnknownValueException | IOException | StringResourceException e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}

	private void fetchSpellAbilities(RawSpell spell, byte buffer[], int effectOffset) throws FactoryException {
		int offset = DynamicArray.getInt(buffer, 100);
		int count = DynamicArray.getShort(buffer, 104);
		spell.setAbilities(abilityFactory.getAbilities(buffer, offset, count, effectOffset));
	}

	private void fetchSpellEffects(RawSpell spell, byte buffer[], int effectOffset) throws FactoryException {
		int count = DynamicArray.getShort(buffer, 112);
		spell.setGlobalEffects(effectFactory.getEffects(buffer, effectOffset, count));
	}

}
