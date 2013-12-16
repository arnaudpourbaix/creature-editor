package com.pourbaix.infinity.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

import com.pourbaix.infinity.resource.are.Actor;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.ColorValue;
import com.pourbaix.infinity.resource.datatype.Datatype;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.Flag;
import com.pourbaix.infinity.resource.datatype.HashBitmap;
import com.pourbaix.infinity.resource.datatype.HashBitmapEx;
import com.pourbaix.infinity.resource.datatype.IdsBitmap;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.StringRef;
import com.pourbaix.infinity.resource.datatype.TextString;
import com.pourbaix.infinity.resource.datatype.Unknown;
import com.pourbaix.infinity.resource.datatype.UnsignDecNumber;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.spl.SplResource;
import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public final class EffectFactory {
	private static EffectFactory efactory;
	private final String s_poricon[];
	private String s_effname[];

	/**
	 * Used in conjunction with <code>getEffectStructure</code> to address specific fields within an effect structure.
	 */
	public static enum EffectEntry {
		// EFF all versions
		// table index abs. structure offset
		IDX_OPCODE, OFS_OPCODE, IDX_TARGET, OFS_TARGET, IDX_POWER, OFS_POWER, IDX_PARAM1, OFS_PARAM1, IDX_PARAM2, OFS_PARAM2, IDX_TIMING, OFS_TIMING, IDX_RESISTANCE, OFS_RESISTANCE, IDX_DURATION, OFS_DURATION, IDX_PROBABILITY1, OFS_PROBABILITY1, IDX_PROBABILITY2, OFS_PROBABILITY2, IDX_RESOURCE, OFS_RESOURCE, IDX_DICETHROWN, OFS_DICETHROWN, IDX_DICESIDES, OFS_DICESIDES, IDX_SAVETYPE, OFS_SAVETYPE, IDX_SAVEBONUS, OFS_SAVEBONUS, IDX_SPECIAL, OFS_SPECIAL,
		// EFF V2.0 only
		// table index abs. structure offset
		IDX_PRIMARYTYPE, OFS_PRIMARYTYPE, IDX_UNKNOWN040, OFS_UNKNOWN040, IDX_PARENTLOWESTLEVEL, OFS_PARENTLOWESTLEVEL, IDX_PARENTHIGHESTLEVEL, OFS_PARENTHIGHESTLEVEL, IDX_PARAM3, OFS_PARAM3, IDX_PARAM4, OFS_PARAM4, IDX_RESOURCE2, OFS_RESOURCE2, IDX_RESOURCE3, OFS_RESOURCE3, IDX_UNKNOWN068, OFS_UNKNOWN068, IDX_UNKNOWN06C, OFS_UNKNOWN06C, IDX_CASTERX, OFS_CASTERX, IDX_CASTERY, OFS_CASTERY, IDX_TARGETX, OFS_TARGETX, IDX_TARGETY, OFS_TARGETY, IDX_PARENTRESOURCETYPE, OFS_PARENTRESOURCETYPE, IDX_PARENTRESOURCE, OFS_PARENTRESOURCE, IDX_PARENTRESOURCEFLAGS, OFS_PARENTRESOURCEFLAGS, IDX_PROJECTILE, OFS_PROJECTILE, IDX_PARENTRESOURCESLOT, OFS_PARENTRESOURCESLOT, IDX_VARIABLE, OFS_VARIABLE, IDX_CASTERLEVEL, OFS_CASTERLEVEL, IDX_FIRSTAPPLY, OFS_FIRSTAPPLY, IDX_SECONDARYTYPE, OFS_SECONDARYTYPE, IDX_UNKNOWN0C4, OFS_UNKNOWN0C4,
	}

	// contains IDS mappings for BGEE's opcode 319 "Item Usability"
	public static final LongIntegerHashMap<String> m_itemids = new LongIntegerHashMap<String>();

	private static final LongIntegerHashMap<String> m_colorloc = new LongIntegerHashMap<String>();
	private static final String s_inctype[] = { "Increment", "Set", "Set % of" };
	private static final String s_yesno[] = { "Yes", "No" };
	private static final String s_noyes[] = { "No", "Yes" };

	private static final String s_actype[] = { "All weapons", "Crushing weapons", "Missile weapons", "Piercing weapons", "Slashing weapons",
			"Set base AC to value" };
	private static final String s_button[] = { "Stealth", "Thieving", "Spell select", "Quick spell 1", "Quick spell 2", "Quick spell 3", "Turn undead", "Talk",
			"Use item", "Quick item 1", "", "Quick item 2", "Quick item 3", "Special abilities" };
	private static final String s_school[] = { "None", "Abjuration", "Conjuration", "Divination", "Enchantment", "Illusion", "Evocation", "Necromancy",
			"Alteration", "Generalist" };

	private static final String s_duration[] = { "Instant/Limited", "Instant/Permanent until death", "Instant/While equipped", "Delay/Limited",
			"Delay/Permanent", "Delay/While equipped", "Limited after duration", "Permanent after duration", "Equipped after duration", "Instant/Permanent",
			"Instant/Limited (ticks)" };

	static final String s_savetype[] = { "No save", "Spell", "Breath weapon", "Paralyze/Poison/Death", "Rod/Staff/Wand", "Petrify/Polymorph", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "Ex: bypass mirror image", "EE: ignore difficulty" };
	static final String s_savetype2[] = { "No save", "", "", "Fortitude", "Reflex", "Will" };

	static {
		m_itemids.put(2L, "EA.IDS");
		m_itemids.put(3L, "GENERAL.IDS");
		m_itemids.put(4L, "RACE.IDS");
		m_itemids.put(5L, "CLASS.IDS");
		m_itemids.put(6L, "SPECIFIC.IDS");
		m_itemids.put(7L, "GENDER.IDS");
		m_itemids.put(8L, "ALIGN.IDS");
		m_itemids.put(9L, "KIT.IDS");
		m_itemids.put(10L, "Actor's name");
		m_itemids.put(11L, "Actor's script name");

		m_colorloc.put(0L, "Belt/Amulet");
		m_colorloc.put(1L, "Minor color");
		m_colorloc.put(2L, "Major color");
		m_colorloc.put(3L, "Skin color");
		m_colorloc.put(4L, "Strap/Leather");
		m_colorloc.put(5L, "Armor/Trimming");
		m_colorloc.put(6L, "Hair");
		m_colorloc.put(16L, "Weapon head/blade/staff major");
		m_colorloc.put(20L, "Weapon grip/staff minor");
		m_colorloc.put(21L, "Weapon head/blade minor");
		m_colorloc.put(32L, "Shield hub");
		m_colorloc.put(33L, "Shield interior");
		m_colorloc.put(34L, "Shield panel");
		m_colorloc.put(35L, "Helm");
		m_colorloc.put(36L, "Shield grip");
		m_colorloc.put(37L, "Shield body/trim");
		m_colorloc.put(48L, "Helm wings");
		m_colorloc.put(49L, "Helm detail");
		m_colorloc.put(50L, "Helm plume");
		m_colorloc.put(52L, "Helm face");
		m_colorloc.put(53L, "Helm exterior");
		m_colorloc.put(255L, "Character color");
	}

	public static EffectFactory getFactory() {
		if (efactory == null)
			efactory = new EffectFactory();
		return efactory;
	}

	public static void init() {
		efactory = null;
	}

	/**
	 * Creates and returns an index/offset map of the current effect structure which can be used to address specific fields within the effect.
	 * 
	 * @param struct
	 *            The effect structure to map.
	 * @return A map containing table indices and structure offsets, starting with the opcode field.
	 * @throws Exception
	 *             If struct doesn't contain a valid effect structure.
	 */
	public static EnumMap<EffectEntry, Integer> getEffectStructure(AbstractStruct struct) throws Exception {
		if (struct != null) {
			EffectType effType = (EffectType) struct.getAttribute("Type");
			if (effType != null) {
				EnumMap<EffectEntry, Integer> map = new EnumMap<EffectFactory.EffectEntry, Integer>(EffectEntry.class);
				boolean isV1 = (effType.getSize() == 2);
				int ofsOpcode = effType.getOffset();
				int idxOpcode = struct.getIndexOf(struct.getAttribute(ofsOpcode));
				if (isV1 && struct.getSize() >= ofsOpcode + 0x30) {
					// EFF V1.0
					map.put(EffectEntry.IDX_OPCODE, idxOpcode);
					map.put(EffectEntry.OFS_OPCODE, ofsOpcode);
					map.put(EffectEntry.IDX_TARGET, idxOpcode + 1);
					map.put(EffectEntry.OFS_TARGET, ofsOpcode + 0x02);
					map.put(EffectEntry.IDX_POWER, idxOpcode + 2);
					map.put(EffectEntry.OFS_POWER, ofsOpcode + 0x03);
					map.put(EffectEntry.IDX_PARAM1, idxOpcode + 3);
					map.put(EffectEntry.OFS_PARAM1, ofsOpcode + 0x04);
					map.put(EffectEntry.IDX_PARAM2, idxOpcode + 4);
					map.put(EffectEntry.OFS_PARAM2, ofsOpcode + 0x08);
					map.put(EffectEntry.IDX_TIMING, idxOpcode + 5);
					map.put(EffectEntry.OFS_TIMING, ofsOpcode + 0x0C);
					map.put(EffectEntry.IDX_RESISTANCE, idxOpcode + 6);
					map.put(EffectEntry.OFS_RESISTANCE, ofsOpcode + 0x0D);
					map.put(EffectEntry.IDX_DURATION, idxOpcode + 7);
					map.put(EffectEntry.OFS_DURATION, ofsOpcode + 0x0E);
					map.put(EffectEntry.IDX_PROBABILITY1, idxOpcode + 8);
					map.put(EffectEntry.OFS_PROBABILITY1, ofsOpcode + 0x12);
					map.put(EffectEntry.IDX_PROBABILITY2, idxOpcode + 9);
					map.put(EffectEntry.OFS_PROBABILITY2, ofsOpcode + 0x13);
					map.put(EffectEntry.IDX_RESOURCE, idxOpcode + 10);
					map.put(EffectEntry.OFS_RESOURCE, ofsOpcode + 0x14);
					map.put(EffectEntry.IDX_DICETHROWN, idxOpcode + 11);
					map.put(EffectEntry.OFS_DICETHROWN, ofsOpcode + 0x1C);
					map.put(EffectEntry.IDX_DICESIDES, idxOpcode + 12);
					map.put(EffectEntry.OFS_DICESIDES, ofsOpcode + 0x20);
					map.put(EffectEntry.IDX_SAVETYPE, idxOpcode + 13);
					map.put(EffectEntry.OFS_SAVETYPE, ofsOpcode + 0x24);
					map.put(EffectEntry.IDX_SAVEBONUS, idxOpcode + 14);
					map.put(EffectEntry.OFS_SAVEBONUS, ofsOpcode + 0x28);
					map.put(EffectEntry.IDX_SPECIAL, idxOpcode + 15);
					map.put(EffectEntry.OFS_SPECIAL, ofsOpcode + 0x2C);
					return map;
				} else if (!isV1 && struct.getSize() >= ofsOpcode + 0x100) {
					// EFF V2.0
					map.put(EffectEntry.IDX_OPCODE, idxOpcode);
					map.put(EffectEntry.OFS_OPCODE, ofsOpcode);
					map.put(EffectEntry.IDX_TARGET, idxOpcode + 1);
					map.put(EffectEntry.OFS_TARGET, ofsOpcode + 0x04);
					map.put(EffectEntry.IDX_POWER, idxOpcode + 2);
					map.put(EffectEntry.OFS_POWER, ofsOpcode + 0x08);
					map.put(EffectEntry.IDX_PARAM1, idxOpcode + 3);
					map.put(EffectEntry.OFS_PARAM1, ofsOpcode + 0x0C);
					map.put(EffectEntry.IDX_PARAM2, idxOpcode + 4);
					map.put(EffectEntry.OFS_PARAM2, ofsOpcode + 0x10);
					map.put(EffectEntry.IDX_TIMING, idxOpcode + 5);
					map.put(EffectEntry.OFS_TIMING, ofsOpcode + 0x14);
					map.put(EffectEntry.IDX_DURATION, idxOpcode + 6);
					map.put(EffectEntry.OFS_DURATION, ofsOpcode + 0x18);
					map.put(EffectEntry.IDX_PROBABILITY1, idxOpcode + 7);
					map.put(EffectEntry.OFS_PROBABILITY1, ofsOpcode + 0x1C);
					map.put(EffectEntry.IDX_PROBABILITY2, idxOpcode + 8);
					map.put(EffectEntry.OFS_PROBABILITY2, ofsOpcode + 0x1E);
					map.put(EffectEntry.IDX_RESOURCE, idxOpcode + 9);
					map.put(EffectEntry.OFS_RESOURCE, ofsOpcode + 0x20);
					map.put(EffectEntry.IDX_DICETHROWN, idxOpcode + 10);
					map.put(EffectEntry.OFS_DICETHROWN, ofsOpcode + 0x28);
					map.put(EffectEntry.IDX_DICESIDES, idxOpcode + 11);
					map.put(EffectEntry.OFS_DICESIDES, ofsOpcode + 0x2C);
					map.put(EffectEntry.IDX_SAVETYPE, idxOpcode + 12);
					map.put(EffectEntry.OFS_SAVETYPE, ofsOpcode + 0x30);
					map.put(EffectEntry.IDX_SAVEBONUS, idxOpcode + 13);
					map.put(EffectEntry.OFS_SAVEBONUS, ofsOpcode + 0x34);
					map.put(EffectEntry.IDX_SPECIAL, idxOpcode + 14);
					map.put(EffectEntry.OFS_SPECIAL, ofsOpcode + 0x38);
					map.put(EffectEntry.IDX_PRIMARYTYPE, idxOpcode + 15);
					map.put(EffectEntry.OFS_PRIMARYTYPE, ofsOpcode + 0x3C);
					map.put(EffectEntry.IDX_UNKNOWN040, idxOpcode + 16);
					map.put(EffectEntry.OFS_UNKNOWN040, ofsOpcode + 0x40);
					map.put(EffectEntry.IDX_PARENTLOWESTLEVEL, idxOpcode + 17);
					map.put(EffectEntry.OFS_PARENTLOWESTLEVEL, ofsOpcode + 0x44);
					map.put(EffectEntry.IDX_PARENTHIGHESTLEVEL, idxOpcode + 18);
					map.put(EffectEntry.OFS_PARENTHIGHESTLEVEL, ofsOpcode + 0x48);
					map.put(EffectEntry.IDX_RESISTANCE, idxOpcode + 19);
					map.put(EffectEntry.OFS_RESISTANCE, ofsOpcode + 0x4C);
					map.put(EffectEntry.IDX_PARAM3, idxOpcode + 20);
					map.put(EffectEntry.OFS_PARAM3, ofsOpcode + 0x50);
					map.put(EffectEntry.IDX_PARAM4, idxOpcode + 21);
					map.put(EffectEntry.OFS_PARAM4, ofsOpcode + 0x54);
					map.put(EffectEntry.IDX_RESOURCE2, idxOpcode + 22);
					map.put(EffectEntry.OFS_RESOURCE2, ofsOpcode + 0x58);
					map.put(EffectEntry.IDX_RESOURCE3, idxOpcode + 23);
					map.put(EffectEntry.OFS_RESOURCE3, ofsOpcode + 0x60);
					map.put(EffectEntry.IDX_UNKNOWN068, idxOpcode + 24);
					map.put(EffectEntry.OFS_UNKNOWN068, ofsOpcode + 0x68);
					map.put(EffectEntry.IDX_UNKNOWN06C, idxOpcode + 25);
					map.put(EffectEntry.OFS_UNKNOWN06C, ofsOpcode + 0x6C);
					map.put(EffectEntry.IDX_CASTERX, idxOpcode + 26);
					map.put(EffectEntry.OFS_CASTERX, ofsOpcode + 0x70);
					map.put(EffectEntry.IDX_CASTERY, idxOpcode + 27);
					map.put(EffectEntry.OFS_CASTERY, ofsOpcode + 0x74);
					map.put(EffectEntry.IDX_TARGETX, idxOpcode + 28);
					map.put(EffectEntry.OFS_TARGETX, ofsOpcode + 0x78);
					map.put(EffectEntry.IDX_TARGETY, idxOpcode + 29);
					map.put(EffectEntry.OFS_TARGETY, ofsOpcode + 0x7C);
					map.put(EffectEntry.IDX_PARENTRESOURCETYPE, idxOpcode + 30);
					map.put(EffectEntry.OFS_PARENTRESOURCETYPE, ofsOpcode + 0x80);
					map.put(EffectEntry.IDX_PARENTRESOURCE, idxOpcode + 31);
					map.put(EffectEntry.OFS_PARENTRESOURCE, ofsOpcode + 0x84);
					map.put(EffectEntry.IDX_PARENTRESOURCEFLAGS, idxOpcode + 32);
					map.put(EffectEntry.OFS_PARENTRESOURCEFLAGS, ofsOpcode + 0x8C);
					map.put(EffectEntry.IDX_PROJECTILE, idxOpcode + 33);
					map.put(EffectEntry.OFS_PROJECTILE, ofsOpcode + 0x90);
					map.put(EffectEntry.IDX_PARENTRESOURCESLOT, idxOpcode + 34);
					map.put(EffectEntry.OFS_PARENTRESOURCESLOT, ofsOpcode + 0x94);
					map.put(EffectEntry.IDX_VARIABLE, idxOpcode + 35);
					map.put(EffectEntry.OFS_VARIABLE, ofsOpcode + 0x98);
					map.put(EffectEntry.IDX_CASTERLEVEL, idxOpcode + 36);
					map.put(EffectEntry.OFS_CASTERLEVEL, ofsOpcode + 0xb8);
					map.put(EffectEntry.IDX_FIRSTAPPLY, idxOpcode + 37);
					map.put(EffectEntry.OFS_FIRSTAPPLY, ofsOpcode + 0xbc);
					map.put(EffectEntry.IDX_SECONDARYTYPE, idxOpcode + 38);
					map.put(EffectEntry.OFS_SECONDARYTYPE, ofsOpcode + 0xc0);
					map.put(EffectEntry.IDX_UNKNOWN0C4, idxOpcode + 39);
					map.put(EffectEntry.OFS_UNKNOWN0C4, ofsOpcode + 0xc4);
					return map;
				}
			}
		}
		throw new Exception("Invalid effect structure specified");
	}

	/**
	 * Returns the StructEntry object at the specified index. Use in conjunction with getEffectStructure.
	 * 
	 * @param struct
	 *            The structure that contains the requested entry.
	 * @param entryIndex
	 *            The index of the requested entry.
	 * @return The entry at the specified index
	 * @throws Exception
	 *             If one or more arguments are invalid.
	 */
	public static StructEntry getEntry(AbstractStruct struct, int entryIndex) throws Exception {
		if (struct != null) {
			if (entryIndex >= 0 && entryIndex < struct.getList().size()) {
				return struct.getList().get(entryIndex);
			} else
				throw new Exception("Index out of bounds");
		} else
			throw new Exception("Invalid arguments specified");
	}

	/**
	 * Returns the data associated with the specified structure entry.
	 * 
	 * @param entry
	 *            The structure entry to fetch data from.
	 * @return Data as byte array.
	 */
	public static byte[] getEntryData(StructEntry entry) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		if (entry != null) {
			try {
				entry.write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return os.toByteArray();
	}

	/**
	 * Convenience function to retrieve data associated with a structure entry within struct.
	 * 
	 * @param struct
	 *            The structure that contains the structure entry
	 * @param entryIndex
	 *            The index of the structure entry within struct
	 * @return Data as byte array
	 */
	public static byte[] getEntryData(AbstractStruct struct, int entryIndex) {
		StructEntry entry = null;
		if (struct != null && entryIndex >= 0 && entryIndex < struct.getList().size())
			entry = struct.getList().get(entryIndex);

		return getEntryData(entry);
	}

	/**
	 * Replaces a data entry in struct with the specified new entry.
	 * 
	 * @param struct
	 *            The struct which contains the entry specified by entryIndex and entryOffset.
	 * @param entryIndex
	 *            The index of the entry within struct.
	 * @param entryOffset
	 *            The absolute offset of the data entry.
	 * @param newEntry
	 *            The new entry which replaces the old one.
	 */
	public static void replaceEntry(AbstractStruct struct, int entryIndex, int entryOffset, StructEntry newEntry) throws Exception {
		if (struct != null && newEntry != null) {
			List<StructEntry> list = struct.getList();
			if (list != null && entryIndex >= 0 && entryIndex < list.size() && entryOffset >= struct.getOffset()
					&& entryOffset < struct.getOffset() + struct.getSize()) {
				newEntry.setOffset(entryOffset);
				list.remove(entryIndex);
				list.add(entryIndex, newEntry);
			} else
				throw new Exception("Index or offset are out of bounds");
		} else
			throw new Exception("Invalid arguments specified");
	}

	/**
	 * Central hub for dynamic opcode specific modifications of effect structures.
	 * 
	 * @param struct
	 *            The effect structure to update.
	 * @return true if fields within the effect structure have been updated, false otherwise.
	 * @throws Exception
	 *             If the argument doesn't specify a valid effect structure.
	 */
	public static boolean updateOpcode(AbstractStruct struct) throws Exception {
		if (struct != null) {
			EnumMap<EffectEntry, Integer> map = getEffectStructure(struct);
			EffectType effType = (EffectType) getEntry(struct, map.get(EffectEntry.IDX_OPCODE));
			if (effType != null) {
				int opcode = ((EffectType) getEntry(struct, map.get(EffectEntry.IDX_OPCODE))).getValue();
				switch (opcode) {
				case 232: // Cast spell on condition
					return updateOpcode232(struct);
				case 319: // Item Usability
					return updateOpcode319(struct);
				}
			}
		}
		return false;
	}

	// Effect type "Cast spell on condition" (232/0xE8)
	private static boolean updateOpcode232(AbstractStruct struct) throws Exception {
		if (struct != null) {
			EnumMap<EffectEntry, Integer> map = getEffectStructure(struct);
			if (map.containsKey(EffectEntry.IDX_OPCODE)) {
				int opcode = ((EffectType) getEntry(struct, map.get(EffectEntry.IDX_OPCODE))).getValue();
				if (opcode == 232) { // effect type "Cast spell on condition" (232/0xE8)
					int param2 = ((Bitmap) getEntry(struct, map.get(EffectEntry.IDX_PARAM2))).getValue();
					if (param2 == 13) {
						// Special = Time of day
						replaceEntry(struct, map.get(EffectEntry.IDX_SPECIAL), map.get(EffectEntry.OFS_SPECIAL),
								new IdsBitmap(getEntryData(struct, map.get(EffectEntry.IDX_SPECIAL)), 0, 4, "Time of day", "TIMEODAY.IDS"));
					} else {
						// Special = Unused
						replaceEntry(struct, map.get(EffectEntry.IDX_SPECIAL), map.get(EffectEntry.OFS_SPECIAL),
								new DecNumber(getEntryData(struct, map.get(EffectEntry.IDX_SPECIAL)), 0, 4, "Unused"));
					}
					return true;
				}
			}
		}
		return false;
	}

	// Effect type "Item Usability" (319/0x13F).
	private static boolean updateOpcode319(AbstractStruct struct) throws Exception {
		if (struct != null) {
			EnumMap<EffectEntry, Integer> map = getEffectStructure(struct);
			if (map.containsKey(EffectEntry.IDX_OPCODE)) {
				int opcode = ((EffectType) getEntry(struct, map.get(EffectEntry.IDX_OPCODE))).getValue();
				if (opcode == 319) {
					long param2 = ((HashBitmap) getEntry(struct, map.get(EffectEntry.IDX_PARAM2))).getValue();
					if (param2 == 10L) {
						// Param1 = Actor's name as Strref
						replaceEntry(struct, map.get(EffectEntry.IDX_PARAM1), map.get(EffectEntry.OFS_PARAM1),
								new StringRef(getEntryData(struct, map.get(EffectEntry.IDX_PARAM1)), 0, "Actor name"));
					} else if (param2 >= 2 && param2 < 10) {
						// Param1 = IDS entry
						replaceEntry(struct, map.get(EffectEntry.IDX_PARAM1), map.get(EffectEntry.OFS_PARAM1),
								new IdsBitmap(getEntryData(struct, map.get(EffectEntry.IDX_PARAM1)), 0, 4, "IDS entry", EffectFactory.m_itemids.get(param2)));
					} else {
						// Param1 = Unused
						replaceEntry(struct, map.get(EffectEntry.IDX_PARAM1), map.get(EffectEntry.OFS_PARAM1),
								new DecNumber(getEntryData(struct, map.get(EffectEntry.IDX_PARAM1)), 0, 4, "Unused"));
					}

					if (param2 == 11L) {
						// Resource = Actor's script name
						replaceEntry(struct, map.get(EffectEntry.IDX_RESOURCE), map.get(EffectEntry.OFS_RESOURCE),
								new TextString(getEntryData(struct, map.get(EffectEntry.IDX_RESOURCE)), 0, 8, "Script name"));
					} else {
						// Resource = Unused
						replaceEntry(struct, map.get(EffectEntry.IDX_RESOURCE), map.get(EffectEntry.OFS_RESOURCE),
								new Unknown(getEntryData(struct, map.get(EffectEntry.IDX_RESOURCE)), 0, 8, "Unused"));
					}
					return true;
				}
			}
		}
		return false;
	}

	public EffectFactory() {
		s_effname = new String[] { "AC bonus", "Modify attacks per round", "Cure sleep", "Berserk", "Cure berserk", "Charm creature", "Charisma bonus",
				"Set color", "Set color glow solid", "Set color glow pulse", "Constitution bonus", "Cure poison", "Damage", "Kill target", "Defrost",
				"Dexterity bonus", "Haste", "Current HP bonus", "Maximum HP bonus", "Intelligence bonus", "Invisibility", "Lore bonus", "Luck bonus",
				"Reset morale", "Panic", "Poison", "Remove curse", "Acid resistance bonus", "Cold resistance bonus", "Electricity resistance bonus",
				"Fire resistance bonus", "Magic damage resistance bonus", "Raise dead", "Save vs. death bonus", "Save vs. wand bonus",
				"Save vs. polymorph bonus", "Save vs. breath bonus", "Save vs. spell bonus", "Silence", "Sleep", "Slow", "Sparkle", "Bonus wizard spells",
				"Stone to flesh", "Strength bonus", "Stun", "Cure stun", "Remove invisibility", "Vocalize", "Wisdom bonus", "Character color pulse",
				"Character tint solid", "Character tint bright", "Animation change", "Base THAC0 bonus", "Slay", "Reverse alignment", "Change alignment",
				"Dispel effects", "Move silently bonus", "Casting failure", "Unknown (3D)", "Bonus priest spells", "Infravision", "Remove infravision", "Blur",
				"Translucency", "Summon creature", "Unsummon creature", "Nondetection", "Remove nondetection", "Change gender", "Change AI type",
				"Attack damage bonus", "Blindness", "Cure blindness", "Feeblemindedness", "Cure feeblemindedness", "Disease", "Cure disease", "Deafness",
				"Cure deafness", "Set AI script", "Immunity to projectile", "Magical fire resistance bonus", "Magical cold resistance bonus",
				"Slashing resistance bonus", "Crushing resistance bonus", "Piercing resistance bonus", "Missile resistance bonus", "Open locks bonus",
				"Find traps bonus", "Pick pockets bonus", "Fatigue bonus", "Intoxication bonus", "Tracking bonus", "Change level",
				"Exceptional strength bonus", "Regeneration", "Modify duration", "Protection from creature type", "Immunity to effect",
				"Immunity to spell level", "Change name", "XP bonus", "Remove gold", "Morale break", "Change portrait", "Reputation bonus", "Paralyze",
				"Retreat from", "Create weapon", "Remove item", "Equip weapon", "Dither", "Detect alignment", "Detect invisible", "Clairvoyance",
				"Show creatures", "Mirror image", "Immunity to weapons", "Visual animation effect", "Create inventory item", "Remove inventory item",
				"Teleport", "Unlock", "Movement rate bonus", "Summon monsters", "Confusion", "Aid (non-cumulative)", "Bless (non-cumulative)",
				"Chant (non-cumulative)", "Draw upon holy might (non-cumulative)", "Luck (non-cumulative)", "Petrification", "Polymorph", "Force visible",
				"Bad chant (non-cumulative)", "Set animation sequence", "Display string", "Casting glow", "Lighting effects", "Display portrait icon",
				"Create item in slot", "Disable button", "Disable spellcasting", "Cast spell", "Learn spell", "Cast spell at point", "Identify", "Find traps",
				"Replace self", "Play movie", "Sanctuary", "Entangle overlay", "Minor globe overlay", "Protection from normal missiles overlay", "Web effect",
				"Grease overlay", "Mirror image effect", "Remove sanctuary", "Remove fear", "Remove paralysis", "Free action", "Remove intoxication",
				"Pause caster", "Magic resistance bonus", "Missile THAC0 bonus", "Remove creature", "Prevent portrait icon", "Play damage animation",
				"Give innate ability", "Remove spell", "Poison resistance bonus", "Play sound", "Hold creature", "Movement rate bonus 2", "Use EFF file",
				"THAC0 vs. type bonus", "Damage vs. type bonus", "Disallow item", "Disallow item type", "Apply effect on equip item",
				"Apply effect on equip type", "No collision detection", "Hold creature 2", "Move creature", "Set local variable",
				"Increase spells cast per round", "Increase casting speed factor", "Increase attack speed factor", "Casting level bonus", "Find familiar",
				"Invisibility detection", "Ignore dialogue pause", "Drain HP on death", "Familiar", "Physical mirror", "Reflect specified effect",
				"Reflect spell level", "Spell turning", "Spell deflection", "Reflect spell school", "Reflect spell type", "Protection from spell school",
				"Protection from spell type", "Protection from spell", "Reflect specified spell", "Minimum HP", "Power word, kill", "Power word, stun",
				"Imprisonment", "Freedom", "Maze", "Select spell", "Play visual effect", "Level drain", "Power word, sleep", "Stoneskin effect",
				"Attack roll penalty", "Remove spell school protections", "Remove spell type protections", "Teleport field", "Spell school deflection",
				"Restoration", "Detect magic", "Spell type deflection", "Spell school turning", "Spell type turning", "Remove protection by school",
				"Remove protection by type", "Time stop", "Cast spell on condition", "Modify proficiencies", "Create contingency", "Wing buffet",
				"Project image", "Set image type", "Disintegrate", "Farsight", "Remove portrait icon", "Control creature", "Cure confusion",
				"Drain item charges", "Drain wizard spells", "Check for berserk", "Berserk effect", "Attack nearest creature", "Melee hit effect",
				"Ranged hit effect", "Maximum damage each hit", "Change bard song", "Set trap", "Set automap note", "Remove automap note",
				"Create item (days)", "Spell sequencer", "Create spell sequencer", "Activate spell sequencer", "Spell trap", "Unknown (104)",
				"Wondrous recall", "Visual range bonus", "Backstab bonus", "Drop item", "Set global variable", "Remove protection from spell",
				"Disable display string", "Clear fog of war", "Shake screen", "Unpause caster", "Disable creature", "Use EFF file on condition",
				"Zone of sweet air", "Phase", "Hide in shadows bonus", "Detect illusions bonus", "Set traps bonus", "THAC0 bonus", "Enable button",
				"Wild magic", "Wild surge bonus", "Modify script state", "Use EFF file as curse", "Melee THAC0 bonus", "Melee weapon damage bonus",
				"Missile weapon damage bonus", "Remove feet circle", "Fist THAC0 bonus", "Fist damage bonus", "Change title", "Disable visual effects",
				"Immunity to backstab", "Set persistent AI", "Set existence delay", "Disable permanent death", "Immunity to specific animation",
				"Immunity to turn undead", "Pocket plane", "Chaos shield effect", "Modify collision behavior", "Critical threat range bonus",
				"Can use any item", "Backstab every hit", "Mass raise dead", "Off-hand THAC0 bonus", "Main hand THAC0 bonus", "Tracking",
				"Immunity to tracking", "Set variable", "Immunity to time stop", "Wish", "Immunity to sequester", "High-level ability", "Stoneskin protection",
				"Remove animation", "Rest", "Haste 2", "Unknown (13E)", "Restrict item", "Change weather", "Remove effects by resource" };
		s_poricon = new String[] { "Charm", "Dire charm", "Rigid thinking", "Confused", "Berserk", "Intoxicated", "Poisoned", "Nauseated", "Blind",
				"Protection from evil", "Protection from petrification", "Protection from normal missiles", "Magic armor", "Held", "Sleep", "Shielded",
				"Protection from fire", "Blessed", "Chant", "Free action", "Barkskin", "Strength", "Heroism", "Invulnerable", "Protection from acid",
				"Protection from cold", "Resist fire/cold", "Protection from electricity", "Protection from magic", "Protection from undead",
				"Protection from poison", "Nondetection", "Good luck", "Bad luck", "Silenced", "Cursed", "Panic", "Resist fear", "Haste", "Fatigue",
				"Bard song", "Slow", "Regenerate", "Domination", "Hopelessness", "Greater malison", "Spirit armor", "Chaos", "Feeblemind", "Defensive harmony",
				"Champion's strength", "Dying", "Mind shield", "Level drain", "Polymorph self", "Stun", "Regeneration", "Perception", "Master thievery",
				"Energy drain", "Holy power", "Cloak of fear", "Iron skins", "Magic resistance", "Righteous magic", "Spell turning", "Repulsing undead",
				"Spell deflection", "Fire shield (red)", "Fire shield (blue)", "Protection from normal weapons", "Protection from magic weapons",
				"Tenser's transformation", "Protection from magic energy", "Mislead", "Contingency", "Protection from the elements", "Projected image", "Maze",
				"Imprisonment", "Stoneskin", "Kai", "Called shot", "Spell failure", "Offensive stance", "Defensive stance", "Intelligence drained",
				"Regenerating", "Talking", "Shopping", "Negative plane protection", "Ability score drained", "Spell sequencer", "Protection from energy",
				"Magnetized", "Able to poison weapons", "Setting trap", "Glass dust", "Blade barrier", "Death ward", "Doom", "Decaying", "Acid", "Vocalize",
				"Mantle", "Miscast magic", "Lower resistance", "Spell immunity", "True seeing", "Detecting traps", "Improved haste", "Spell trigger", "Deaf",
				"Enfeebled", "Infravision", "Friends", "Shield of the archons", "Spell trap", "Absolute immunity", "Improved mantle", "Farsight",
				"Globe of invulnerability", "Minor globe of invulnerability", "Spell shield", "Polymorphed", "Otiluke's resilient sphere", "Nauseous",
				"Ghost armor", "Glitterdust", "Webbed", "Unconscious", "Mental combat", "Physical mirror", "Repulse undead", "Chaotic commands",
				"Draw upon holy might", "Strength of one", "Bleeding", "Barbarian rage", "Boon of lathander", "Storm shield", "Enraged", "Stunning blow",
				"Quivering palm", "Entangled", "Grease", "Smite", "Hardiness", "Power attack", "Whirlwind attack", "Greater whirlwind attack", "Magic flute",
				"Critical strike", "Greater deathblow", "Deathblow", "Avoid death", "Assassination", "Evasion", "Greater evasion", "Improved alacrity",
				"Aura of flaming death", "Globe of blades", "Improved chaos shield", "Chaos shield", "Fire elemental transformation",
				"Earth elemental transformation" };
	}

	public String[] getEffectNameArray() {
		return s_effname;
	}

	public int makeEffectStruct(Datatype parent, byte buffer[], int offset, List<StructEntry> s, int effectType, boolean isV1) throws Exception {
		if (buffer != null && offset >= 0 && s != null && effectType >= 0) {
			int param1 = DynamicArray.getInt(buffer, offset);
			int param2 = DynamicArray.getInt(buffer, offset + 4);

			// setting param1 & param2
			String restype = makeEffectParams(parent, buffer, offset, s, effectType, isV1);
			offset += 8;

			// setting common fields #1 ("Timing mode" ... "Probability2")
			offset = makeEffectCommon1(buffer, offset, s, isV1);

			// setting Resource field
			offset = makeEffectResource(parent, buffer, offset, s, effectType, restype, param1, param2);

			// setting common fields #2 ("Dice" ... "Save bonus")
			offset = makeEffectCommon2(buffer, offset, s, isV1);

			// setting Parameter 2.5 field
			offset = makeEffectParam25(parent, buffer, offset, s, effectType, restype, param1, param2);

			return offset;
		} else
			throw new Exception("Invalid parameters specified");
	}

	private String makeEffectParams(Datatype parent, byte buffer[], int offset, List<StructEntry> s, int effectType, boolean isV1) {
		final int initSize = s.size();

		// Processing effects common to all supported game engines
		String restype = makeEffectParamsGeneric(parent, buffer, offset, s, effectType, isV1);

		// Processing game specific effects
		if (s.size() == initSize && restype == null) {
			restype = makeEffectParamsBG2(parent, buffer, offset, s, effectType, isV1);
		}

		// failsafe initialization
		if (s.size() == initSize) {
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Unknown(buffer, offset + 4, 4));
		}

		return restype;
	}

	private String makeEffectParamsGeneric(Datatype parent, byte buffer[], int offset, List<StructEntry> s, int effectType, boolean isV1) {
		String restype = null;

		switch (effectType) {
		case 0x5: // Charm creature (CGameEffectCharm)
			s.add(new IdsBitmap(buffer, offset, 4, "Creature type", "GENERAL.IDS"));
			s.add(new Flag(buffer, offset + 4, 4, "Charm flags",
					new String[] { "No flags set", "Turn hostile afterward", "Dire charm", "Controlled by cleric" }));
			break;

		case 0x6: // Charisma bonus (CGameEffectCHR)
		case 0xA: // Constitution bonus (CGameEffectCON)
		case 0x13: // Intelligence bonus (CGameEffectINT)
		case 0x1B: // Acid resistance bonus (CGameEffectResistAcid)
		case 0x1C: // Cold resistance bonus (CGameEffectResistCold)
		case 0x1D: // Electricity resistance bonus (CGameEffectResistElectricity)
		case 0x1E: // Fire resistance bonus (CGameEffectResistFire)
		case 0x1F: // Magic damage resistance bonus (CGameEffectResistMagic)
		case 0x21: // Save vs. death bonus (CGameEffectSaveVsDeath) / Fortitude save bonus
		case 0x22: // Save vs. wand bonus (CGameEffectSaveVsWands) / Reflex save bonus
		case 0x23: // Save vs. polymorph bonus (CGameEffectSaveVsPoly) / Will save bonus
		case 0x24: // Save vs. breath bonus (CGameEffectSaveVsBreath)
		case 0x25: // Save vs. spell bonus (CGameEffectSaveVsSpell)
		case 0x31: // Wisdom bonus (CGameEffectWIS)
		case 0x36: // Base THAC0 bonus (CGameEffectThac0) / Base attack bonus
		case 0x3B: // Stealth bonus / Move silently bonus (CGameEffectSkillStealth)
		case 0x54: // Magical fire resistance bonus (CGameEffectResistMagicFire)
		case 0x55: // Magical cold resistance bonus (CGameEffectResistMagicCold)
		case 0x56: // Slashing resistance bonus (CGameEffectResistSlashing)
		case 0x57: // Crushing resistance bonus (CGameEffectResistCrushing)
		case 0x58: // Piercing resistance bonus (CGameEffectResistPiercing)
		case 0x59: // Missile resistance bonus (CGameEffectResistMissile)
		case 0x5A: // Open locks bonus (CGameEffectSkillLockPicking)
		case 0x5B: // Find traps bonus (CGameEffectSkillTraps)
		case 0x5C: // Pick pockets bonus (CGameEffectSkillPickPocket)
		case 0x5D: // Fatigue bonus (CGameEffectFatigue)
		case 0x5E: // Intoxication bonus (CGameEffectIntoxication)
		case 0x5F: // Tracking bonus (CGameEffectSkillTracking)
		case 0x60: // Change level (CGameEffectLevel)
		case 0x61: // Exceptional strength bonus (CGameEffectSTRExtra)
		case 0x68: // XP bonus (CGameEffectXP)
		case 0x69: // Remove gold (CGameEffectGold)
		case 0x6A: // Morale break (CGameEffectMoraleBreak)
		case 0x6C: // Reputation bonus (CGameEffectReputation)
		case 0x7E: // Movement rate bonus (CGameEffectMovementRate)
		case 0xA7: // Missile THAC0 bonus (CGameEffectMissileTHAC0Bonus) / Missile attack bonus
		case 0xB0: // Movement rate bonus 2 (CGameEffectMovementRate) / Movement rate penalty
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Modifier type", s_inctype));
			break;

		case 0x7: // Set color (CGameEffectColorChange)
			s.add(new ColorValue(buffer, offset, 4, "Color"));
			s.add(new HashBitmap(buffer, offset + 4, 4, "Location", m_colorloc));
			break;

		case 0x8: // Set color glow solid (CGameEffectColorGlowSolid)
			s.add(new Unknown(buffer, offset, 1));
			s.add(new UnsignDecNumber(buffer, offset + 1, 1, "Red"));
			s.add(new UnsignDecNumber(buffer, offset + 2, 1, "Green"));
			s.add(new UnsignDecNumber(buffer, offset + 3, 1, "Blue"));
			s.add(new HashBitmap(buffer, offset + 4, 4, "Location", m_colorloc));
			break;

		case 0x9: // Set color glow pulse (CGameEffectColorGlowPulse)
			s.add(new Unknown(buffer, offset, 1));
			s.add(new UnsignDecNumber(buffer, offset + 1, 1, "Red"));
			s.add(new UnsignDecNumber(buffer, offset + 2, 1, "Green"));
			s.add(new UnsignDecNumber(buffer, offset + 3, 1, "Blue"));
			s.add(new HashBitmap(buffer, offset + 4, 2, "Location", m_colorloc));
			s.add(new DecNumber(buffer, offset + 6, 2, "Cycle speed"));
			break;

		case 0xD: // Kill target (CGameEffectDeath)
			s.add(new Bitmap(buffer, offset, 4, "Display text?", s_yesno));
			s.add(new Flag(buffer, offset + 4, 4, "Death type", new String[] { "Acid", "Burning", "Crushed", "Normal", "Exploding", "Stoned", "Freezing",
					"Exploding stoned", "Exploding freezing", "Electrified", "Disintegration" }));
			break;

		case 0x10: // Haste (CGameEffectHaste)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Haste type", new String[] { "Normal", "Improved", "Movement rate only" }));
			break;

		case 0x12: // Maximum HP bonus (CGameEffectHitPoints)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Modifier type", new String[] { "Increment", "Set", "Set % of", "Increment, don't update current HP",
					"Set, don't update current HP", "Set % of, don't update current HP" }));
			break;

		case 0x14: // Invisibility (CGameEffectInvisible)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Invisibility type", new String[] { "Normal", "Improved" }));
			break;

		case 0x15: // Lore bonus (CGameEffectLore) / Knowledge arcana
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Modifier type", new String[] { "Increment", "Set", "Mastery" }));
			break;

		case 0x29: // Sparkle (CGameEffectSparkle)
			s.add(new Bitmap(buffer, offset, 4, "Color", new String[] { "", "Black", "Blue", "Chromatic", "Gold", "Green", "Purple", "Red", "White", "Ice",
					"Stone", "Magenta", "Orange" }));
			s.add(new Bitmap(buffer, offset + 4, 4, "Particle effect", new String[] { "", "Explosion", "Swirl", "Shower" }));
			break;

		case 0x2A: // Bonus wizard spells (CGameEffectSpellMemorizationMage)
			s.add(new DecNumber(buffer, offset, 4, "# spells to add"));
			s.add(new Flag(buffer, offset + 4, 4, "Spell levels", new String[] { "Double spells", "Level 1", "Level 2", "Level 3", "Level 4", "Level 5",
					"Level 6", "Level 7", "Level 8", "Level 9" }));
			break;

		case 0x32: // Character color pulse (CGameEffectSingleColorPulseAll)
			s.add(new Unknown(buffer, offset, 1));
			s.add(new UnsignDecNumber(buffer, offset + 1, 1, "Red"));
			s.add(new UnsignDecNumber(buffer, offset + 2, 1, "Green"));
			s.add(new UnsignDecNumber(buffer, offset + 3, 1, "Blue"));
			s.add(new Unknown(buffer, offset + 4, 2));
			s.add(new DecNumber(buffer, offset + 6, 2, "Cycle speed"));
			break;

		case 0x33: // Character tint solid (CGameEffectColorTintSolid)
		case 0x34: // Character tint bright (CGameEffectColorLightSolid)
			s.add(new Unknown(buffer, offset, 1));
			s.add(new UnsignDecNumber(buffer, offset + 1, 1, "Red"));
			s.add(new UnsignDecNumber(buffer, offset + 2, 1, "Green"));
			s.add(new UnsignDecNumber(buffer, offset + 3, 1, "Blue"));
			s.add(new HashBitmap(buffer, offset + 4, 2, "Location", m_colorloc));
			s.add(new Unknown(buffer, offset + 6, 2));
			break;

		case 0x35: // Animation change (CGameEffectAnimationChange)
			s.add(new IdsBitmap(buffer, offset, 4, "Morph into", "ANIMATE.IDS"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Morph type", new String[] { "Temporary change", "Remove temporary change", "Permanent change" }));
			break;

		case 0x37: // Slay (CGameEffectSlay)
		case 0x64: // Protection from creature type (CGameEffectProtectionFromCreature)
		case 0x6D: // Paralyze (CGameEffectHoldCreature)
		case 0xAF: // Hold creature (CGameEffectHoldCreatureSpell)
		case 0xB2: // THAC0 vs. type bonus (CGameEffectSelectiveToHitBonus)
		case 0xB3: // Damage vs. type bonus (CGameEffectSelectiveDamageBonus)
		case 0xB9: // Hold creature 2 (CGameEffectHoldCreatureSpell)
			s.add(new IDSTargetEffect(buffer, offset));
			break;

		case 0x39: // Change alignment (CGameEffectAlignmentChange)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new IdsBitmap(buffer, offset + 4, 4, "Alignment", "ALIGN.IDS"));
			break;

		case 0x3A: // Dispel effects (CGameEffectDispelEffects)
			s.add(new DecNumber(buffer, offset, 4, "Level"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Dispel type", new String[] { "Always dispel", "Use caster level", "Use specific level" }));
			break;

		case 0x3E: // Bonus priest spells (CGameEffectSpellMemorizationCleric)
			s.add(new DecNumber(buffer, offset, 4, "# spells to add"));
			s.add(new Flag(buffer, offset + 4, 4, "Spell levels", new String[] { "Double spells", "Level 1", "Level 2", "Level 3", "Level 4", "Level 5",
					"Level 6", "Level 7" }));
			break;

		case 0x44: // Unsummon creature (CGameEffectUnsummon)
			s.add(new Bitmap(buffer, offset, 4, "Display text?", s_noyes));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x47: // Change gender (CGameEffectSexChange)
			s.add(new IdsBitmap(buffer, offset, 4, "Gender", "GENDER.IDS"));
			s.add(new Bitmap(buffer, offset + 4, 4, "How?", new String[] { "Reverse gender", "Set gender" }));
			break;

		case 0x48: // Change AI type (CGameEffectAIChange)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "AI type", new String[] { "Allegiance", "General", "Race", "Class", "Specifics", "Gender", "Alignment" }));
			break;

		case 0x53: // Immunity to projectile (CGameEffectImmunityToProjectile)
			s.add(new Unknown(buffer, offset, 4));
			if (Keyfile.getInstance().resourceExists("PROJECTL.IDS"))
				s.add(new IdsBitmap(buffer, offset + 4, 4, "Projectile", "PROJECTL.IDS"));
			else
				s.add(new DecNumber(buffer, offset + 4, 4, "Projectile"));
			break;

		case 0x63: // Modify duration (CGameEffectDurationCasting)
			s.add(new DecNumber(buffer, offset, 4, "Percentage"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell class", new String[] { "Wizard", "Priest" }));
			break;

		case 0x65: // Immunity to effect (CGameEffectImmunityToEffect)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Effect", s_effname));
			break;

		case 0x66: // Immunity to spell level (CGameEffectImmunityToSpellLevel)
			s.add(new DecNumber(buffer, offset, 4, "Spell level"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x67: // Change name (CGameEffectName)
			s.add(new StringRef(buffer, offset, "Name"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x6B: // Change portrait (CGameEffectPortrait)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Which portrait?", new String[] { "Small", "Large" }));
			restype = "BMP";
			break;

		case 0x6F: // Create weapon (CGameEffectCreateWeapon)
		case 0x7A: // Create inventory item (CGameEffectCreateItem)
			s.add(new DecNumber(buffer, offset, 4, "# to create"));
			s.add(new Unknown(buffer, offset + 4, 4));
			restype = "ITM";
			break;

		case 0x70: // Remove item (CGameEffectDestroyWeapon)
		case 0x7B: // Remove inventory item (CGameEffectDestroyItem)
			restype = "ITM";
			break;

		case 0x73: // Detect alignment (CGameEffectDetectAlignment)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Alignment mask", new String[] { "Evil", "Neutral", "Good" }));
			break;

		case 0x78: // Immunity to weapons (CGameEffectImmuneToWeapon)
			s.add(new DecNumber(buffer, offset, 4, "Maximum enchantment"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Weapon type", new String[] { "Enchanted", "Magical", "Non-magical", "Silver", "Non-silver",
					"Non-silver, non-magical", "Two-handed", "One-handed", "Cursed", "Non-cursed", "Cold iron", "Non-cold-iron" }));
			break;

		case 0x7F: // Summon monsters (CGameEffectRandomSummon)
			s.add(new DecNumber(buffer, offset, 4, "Total XP"));
			s.add(new Bitmap(buffer, offset + 4, 4, "From 2DA file", new String[] { "Monsum01 (ally)", "Monsum02 (ally)", "Monsum03 (ally)", "Anisum01 (ally)",
					"Anisum02 (ally)", "Monsum01 (enemy)", "Monsum02 (enemy)", "Monsum03 (enemy)", "Anisum01 (enemy)", "Anisum02 (enemy)" }));
			restype = "2DA";
			break;

		case 0x81: // Aid (non-cumulative) (CGameEffectNon_CumulativeAid)
			s.add(new DecNumber(buffer, offset, 4, "Amount"));
			s.add(new DecNumber(buffer, offset + 4, 4, "HP bonus"));
			break;

		case 0x82: // Bless (non-cumulative) (CGameEffectNon_CumulativeBless)
		case 0x84: // Draw upon holy might (non-cumulative) (CGameEffectNon_CumulativeDrawUponHolyMight)
		case 0x85: // Luck (non-cumulative) (CGameEffectNon_CumulativeLuck)
		case 0x89: // Bad chant (non-cumulative) (CGameEffectNon_CumulativeChantBad)
		case 0xAD: // Poison resistance bonus (CGameEffectResistanceToPoison)
			s.add(new DecNumber(buffer, offset, 4, "Amount"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x87: // Polymorph (CGameEffectPolymorph)
			s.add(new IdsBitmap(buffer, offset, 4, "Animation", "ANIMATE.IDS"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Polymorph type", new String[] { "Change into", "Appearance only", "Appearance only", "Appearance only" }));
			restype = "CRE";
			break;

		case 0x8A: // Set animation sequence (CGameEffectSetSequence)
			s.add(new Unknown(buffer, offset, 4));
			if (Keyfile.getInstance().resourceExists("ANIMSTAT.IDS"))
				s.add(new IdsBitmap(buffer, offset + 4, 4, "Sequence", "ANIMSTAT.IDS"));
			else if (Keyfile.getInstance().resourceExists("SEQ.IDS"))
				s.add(new IdsBitmap(buffer, offset + 4, 4, "Sequence", "SEQ.IDS"));
			else {
				String s_seqtype[] = { "", "Lay down (short)", "Move hands (short)", "Move hands (long)", "Move shoulder (short)", "Move shoulder (long)",
						"Lay down (long)", "Breathe rapidly (short)", "Breathe rapidly (long)" };
				s.add(new Bitmap(buffer, offset + 4, 4, "Sequence", s_seqtype));
			}
			break;

		case 0x8B: // Display string (CGameEffectDisplayString)
			s.add(new StringRef(buffer, offset, "String"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x8C: // Casting glow (CGameEffectCastingGlow)
			final LongIntegerHashMap<String> m_castglow = new LongIntegerHashMap<String>();
			m_castglow.put(9L, "Necromancy");
			m_castglow.put(10L, "Alteration");
			m_castglow.put(11L, "Enchantment");
			m_castglow.put(12L, "Abjuration");
			m_castglow.put(13L, "Illusion");
			m_castglow.put(14L, "Conjuration");
			m_castglow.put(15L, "Invocation");
			m_castglow.put(16L, "Divination");
			s.add(new Unknown(buffer, offset, 4));
			s.add(new HashBitmap(buffer, offset + 4, 4, "Glow", m_castglow));
			break;

		case 0x8D: // Lighting effects (CGameEffectVisualSpellHit) / Visual spell hit
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Effect", new String[] { "Necromancy air", "Necromancy earth", "Necromancy water", "", "Alteration air",
					"Alteration earth", "Alteration water", "", "Enchantment air", "Enchantment earth", "Enchantment water", "", "Abjuration air",
					"Abjuration earth", "Abjuration water", "", "Illusion air", "Illusion earth", "Illusion water", "", "Conjure air", "Conjure earth",
					"Conjure water", "", "Invocation air", "Invocation earth", "Invocation water", "", "Divination air", "Divination earth",
					"Divination water", "", "Mushroom fire", "Mushroom gray", "Mushroom green", "Shaft fire", "Shaft light", "Shaft white", "Hit door",
					"Hit finger of death" }));
			break;

		case 0x8E: // Display portrait icon (CGameEffectPortraitIcon)
		case 0xA9: // Prevent portrait icon (CGameEffectImmunityToPortraitIcon)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Icon", s_poricon));
			break;

		case 0x8F: // Create item in slot (CGameEffectReplaceItem)
			s.add(new IdsBitmap(buffer, offset, 4, "Slot", "SLOTS.IDS"));
			s.add(new Unknown(buffer, offset + 4, 4));
			restype = "ITM";
			break;

		case 0x92: // Cast spell (CGameEffectCastSpell)
		case 0x94: // Cast spell at point (CGameEffectCastSpellPoint)
			s.add(new DecNumber(buffer, offset, 4, "Cast at level"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Cast instantly?", s_noyes));
			restype = "SPL";
			break;

		case 0x93: // Learn spell (CGameEffectLearnSpell)
		case 0xAB: // Give innate ability (CGameEffectAddInnateAbility)
		case 0xAC: // Remove spell (CGameEffectRemoveInnateAbility) / Remove innate ability
			restype = "SPL";
			break;

		case 0x97: // Replace self (CGameEffectReplaceSelf)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Replacement method", new String[] { "Remove silently", "Remove via chunked death",
					"Remove via normal death", "Don't remove" }));
			restype = "CRE";
			break;

		case 0x98: // Play movie (CGameEffectPlayMovie)
			// FIX ME if (gameid == ResourceFactory.ID_BGEE || gameid == ResourceFactory.ID_BG2EE) {
			// restype = "WBM";
			// } else {
			// restype = "MVE";
			// }
			restype = "WBM";
			break;

		case 0x9F: // Mirror image effect (CGameEffectMirrorImageRun)
			s.add(new DecNumber(buffer, offset, 4, "# images"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xA6: // Magic resistance bonus (CGameEffectResistanceToMagic)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Modifier type", new String[] { "Increment", "Set" }));
			break;

		case 0xAA: // Play damage animation (CGameEffectDamageVisualEffect)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Animation", new String[] { "Blood (behind)", "Blood (front)", "Blood (left)", "Blood (right)", "Fire 1",
					"Fire 2", "Fire 3", "Electricity 1", "Electricity 2", "Electricity 3" }));
			break;

		case 0xAE: // Play sound (CGameEffectPlaySound)
			restype = "WAV";
			break;

		case 0xB1: // Use EFF file (CGameEffectApplyEffect)
			s.add(new IDSTargetEffect(buffer, offset));
			restype = "EFF";
			break;

		case 0xB4: // Disallow item (CGameEffectRestrictEquipItem)
			s.add(new StringRef(buffer, offset, "String"));
			s.add(new Unknown(buffer, offset + 4, 4));
			restype = "ITM";
			break;
		}

		return restype;
	}

	private String makeEffectParamsBG2(Datatype parent, byte buffer[], int offset, List<StructEntry> s, int effectType, boolean isV1) {
		String restype = null;
		switch (effectType) {
		case 0x0: // AC bonus (CGameEffectAC)
			s.add(new DecNumber(buffer, offset, 4, "AC value"));
			s.add(new Flag(buffer, offset + 4, 4, "Bonus to", s_actype));
			break;

		case 0x1: // Modify attacks per round (CGameEffectAttackNo)
			s.add(new Bitmap(buffer, offset, 4, "Value", new String[] { "0 attacks per round", "1 attack per round", "2 attacks per round",
					"3 attacks per round", "4 attacks per round", "5 attacks per round", "1 attack per 2 rounds", "3 attacks per 2 rounds",
					"5 attacks per 2 rounds", "7 attacks per 2 rounds", "9 attacks per 2 rounds" }));
			s.add(new Bitmap(buffer, offset + 4, 4, "Modifier type", s_inctype));
			break;

		case 0xC: // Damage (CGameEffectDamage)
			s.add(new DecNumber(buffer, offset, 4, "Amount"));
			s.add(new Bitmap(buffer, offset + 4, 2, "Mode", new String[] { "Normal", "Set to value", "Set to %", "Percentage" }));
			s.add(new IdsBitmap(buffer, offset + 6, 2, "Damage type", "DAMAGES.IDS"));
			break;

		case 0x11: // Current HP bonus (CGameEffectHeal)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 2, "Modifier type", new String[] { "Increment", "Set", "Increment % of" }));
			s.add(new Flag(buffer, offset + 6, 2, "Heal flags", new String[] { "No flags set", "Raise dead", "Remove limited effects" }));
			break;

		case 0xF: // Dexterity bonus (CGameEffectDEX)
		case 0x16: // Luck bonus (CGameEffectLuck)
		case 0x2C: // Strength bonus (CGameEffectSTR)
		case 0x49: // Attack damage bonus (CGameEffectDamageMod)
		case 0x106: // Visual range bonus (CGameEffectVisualRange)
		case 0x107: // Backstab bonus (CGameEffectBackStabMod)
		case 0x113: // Hide in shadows bonus (CGameEffectHideInShadows)
		case 0x114: // Detect illusions bonus (CGameEffectDetectIllusion)
		case 0x115: // Set traps bonus (CGameEffectSetTrap)
		case 0x116: // THAC0 bonus (CGameEffectHitMod)
		case 0x119: // Wild surge bonus (CGameEffectSurgeMod)
		case 0x11C: // Melee THAC0 bonus (CGameEffectMeleeTHAC0Bonus)
		case 0x11D: // Melee weapon damage bonus (CGameEffectMeleeDamageBonus)
		case 0x11E: // Missile weapon damage bonus (CGameEffectMissileDamageBonus)
		case 0x120: // Fist THAC0 bonus (CGameEffectFistTHAC0Bonus)
		case 0x121: // Fist damage bonus (CGameEffectFistDamageBonus)
		case 0x131: // Off-hand THAC0 bonus (CGameEffectThac0Left)
		case 0x132: // Main hand THAC0 bonus (CGameEffectThac0Right)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Modifier type", s_inctype));
			break;

		case 0x3C: // Casting failure (CGameEffectCastingFailure)
			s.add(new DecNumber(buffer, offset, 4, "Amount"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Failure type", new String[] { "Wizard", "Priest", "Innate", "Wizard (dead magic)", "Priest (dead magic)",
					"Innate (dead magic)" }));
			break;

		case 0x42: // Translucency (CGameEffectTranslucent)
			s.add(new DecNumber(buffer, offset, 4, "Fade amount"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x43: // Summon creature (CGameEffectSummon)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Allegiance", new String[] { "Match target", "Match target", "From CRE file", "Match target",
					"From CRE file", "Hostile" }));
			restype = "CRE";
			break;

		case 0x4E: // Disease (CGameEffectDisease)
			s.add(new DecNumber(buffer, offset, 4, "Amount"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Disease type", new String[] { "1 damage per second", "1 damage per second", "Amount damage per second",
					"1 damage per amount seconds", "Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma", "Slow target" }));
			break;

		case 0x52: // Set AI script (CGameEffectSetAIScript)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new IdsBitmap(buffer, offset + 4, 4, "Script level", "SCRLEV.IDS"));
			restype = "BCS";
			break;

		case 0x62: // Regeneration (CGameEffectRegeneration)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Regeneration type", new String[] { "1 HP per second", "1 HP per second", "Amount HP per second",
					"1 HP per amount seconds", "Variable per amount seconds" }));
			break;

		case 0x83: // Chant (non-cumulative) (CGameEffectNon_CumulativeChant)
		case 0xBD: // Increase casting speed factor (CGameEffectMentalSpeed)
		case 0xBE: // Increase attack speed factor (CGameEffectPhysicalSpeed)
		case 0x12D: // Critical threat range bonus (CGameEffectCriticalHitBonus)
			s.add(new DecNumber(buffer, offset, 4, "Amount"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x90: // Disable button (CGameEffectDisableButton)
		case 0x117: // Enable button (CGameEffectEnableButton)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Button", s_button));
			break;

		case 0x91: // Disable spellcasting (CGameEffectDisableSpellType)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell class", new String[] { "Wizard", "Priest", "Innate" }));
			break;

		case 0xB8: // No collision detection (CGameEffectSetDoNotJump)
		case 0xBC: // Increase spells cast per round (CGameEffectAuraCleansing)
		case 0xC1: // Invisibility detection (CGameEffectSeeInvisible)
		case 0xC2: // Ignore dialogue pause (CGameEffectIgnoreDialogPause)
		case 0xF5: // Unknown (F5) (CGameEffectCheckForBerserk)
		case 0xF6: // Berserk effect (CGameEffectBerserkStage1)
		case 0xF7: // Attack nearest creature (CGameEffectBerserkStage2)
		case 0x123: // Disable visual effects (CGameEffectPreventSpellProtectionEffects)
		case 0x124: // Immunity to backstab (CGameEffectImmunityToBackstab)
		case 0x125: // Set persistent AI (CGameEffectPreventAISlowDown)
		case 0x127: // Disable permanent death (CGameEffectNoPermanentDeath)
		case 0x129: // Immunity to turn undead (CGameEffectImmunityToTurnUndead)
		case 0x12C: // Modify collision behavior (CGameEffectNPCBump)
		case 0x12E: // Can use any item (CGameEffectUseAnyItem)
		case 0x12F: // Backstab every hit (CGameEffectAssassination)
		case 0x134: // Immunity to tracking (CGameEffectImmuneToTracking)
		case 0x136: // Immunity to time stop (CGameEffectImmunityToTimeStop)
		case 0x13B: // Remove animation (CGameEffectDoNotDraw)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new DecNumber(buffer, offset + 4, 4, "Stat value"));
			break;

		case 0xBA: // Move creature (CGameEffectJumpToArea)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Orientation", Actor.s_orientation));
			restype = "ARE";
			break;

		case 0xBB: // Set local variable (CGameEffectSetLocalVariable)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Unknown(buffer, offset + 4, 4));
			restype = "String";
			break;

		case 0xBF: // Casting level bonus (CGameEffectCastingLevelBonus)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell class", new String[] { "Wizard", "Priest" }));
			break;

		case 0xC3: // Drain HP on death (CGameEffectHitPointsOnDeath)
			s.add(new DecNumber(buffer, offset, 4, "HP amount"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xC5: // Physical mirror (CGameEffectBounceProjectile)
			s.add(new Unknown(buffer, offset, 4));
			if (Keyfile.getInstance().resourceExists("PROJECTL.IDS"))
				s.add(new IdsBitmap(buffer, offset + 4, 4, "Projectile", "PROJECTL.IDS"));
			else
				s.add(new DecNumber(buffer, offset + 4, 4, "Projectile"));
			break;

		case 0xC6: // Reflect specified effect (CGameEffectBounceEffect)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Effect", s_effname));
			break;

		case 0xC7: // Reflect spell level (CGameEffectBounceLevel)
			s.add(new DecNumber(buffer, offset, 4, "Spell level"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xC8: // Spell turning (CGameEffectBounceLevelDecrement)
		case 0xC9: // Spell deflection (CGameEffectImmunityLevelDecrement)
			s.add(new DecNumber(buffer, offset, 4, "# levels"));
			s.add(new DecNumber(buffer, offset + 4, 4, "Spell level"));
			break;

		case 0xCA: // Reflect spell school (CGameEffectBounceSchool)
		case 0xCC: // Protection from spell school (CGameEffectImmunitySchool)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell school", s_school));
			break;

		case 0xCB: // Reflect spell type (CGameEffectBounceSecondaryType)
		case 0xCD: // Protection from spell type (CGameEffectImmunitySecondaryType)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell type", SplResource.s_category));
			break;

		case 0xCE: // Protection from spell (CGameEffectImmunitySpell)
			s.add(new StringRef(buffer, offset, "String"));
			s.add(new Unknown(buffer, offset + 4, 4));
			restype = "SPL";
			break;

		case 0xCF: // Reflect specified spell (CGameEffectBounceSpell)
		case 0xFB: // Change bard song (CGameEffectBardSong)
		case 0xFC: // Set trap (CGameEffectSetSnare)
		case 0x100: // Spell sequencer (CGameEffectSequencerInstance)
		case 0x102: // Activate spell sequencer (CGameEffectSequencerFire)
		case 0x10A: // Remove protection from spell (CGameEffectRemoveSpellImmunity)
		case 0x139: // High-level ability (CGameEffectHighLevelAbility)
			restype = "SPL";
			break;

		case 0xD0: // Minimum HP (CGameEffectMinHitPoints)
			s.add(new DecNumber(buffer, offset, 4, "HP amount"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xD6: // Select spell (CGameEffectSecondaryCastList)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Show", new String[] { "All spells", "Known spells" }));
			restype = "2DA";
			break;

		case 0xD7: // Play visual effect (CGameEffectVisualEffect)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Play where?", new String[] { "Over target (unattached)", "Over target (attached)", "At target point" }));
			restype = "VEF:VVC:BAM";
			break;

		case 0xD8: // Level drain (CGameEffectLevelDrain)
			s.add(new DecNumber(buffer, offset, 4, "# levels"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xDA: // Stoneskin effect (CGameEffectStoneSkins)
		case 0x13A: // Stoneskin protection (CGameEffectStoneSkinsGolem)
			s.add(new DecNumber(buffer, offset, 4, "# skins"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xDB: // Attack roll penalty (CGameEffectProtectionCircle)
		case 0xEE: // Disintegrate (CGameEffectDisintegrate)
			s.add(new IDSTargetEffect(buffer, offset));
			break;

		case 0xDC: // Remove spell school protections (CGameEffectDispelSchool)
		case 0xE5: // Remove protection by school (CGameEffectDispelSchoolOne)
			s.add(new DecNumber(buffer, offset, 4, "Maximum level"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell school", s_school));
			break;

		case 0xDD: // Remove spell type protections (CGameEffectDispelSecondaryType)
		case 0xE6: // Remove protection by type (CGameEffectDispelSecondaryTypeOne)
			s.add(new DecNumber(buffer, offset, 4, "Maximum level"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell type", SplResource.s_category));
			break;

		case 0xDE: // Teleport field (CGameEffectRandomTeleport)
			s.add(new DecNumber(buffer, offset, 4, "Maximum range"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xDF: // Spell school deflection (CGameEffectImmunitySchoolDecrement)
		case 0xE3: // Spell school turning (CGameEffectBounceSchoolDecrement)
			s.add(new DecNumber(buffer, offset, 4, "# levels"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell school", s_school));
			break;

		case 0xE2: // Spell type deflection (CGameEffectImmunitySecondaryTypeDecrement)
		case 0xE4: // Spell type turning (CGameEffectBounceSecondaryTypeDecrement)
			s.add(new DecNumber(buffer, offset, 4, "# levels"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell type", SplResource.s_category));
			break;

		case 0xE8: // Cast spell on condition (CGameEffectContingencyInstance)
		{
			s.add(new Bitmap(buffer, offset, 4, "Target", new String[] { "Caster", "Last hit by", "Nearest enemy" }));
			if (ResourceFactory.getInstance().isEnhancedEdition()) {
				Bitmap item = new Bitmap(buffer, offset + 4, 4, "Condition", new String[] { "Target hit", "Enemy sighted", "HP below 50%", "HP below 25%",
						"HP below 10%", "If helpless", "If poisoned", "Every round when attacked", "Target in range 4'", "Target in range 10'", "Every round",
						"Took damage", "Actor killed", "Time of day" });
				s.add(item);

			} else {
				s.add(new Bitmap(buffer, offset + 4, 4, "Condition", new String[] { "Target hit", "Enemy sighted", "HP below 50%", "HP below 25%",
						"HP below 10%", "If helpless", "If poisoned", "Every round when attacked", "Target in range 4'", "Target in range 10'", "Every round",
						"Took damage" }));
			}
			restype = "SPL";
			break;
		}

		case 0xE9: // Modify proficiencies (CGameEffectProficiency)
			s.add(new DecNumber(buffer, offset, 4, "# stars"));
			s.add(new IdsBitmap(buffer, offset + 4, 4, "Proficiency", "STATS.IDS"));
			break;

		case 0xEA: // Create contingency (CGameEffectContingencyStart)
			s.add(new DecNumber(buffer, offset, 4, "Maximum spell level"));
			s.add(new DecNumber(buffer, offset + 4, 2, "# spells"));
			s.add(new Bitmap(buffer, offset + 6, 2, "Trigger type", new String[] { "Chain contingency", "Contingency", "Spell sequencer" }));
			break;

		case 0xEB: // Wing buffet (CGameEffectPushPull)
			s.add(new DecNumber(buffer, offset, 4, "Strength"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Direction", new String[] { "", "Away from target point", "Away from source", "Toward target point",
					"Toward source" }));
			break;

		case 0xEC: // Project image (CGameEffectCopySelf)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Image type", new String[] { "", "Mislead", "Project image", "Simulacrum" }));
			break;

		case 0xED: // Set image type (CGameEffectPuppetMaster)
			s.add(new DecNumber(buffer, offset, 4, "Puppet master"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Puppet type", new String[] { "", "Mislead", "Project image", "Simulacrum" }));
			break;

		case 0xEF: // Farsight (CGameEffectClairvoyance)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Can view unexplored?", s_noyes));
			break;

		case 0xF0: // Remove portrait icon (CGameEffectRemovePortraitIcon)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Icon", s_poricon));
			break;

		case 0xF1: // Control creature (CGameEffectCharm)
			s.add(new IdsBitmap(buffer, offset, 4, "Creature type", "GENERAL.IDS"));
			s.add(new Flag(buffer, offset + 4, 4, "Charm flags",
					new String[] { "No flags set", "Turn hostile afterward", "Dire charm", "Controlled by cleric" }));
			break;

		case 0xF3: // Drain item charges (CGameEffectDrainChargeFromAllItems)
			s.add(new Bitmap(buffer, offset, 4, "Include weapons?", s_noyes));
			if (ResourceFactory.getInstance().isEnhancedEdition()) {
				s.add(new DecNumber(buffer, offset + 4, 4, "# to drain"));
			} else {
				s.add(new Unknown(buffer, offset + 4, 4));
			}
			break;

		case 0xF4: // Drain wizard spells (CGameEffectRemoveRandomSpell)
			s.add(new DecNumber(buffer, offset, 4, "# spells"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xF8: // Melee hit effect (CGameEffectMeleeEffect)
		case 0xF9: // Ranged hit effect (CGameEffectRangeEffect)
			restype = "EFF";
			break;

		case 0xFA: // Maximum damage each hit (CGameEffectDamageLuck)
			s.add(new DecNumber(buffer, offset, 4, "Stat value"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xFD: // Set automap note (CGameAddMapNote)
		case 0xFE: // Remove automap note (CGameRemoveMapNote)
		case 0x10B: // Disable display string (CGameEffectImmunityToDisplayString)
			s.add(new StringRef(buffer, offset, "String"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0xFF: // Create item (days) (CGameEffectCreateItem)
			s.add(new DecNumber(buffer, offset, 4, "# items in stack"));
			s.add(new Unknown(buffer, offset + 4, 4));
			restype = "ITM";
			break;

		case 0x101: // Create spell sequencer (CGameEffectSequencerStart)
			s.add(new DecNumber(buffer, offset, 4, "Maximum level"));
			s.add(new DecNumber(buffer, offset + 4, 4, "# spells"));
			break;

		case 0x103: // Spell trap (CGameEffectSpellTrapLevelDecrement)
			s.add(new DecNumber(buffer, offset, 4, "# spells"));
			s.add(new DecNumber(buffer, offset + 4, 4, "Spell level"));
			break;

		case 0x105: // Wondrous recall (CGameEffectRememorizeSpell)
			s.add(new DecNumber(buffer, offset, 4, "Spell level"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Spell class", new String[] { "Wizard", "Priest" }));
			break;

		case 0x108: // Drop item (CGameEffectRandomDrop)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Only quick weapons?", s_noyes));
			break;

		case 0x109: // Set global variable (CGameEffectSetGlobal)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Modifier type", new String[] { "Set", "Increment" }));
			restype = "String";
			break;

		case 0x10D: // Shake screen (CGameEffectScreenShake)
			s.add(new DecNumber(buffer, offset, 4, "Strength"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x110: // Use EFF file on condition (CGameEffectRepeatingApplyEffect)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Frequency", new String[] { "Once per second", "", "Value per second", "Once per value seconds",
					"Variable per value rounds" }));
			restype = "EFF";
			break;

		case 0x118: // Wild magic (CGameEffectForceSurge)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Affect", new String[] { "", "Next spell", "Every spell" }));
			break;

		case 0x11A: // Modify script state (CGameEffectScriptingState)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new IdsBitmap(buffer, offset + 4, 4, "State", "STATS.IDS", 156));
			break;

		case 0x11B: // Use EFF file as curse (CGameEffectCurseApplyEffect)
			s.add(new IDSTargetEffect(buffer, offset));
			restype = "EFF";
			break;

		case 0x122: // Change title (CGameEffectClassStringOverride)
			s.add(new StringRef(buffer, offset, "Title"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Change where?", new String[] { "Records screen", "Class name" }));
			break;

		case 0x126: // Set existence delay (CGameEffectExistanceDelayOverride)
			s.add(new DecNumber(buffer, offset, 4, "Interval override"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x128: // Immunity to specific animation (CGameEffectImmunityToVisualEffect)
			restype = "VEF:VVC:BAM";
			break;

		case 0x133: // Tracking (CGameEffectTracking)
			s.add(new DecNumber(buffer, offset, 4, "Range"));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x135: // Set variable (CGameEffectSetLocalExternal)
			s.add(new DecNumber(buffer, offset, 4, "Value"));
			s.add(new Bitmap(buffer, offset + 4, 4, "Global or local?", new String[] { "Both", "Local only" }));
			restype = "String";
			break;

		case 0x13D: // Haste 2 (CGameEffectHaste2)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Bitmap(buffer, offset + 4, 4, "Haste type", new String[] { "Normal", "Improved", "Movement rate only" }));
			break;

		case 0x13F: // Restrict item (BGEE)
		{
			int param2 = DynamicArray.getInt(buffer, offset + 4);
			if (param2 >= 2 && param2 < 10) {
				s.add(new IdsBitmap(buffer, offset, 4, "IDS entry", m_itemids.get((long) param2)));
			} else if (param2 == 10) {
				s.add(new StringRef(buffer, offset, "Actor name"));
			} else {
				s.add(new DecNumber(buffer, offset, 4, "Unused"));
			}
			HashBitmapEx idsFile = new HashBitmapEx(buffer, offset + 4, 4, "IDS file", m_itemids);
			s.add(idsFile);
			break;
		}

		case 0x140: // Change weather (BGEE)
			s.add(new Bitmap(buffer, offset, 4, "Type", new String[] { "", "Rain", "Snow", "Nothing" }));
			s.add(new Unknown(buffer, offset + 4, 4));
			break;

		case 0x141: // Remove effects by resource (BGEE)
			s.add(new Unknown(buffer, offset, 4));
			s.add(new Unknown(buffer, offset + 4, 4));
			restype = "SPL";
			break;
		}

		return restype;
	}

	private int makeEffectCommon1(byte[] buffer, int offset, List<StructEntry> s, boolean isV1) {
		if (isV1) {
			s.add(new Bitmap(buffer, offset, 1, "Timing mode", s_duration));
			s.add(new Bitmap(buffer, offset + 1, 1, "Dispel/Resistance", EffectType.s_dispel));
			offset += 2;
		} else {
			s.add(new Bitmap(buffer, offset, 4, "Timing mode", s_duration));
			offset += 4;
		}

		s.add(new DecNumber(buffer, offset, 4, "Duration"));
		offset += 4;

		if (isV1) {
			s.add(new DecNumber(buffer, offset, 1, "Probability 1"));
			s.add(new DecNumber(buffer, offset + 1, 1, "Probability 2"));
			offset += 2;
		} else {
			s.add(new DecNumber(buffer, offset, 2, "Probability 1"));
			s.add(new DecNumber(buffer, offset + 2, 2, "Probability 2"));
			offset += 4;
		}

		return offset;
	}

	private int makeEffectResource(Datatype parent, byte buffer[], int offset, List<StructEntry> s, int effectType, String resourceType, int param1, int param2) {
		if (resourceType == null) {
			if (effectType == 0x13F && param2 == 11) {
				s.add(new TextString(buffer, offset, 8, "Script name"));
			} else {
				s.add(new Unknown(buffer, offset, 8, "Unused"));
			}
		} else if (resourceType.equalsIgnoreCase("String")) {
			s.add(new TextString(buffer, offset, 8, "String"));
		} else {
			s.add(new ResourceRef(buffer, offset, "Resource", resourceType.split(":")));
		}
		offset += 8;

		return offset;
	}

	private int makeEffectCommon2(byte[] buffer, int offset, List<StructEntry> s, boolean isV1) {
		if (isV1) {
			s.add(new DecNumber(buffer, offset, 4, "# dice thrown/maximum level"));
			s.add(new DecNumber(buffer, offset + 4, 4, "Dice size/minimum level"));
			s.add(new Flag(buffer, offset + 8, 4, "Save type", s_savetype));
			s.add(new DecNumber(buffer, offset + 12, 4, "Save bonus"));
		} else {
			s.add(new DecNumber(buffer, offset, 4, "# dice thrown"));
			s.add(new DecNumber(buffer, offset + 4, 4, "Dice size"));
			s.add(new Flag(buffer, offset + 8, 4, "Save type", s_savetype));
			s.add(new DecNumber(buffer, offset + 12, 4, "Save bonus"));
		}
		offset += 16;

		return offset;
	}

	private int makeEffectParam25(Datatype parent, byte buffer[], int offset, List<StructEntry> s, int effectType, String resourceType, int param1, int param2) {
		switch (effectType) {
		case 0xE8: // Cast spell on condition (CGameEffectContingencyInstance)
			if (param2 == 13) {
				s.add(new IdsBitmap(buffer, offset, 4, "Time of day", "TIMEODAY.IDS"));
			} else {
				s.add(new DecNumber(buffer, offset, 4, "Unused"));
			}
			break;
		case 0x13F: // Restrict item (BGEE)
			s.add(new StringRef(buffer, offset, "Description note"));
			break;
		default:
			s.add(new DecNumber(buffer, offset, 4, "Unused"));
			break;
		}
		offset += 4;

		return offset;
	}

}
