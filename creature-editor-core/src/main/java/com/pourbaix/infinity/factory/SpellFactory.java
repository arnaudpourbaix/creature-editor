package com.pourbaix.infinity.factory;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Spell;
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

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(SpellFactory.class);

	@Resource
	private IdentifierFactory identifierFactory;

	@Resource
	private AbilityFactory abilityFactory;

	@Resource
	private EffectFactory effectFactory;

	private static final String MISSING_SPELL_FILE = "MISSING_SPELL_FILE";
	private static final String INVALID_SPELL_FILE = "INVALID_SPELL_FILE";
	private static final String INVALID_SPELL_NAME = "INVALID_SPELL_NAME";
	private static final String INVALID_SPELL_TYPE = "INVALID_SPELL_TYPE";
	private static final String INVALID_SPELL_SCHOOL = "INVALID_SPELL_SCHOOL";
	private static final String INVALID_SPELL_SECONDARY_TYPE = "INVALID_SPELL_SECONDARY_TYPE";
	private static final String INVALID_SPELL_DESCRIPTION = "INVALID_SPELL_DESCRIPTION";

	private static final String[] FLAGS = { "None", "", "", "", "", "", "", "", "", "", "", "Hostile/Breaks Invisibility", "No LOS required", "Allow spotting",
			"Outdoors only", "Non-magical ability", "Trigger/Contingency", "Non-combat ability", "", "", "", "", "", "", "", "Ex: can target invisible",
			"Ex: castable when silenced" };
	private static final String[] EXCLUDE_FLAGS = { "None", "Chaotic", "Evil", "Good", "... Neutral", "Lawful", "Neutral ...", "Abjurer", "Conjurer",
			"Diviner", "Enchanter", "Illusionist", "Invoker", "Necromancer", "Transmuter", "Generalist", "", "", "", "", "", "", "", "", "Elf", "Dwarf",
			"Half-elf", "Halfling", "Human", "Gnome", "", "Cleric", "Druid" };

	public Spell getSpell(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException(MISSING_SPELL_FILE, entryName);
		}
		return getSpell(entry);
	}

	public Spell getSpell(ResourceEntry entry) throws FactoryException {
		RawSpell spell = new RawSpell();
		spell.setResource(entry.getResourceName());
		byte buffer[];
		try {
			buffer = entry.getResourceData();
		} catch (IOException e) {
			throw new FactoryException(INVALID_SPELL_FILE, entry.getResourceName());
		}
		try {
			spell.setName(StringResource.getInstance().getStringRef(buffer, 8));
		} catch (StringResourceException e) {
			throw new FactoryException(INVALID_SPELL_NAME, entry.getResourceName());
		}
		spell.setFlags(new Flag((long) DynamicArray.getInt(buffer, 24), 4, FLAGS));
		spell.setExclusionFlags(new Flag((long) DynamicArray.getInt(buffer, 30), 4, EXCLUDE_FLAGS));
		try {
			spell.setType(SpellTypeEnum.valueOf(DynamicArray.getShort(buffer, 28)));
		} catch (UnknownValueException e) {
			throw new FactoryException(INVALID_SPELL_TYPE, entry.getResourceName());
		}
		try {
			spell.setSchool(SchoolEnum.valueOf(DynamicArray.getByte(buffer, 37)));
		} catch (UnknownValueException e) {
			throw new FactoryException(INVALID_SPELL_SCHOOL, entry.getResourceName());
		}
		try {
			spell.setSecondaryType(SpellSecondaryTypeEnum.valueOf(DynamicArray.getByte(buffer, 39)));
		} catch (UnknownValueException e) {
			throw new FactoryException(INVALID_SPELL_SECONDARY_TYPE, entry.getResourceName());
		}
		spell.setLevel((byte) DynamicArray.getInt(buffer, 52));
		try {
			spell.setDescription(StringResource.getInstance().getStringRef(buffer, 80));
		} catch (StringResourceException e) {
			throw new FactoryException(INVALID_SPELL_DESCRIPTION, entry.getResourceName());
		}
		IdentifierEntry identifierEntry = identifierFactory.getSpellIdentifierByResource(spell.getResource());
		if (identifierEntry != null) {
			spell.setIdentifier(identifierEntry.getFirstValue());
		}
		int effectOffset = DynamicArray.getInt(buffer, 106);
		fetchSpellAbilities(spell, buffer, effectOffset);
		fetchSpellEffects(spell, buffer, effectOffset);
		//		logger.debug(spell.toString());
		return getSpell(spell);
	}

	private Spell getSpell(RawSpell rawSpell) {
		Spell spell = new Spell();
		spell.setResource(rawSpell.getResource().replace(".SPL", ""));
		spell.setName(StringUtils.abbreviate(rawSpell.getName(), 100));
		spell.setLevel(rawSpell.getLevel());
		spell.setDescription(rawSpell.getDescription());
		spell.setIdentifier(rawSpell.getIdentifier());
		spell.setType(rawSpell.getType());
		spell.setSecondaryType(rawSpell.getSecondaryType());
		spell.setSchool(rawSpell.getSchool());
		spell.setFlags(rawSpell.getFlags().getValue());
		spell.setExclusionFlags(rawSpell.getExclusionFlags().getValue());
		return spell;
	}

	private void fetchSpellAbilities(RawSpell spell, byte buffer[], int effectOffset) throws FactoryException {
		int offset = DynamicArray.getInt(buffer, 100);
		int count = DynamicArray.getShort(buffer, 104);
		spell.setAbilities(abilityFactory.getAbilities(spell.getResource(), buffer, offset, count, effectOffset));
	}

	private void fetchSpellEffects(RawSpell spell, byte buffer[], int effectOffset) throws FactoryException {
		int count = DynamicArray.getShort(buffer, 112);
		spell.setGlobalEffects(effectFactory.getEffects(spell.getResource(), buffer, effectOffset, count));
	}

}
