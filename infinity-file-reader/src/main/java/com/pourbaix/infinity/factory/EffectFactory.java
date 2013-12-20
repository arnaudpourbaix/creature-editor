package com.pourbaix.infinity.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pourbaix.infinity.cache.CacheException;
import com.pourbaix.infinity.entity.Effect;
import com.pourbaix.infinity.entity.Flag;
import com.pourbaix.infinity.entity.ResistanceEnum;
import com.pourbaix.infinity.entity.TargetTypeEnum;
import com.pourbaix.infinity.entity.TimingEnum;
import com.pourbaix.infinity.entity.UnknownValueException;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.util.DynamicArray;

public abstract class EffectFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EffectFactory.class);

	static final String[] SAVE_TYPES = { "No save", "Spell", "Breath weapon", "Paralyze/Poison/Death", "Rod/Staff/Wand", "Petrify/Polymorph", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "Ex: bypass mirror image", "EE: ignore difficulty" };

	private static final String[] EFFECT_OPCODES = new String[] { "AC bonus", "Modify attacks per round", "Cure sleep", "Berserk", "Cure berserk",
			"Charm creature", "Charisma bonus", "Set color", "Set color glow solid", "Set color glow pulse", "Constitution bonus", "Cure poison", "Damage",
			"Kill target", "Defrost", "Dexterity bonus", "Haste", "Current HP bonus", "Maximum HP bonus", "Intelligence bonus", "Invisibility", "Lore bonus",
			"Luck bonus", "Reset morale", "Panic", "Poison", "Remove curse", "Acid resistance bonus", "Cold resistance bonus", "Electricity resistance bonus",
			"Fire resistance bonus", "Magic damage resistance bonus", "Raise dead", "Save vs. death bonus", "Save vs. wand bonus", "Save vs. polymorph bonus",
			"Save vs. breath bonus", "Save vs. spell bonus", "Silence", "Sleep", "Slow", "Sparkle", "Bonus wizard spells", "Stone to flesh", "Strength bonus",
			"Stun", "Cure stun", "Remove invisibility", "Vocalize", "Wisdom bonus", "Character color pulse", "Character tint solid", "Character tint bright",
			"Animation change", "Base THAC0 bonus", "Slay", "Reverse alignment", "Change alignment", "Dispel effects", "Move silently bonus",
			"Casting failure", "Unknown (3D)", "Bonus priest spells", "Infravision", "Remove infravision", "Blur", "Translucency", "Summon creature",
			"Unsummon creature", "Nondetection", "Remove nondetection", "Change gender", "Change AI type", "Attack damage bonus", "Blindness",
			"Cure blindness", "Feeblemindedness", "Cure feeblemindedness", "Disease", "Cure disease", "Deafness", "Cure deafness", "Set AI script",
			"Immunity to projectile", "Magical fire resistance bonus", "Magical cold resistance bonus", "Slashing resistance bonus",
			"Crushing resistance bonus", "Piercing resistance bonus", "Missile resistance bonus", "Open locks bonus", "Find traps bonus", "Pick pockets bonus",
			"Fatigue bonus", "Intoxication bonus", "Tracking bonus", "Change level", "Exceptional strength bonus", "Regeneration", "Modify duration",
			"Protection from creature type", "Immunity to effect", "Immunity to spell level", "Change name", "XP bonus", "Remove gold", "Morale break",
			"Change portrait", "Reputation bonus", "Paralyze", "Retreat from", "Create weapon", "Remove item", "Equip weapon", "Dither", "Detect alignment",
			"Detect invisible", "Clairvoyance", "Show creatures", "Mirror image", "Immunity to weapons", "Visual animation effect", "Create inventory item",
			"Remove inventory item", "Teleport", "Unlock", "Movement rate bonus", "Summon monsters", "Confusion", "Aid (non-cumulative)",
			"Bless (non-cumulative)", "Chant (non-cumulative)", "Draw upon holy might (non-cumulative)", "Luck (non-cumulative)", "Petrification", "Polymorph",
			"Force visible", "Bad chant (non-cumulative)", "Set animation sequence", "Display string", "Casting glow", "Lighting effects",
			"Display portrait icon", "Create item in slot", "Disable button", "Disable spellcasting", "Cast spell", "Learn spell", "Cast spell at point",
			"Identify", "Find traps", "Replace self", "Play movie", "Sanctuary", "Entangle overlay", "Minor globe overlay",
			"Protection from normal missiles overlay", "Web effect", "Grease overlay", "Mirror image effect", "Remove sanctuary", "Remove fear",
			"Remove paralysis", "Free action", "Remove intoxication", "Pause caster", "Magic resistance bonus", "Missile THAC0 bonus", "Remove creature",
			"Prevent portrait icon", "Play damage animation", "Give innate ability", "Remove spell", "Poison resistance bonus", "Play sound", "Hold creature",
			"Movement rate bonus 2", "Use EFF file", "THAC0 vs. type bonus", "Damage vs. type bonus", "Disallow item", "Disallow item type",
			"Apply effect on equip item", "Apply effect on equip type", "No collision detection", "Hold creature 2", "Move creature", "Set local variable",
			"Increase spells cast per round", "Increase casting speed factor", "Increase attack speed factor", "Casting level bonus", "Find familiar",
			"Invisibility detection", "Ignore dialogue pause", "Drain HP on death", "Familiar", "Physical mirror", "Reflect specified effect",
			"Reflect spell level", "Spell turning", "Spell deflection", "Reflect spell school", "Reflect spell type", "Protection from spell school",
			"Protection from spell type", "Protection from spell", "Reflect specified spell", "Minimum HP", "Power word, kill", "Power word, stun",
			"Imprisonment", "Freedom", "Maze", "Select spell", "Play visual effect", "Level drain", "Power word, sleep", "Stoneskin effect",
			"Attack roll penalty", "Remove spell school protections", "Remove spell type protections", "Teleport field", "Spell school deflection",
			"Restoration", "Detect magic", "Spell type deflection", "Spell school turning", "Spell type turning", "Remove protection by school",
			"Remove protection by type", "Time stop", "Cast spell on condition", "Modify proficiencies", "Create contingency", "Wing buffet", "Project image",
			"Set image type", "Disintegrate", "Farsight", "Remove portrait icon", "Control creature", "Cure confusion", "Drain item charges",
			"Drain wizard spells", "Check for berserk", "Berserk effect", "Attack nearest creature", "Melee hit effect", "Ranged hit effect",
			"Maximum damage each hit", "Change bard song", "Set trap", "Set automap note", "Remove automap note", "Create item (days)", "Spell sequencer",
			"Create spell sequencer", "Activate spell sequencer", "Spell trap", "Unknown (104)", "Wondrous recall", "Visual range bonus", "Backstab bonus",
			"Drop item", "Set global variable", "Remove protection from spell", "Disable display string", "Clear fog of war", "Shake screen", "Unpause caster",
			"Disable creature", "Use EFF file on condition", "Zone of sweet air", "Phase", "Hide in shadows bonus", "Detect illusions bonus",
			"Set traps bonus", "THAC0 bonus", "Enable button", "Wild magic", "Wild surge bonus", "Modify script state", "Use EFF file as curse",
			"Melee THAC0 bonus", "Melee weapon damage bonus", "Missile weapon damage bonus", "Remove feet circle", "Fist THAC0 bonus", "Fist damage bonus",
			"Change title", "Disable visual effects", "Immunity to backstab", "Set persistent AI", "Set existence delay", "Disable permanent death",
			"Immunity to specific animation", "Immunity to turn undead", "Pocket plane", "Chaos shield effect", "Modify collision behavior",
			"Critical threat range bonus", "Can use any item", "Backstab every hit", "Mass raise dead", "Off-hand THAC0 bonus", "Main hand THAC0 bonus",
			"Tracking", "Immunity to tracking", "Set variable", "Immunity to time stop", "Wish", "Immunity to sequester", "High-level ability",
			"Stoneskin protection", "Remove animation", "Rest", "Haste 2", "Unknown (13E)", "Restrict item", "Change weather", "Remove effects by resource" };

	public static List<Effect> getEffects(byte buffer[], int offset, int count) throws FactoryException, CacheException {
		List<Effect> effects = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			Effect effect = getEffect(buffer, offset);
			effects.add(effect);
			offset += 48;
		}
		return effects;
	}

	private static Effect getEffect(byte buffer[], int offset) throws FactoryException, CacheException {
		Effect effect = new Effect();
		int value = (int) DynamicArray.getShort(buffer, offset);
		try {
			effect.setTarget(TargetTypeEnum.valueOf(DynamicArray.getByte(buffer, offset + 2)));
			effect.setTiming(TimingEnum.valueOf(DynamicArray.getByte(buffer, offset + 12)));
			effect.setResistance(ResistanceEnum.valueOf(DynamicArray.getByte(buffer, offset + 13)));
			effect.setDuration(DynamicArray.getInt(buffer, offset + 14));
			effect.setProbability1(DynamicArray.getByte(buffer, offset + 18));
			effect.setProbability2(DynamicArray.getByte(buffer, offset + 19));
			effect.setDiceThrown((short) DynamicArray.getInt(buffer, offset + 28));
			effect.setDiceSides((short) DynamicArray.getInt(buffer, offset + 32));
			effect.setSavingThrowType(new Flag(DynamicArray.getInt(buffer, offset + 36), 4, SAVE_TYPES));
			effect.setSavingThrowBonus((short) DynamicArray.getInt(buffer, offset + 40));
			fetchResource(effect, buffer, offset);
		} catch (UnknownValueException e) {
			throw new FactoryException(e);
		}
		logger.debug(EFFECT_OPCODES[value] + ", " + effect.toString());
		return effect;
	}

	private static void fetchResource(Effect effect, byte buffer[], int offset) throws FactoryException, CacheException {
		//new ResourceRef(buffer, offset, "Resource", resourceType.split(":"));
	}

}
