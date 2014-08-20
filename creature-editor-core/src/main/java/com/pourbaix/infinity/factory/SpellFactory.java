package com.pourbaix.infinity.factory;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.creature.editor.domain.SpellOffensiveFlagEnum;
import com.pourbaix.infinity.datatype.Flag;
import com.pourbaix.infinity.datatype.SchoolEnum;
import com.pourbaix.infinity.datatype.SpellSecondaryTypeEnum;
import com.pourbaix.infinity.datatype.SpellTypeEnum;
import com.pourbaix.infinity.datatype.UnknownValueException;
import com.pourbaix.infinity.domain.Ability;
import com.pourbaix.infinity.domain.Effect;
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

	/**
	 * These arrays are only used by RawSpell objects
	 */
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
		parseEffects(spell, rawSpell.getGlobalEffects());
		for (Ability ability : rawSpell.getAbilities()) {
			parseEffects(spell, ability.getEffects());
		}
		return spell;
	}

	private void parseEffects(Spell spell, List<Effect> effects) {
		for (Effect effect : effects) {
			if (effect.getOpcodeId() == 12) {
				int value = effect.getParam2().getRawValue();
				if (value <= 3) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Crushing);
				} else if (value >= 65536 && value <= 65539) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Acid);
				} else if (value >= 131072 && value <= 131075) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Cold);
				} else if (value >= 262144 && value <= 262147) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Electricity);
				} else if (value >= 524288 && value <= 524291) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Fire);
				} else if (value >= 1048576 && value <= 1048579) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Piercing);
				} else if (value >= 2097152 && value <= 2097155) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Poison);
				} else if (value >= 4194304 && value <= 4194307) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Magic);
				} else if (value >= 8388608 && value <= 8388611) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Missile);
				} else if (value >= 16777216 && value <= 16777218) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.Slashing);
				} else if (value >= 33554432 && value <= 33554435) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.MagicFire);
				} else if (value >= 67108864 && value <= 67108867) {
					spell.setOffensiveFlag(SpellOffensiveFlagEnum.MagicCold);
				}
			} else if (effect.getOpcodeId() == 5) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.CharmCreature);
			} else if (effect.getOpcodeId() == 13) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.InstantDeath);
			} else if (effect.getOpcodeId() == 24) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Panic);
			} else if (effect.getOpcodeId() == 25) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Poison);
			} else if (effect.getOpcodeId() == 38) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Silence);
			} else if (effect.getOpcodeId() == 39) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Sleep);
			} else if (effect.getOpcodeId() == 40) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Slow);
			} else if (effect.getOpcodeId() == 45) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Stun);
			} else if (effect.getOpcodeId() == 55) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Slay);
			} else if (effect.getOpcodeId() == 60) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.CastingFailure);
			} else if (effect.getOpcodeId() == 74) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Blindness);
			} else if (effect.getOpcodeId() == 76) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Feeblemindedness);
			} else if (effect.getOpcodeId() == 80) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Deafness);
			} else if (effect.getOpcodeId() == 109) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Paralyze);
			} else if (effect.getOpcodeId() == 128) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Confusion);
			} else if (effect.getOpcodeId() == 134) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Petrification);
			} else if (effect.getOpcodeId() == 135) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Polymorph);
			} else if (effect.getOpcodeId() == 175) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.HoldCreature);
			} else if (effect.getOpcodeId() == 185) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.HoldCreature2);
			} else if (effect.getOpcodeId() == 209) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.PowerWordKill);
			} else if (effect.getOpcodeId() == 210) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.PowerWordStun);
			} else if (effect.getOpcodeId() == 211) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Imprisonment);
			} else if (effect.getOpcodeId() == 213) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Maze);
			} else if (effect.getOpcodeId() == 216) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.LevelDrain);
			} else if (effect.getOpcodeId() == 217) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.PowerWordSleep);
			} else if (effect.getOpcodeId() == 238) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.Disintegrate);
			} else if (effect.getOpcodeId() == 241) {
				spell.setOffensiveFlag(SpellOffensiveFlagEnum.CharmCreature);
			}
		}
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
