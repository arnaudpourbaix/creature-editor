package com.pourbaix.infinity.resource.cre;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.Effect;
import com.pourbaix.infinity.resource.Effect2;
import com.pourbaix.infinity.resource.StructEntry;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.ColorValue;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.Flag;
import com.pourbaix.infinity.resource.datatype.HashBitmap;
import com.pourbaix.infinity.resource.datatype.HexNumber;
import com.pourbaix.infinity.resource.datatype.IdsBitmap;
import com.pourbaix.infinity.resource.datatype.IdsFlag;
import com.pourbaix.infinity.resource.datatype.Kit2daBitmap;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.SectionCount;
import com.pourbaix.infinity.resource.datatype.SectionOffset;
import com.pourbaix.infinity.resource.datatype.StringRef;
import com.pourbaix.infinity.resource.datatype.TextString;
import com.pourbaix.infinity.resource.datatype.Unknown;
import com.pourbaix.infinity.resource.datatype.UnsignDecNumber;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.IdsMapCache;
import com.pourbaix.infinity.util.IdsMapEntry;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public final class CreResource extends AbstractStruct {

	private static final LongIntegerHashMap<String> m_magetype = new LongIntegerHashMap<String>();
	private static final LongIntegerHashMap<String> m_colorPlacement = new LongIntegerHashMap<String>();
	private static final String s_flag[] = { "No flags set", "Identified", "No corpse", "Permanent corpse", "Original class: Fighter", "Original class: Mage",
			"Original class: Cleric", "Original class: Thief", "Original class: Druid", "Original class: Ranger", "Fallen paladin", "Fallen ranger",
			"Export allowed", "Hide status", "Large creature", "Moving between areas", "Been in party", "Holding item", "Clear all flags", "", "", "", "", "",
			"", "Allegiance tracking", "General tracking", "Race tracking", "Class tracking", "Specifics tracking", "Gender tracking", "Alignment tracking",
			"Uninterruptible" };
	private static final String s_attacks[] = { "0", "1", "2", "3", "4", "5", "1/2", "3/2", "5/2", "7/2", "9/2" };
	private static final String s_noyes[] = { "No", "Yes" };
	private static final String s_visible[] = { "Shown", "Hidden" };

	static {
		m_magetype.put((long) 0x0000, "None");
		m_magetype.put((long) 0x0040, "Abjurer");
		m_magetype.put((long) 0x0080, "Conjurer");
		m_magetype.put((long) 0x0100, "Diviner");
		m_magetype.put((long) 0x0200, "Enchanter");
		m_magetype.put((long) 0x0400, "Illusionist");
		m_magetype.put((long) 0x0800, "Invoker");
		m_magetype.put((long) 0x1000, "Necromancer");
		m_magetype.put((long) 0x2000, "Transmuter");
		m_magetype.put((long) 0x4000, "Generalist");

		m_colorPlacement.put((long) 0x80, "Metal");
		m_colorPlacement.put((long) 0x81, "Metal (hologram)");
		m_colorPlacement.put((long) 0x82, "Metal (pulsate)");
		m_colorPlacement.put((long) 0x83, "Metal (hologram/pulsate)");
		m_colorPlacement.put((long) 0x90, "Minor cloth");
		m_colorPlacement.put((long) 0x91, "Minor cloth (hologram)");
		m_colorPlacement.put((long) 0x92, "Minor cloth (pulsate)");
		m_colorPlacement.put((long) 0x93, "Minor cloth (hologram/pulsate)");
		m_colorPlacement.put((long) 0xA0, "Main cloth");
		m_colorPlacement.put((long) 0xA1, "Main cloth (hologram)");
		m_colorPlacement.put((long) 0xA2, "Main cloth (pulsate)");
		m_colorPlacement.put((long) 0xA3, "Main cloth (hologram/pulsate)");
		m_colorPlacement.put((long) 0xB0, "Skin");
		m_colorPlacement.put((long) 0xB1, "Skin (hologram)");
		m_colorPlacement.put((long) 0xB2, "Skin (pulsate)");
		m_colorPlacement.put((long) 0xB3, "Skin (hologram/pulsate)");
		m_colorPlacement.put((long) 0xC0, "Leather");
		m_colorPlacement.put((long) 0xC1, "Leather (hologram)");
		m_colorPlacement.put((long) 0xC2, "Leather (pulsate)");
		m_colorPlacement.put((long) 0xC3, "Leather (hologram/pulsate)");
		m_colorPlacement.put((long) 0xD0, "Armor");
		m_colorPlacement.put((long) 0xD1, "Armor (hologram)");
		m_colorPlacement.put((long) 0xD2, "Armor (pulsate)");
		m_colorPlacement.put((long) 0xD3, "Armor (hologram/pulsate)");
		m_colorPlacement.put((long) 0xE0, "Hair");
		m_colorPlacement.put((long) 0xE1, "Hair (hologram)");
		m_colorPlacement.put((long) 0xE2, "Hair (pulsate)");
		m_colorPlacement.put((long) 0xE3, "Hair (hologram/pulsate)");
		m_colorPlacement.put((long) 0x00, "Not used");
	}

	public static void addScriptName(Map<String, Set<ResourceEntry>> scriptNames, ResourceEntry entry) {
		try {
			byte[] buffer = entry.getResourceData();
			String signature = new String(buffer, 0, 4);
			String scriptName = "";
			if (signature.equalsIgnoreCase("CRE ")) {
				String version = new String(buffer, 4, 4);
				if (version.equalsIgnoreCase("V1.0"))
					scriptName = DynamicArray.getString(buffer, 640, 32);
				else if (version.equalsIgnoreCase("V1.1") || version.equalsIgnoreCase("V1.2"))
					scriptName = DynamicArray.getString(buffer, 804, 32);
				else if (version.equalsIgnoreCase("V2.2"))
					scriptName = DynamicArray.getString(buffer, 916, 32);
				else if (version.equalsIgnoreCase("V9.0"))
					scriptName = DynamicArray.getString(buffer, 744, 32);
				if (scriptName.equals("") || scriptName.equalsIgnoreCase("None"))
					return;
				// Apparently script name is the only thing that matters
				// scriptName = entry.toString().substring(0, entry.toString().length() - 4);
				else {
					scriptName = scriptName.toLowerCase().replaceAll(" ", "");
					if (scriptNames.containsKey(scriptName)) {
						Set<ResourceEntry> entries = scriptNames.get(scriptName);
						entries.add(entry);
					} else {
						Set<ResourceEntry> entries = new HashSet<ResourceEntry>();
						entries.add(entry);
						scriptNames.put(scriptName, entries);
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public static String getSearchString(byte buffer[]) {
		String signature = new String(buffer, 0, 4);
		if (signature.equalsIgnoreCase("CHR "))
			return new String(buffer, 8, 32);
		String name = new StringRef(buffer, 8, "").toString().trim();
		String shortname = new StringRef(buffer, 12, "").toString().trim();
		if (name.equals(shortname))
			return name;
		return name + " - " + shortname;
	}

	public CreResource(ResourceEntry entry) throws Exception {
		super(entry);
	}

	public CreResource(AbstractStruct superStruct, String name, byte data[], int startoffset) throws Exception {
		super(superStruct, name, data, startoffset);
	}

	@Override
	public void write(OutputStream os) throws IOException {
		super.writeFlatList(os);
	}

	@Override
	protected int read(byte buffer[], int offset) throws Exception {
		setExtraOffset(getExtraOffset() + offset);
		TextString signature = new TextString(buffer, offset, 4, "Signature");
		list.add(signature);
		TextString version = new TextString(buffer, offset + 4, 4, "Version");
		list.add(version);
		if (signature.toString().equalsIgnoreCase("CHR ")) {
			list.add(new TextString(buffer, offset + 8, 32, "Character name"));
			HexNumber structOffset = new HexNumber(buffer, offset + 40, 4, "CRE structure offset");
			list.add(structOffset);
			list.add(new HexNumber(buffer, offset + 44, 4, "CRE structure length"));
			if (version.toString().equalsIgnoreCase("V2.2")) {
				list.add(new IdsBitmap(buffer, offset + 48, 2, "Quick weapon slot 1", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 50, 2, "Quick shield slot 1", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 52, 2, "Quick weapon slot 2", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 54, 2, "Quick shield slot 2", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 56, 2, "Quick weapon slot 3", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 58, 2, "Quick shield slot 3", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 60, 2, "Quick weapon slot 4", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 62, 2, "Quick shield slot 4", "SLOTS.IDS"));
				list.add(new DecNumber(buffer, offset + 64, 2, "Quick weapon 1 ability"));
				list.add(new DecNumber(buffer, offset + 66, 2, "Quick shield 1 ability"));
				list.add(new DecNumber(buffer, offset + 68, 2, "Quick weapon 2 ability"));
				list.add(new DecNumber(buffer, offset + 70, 2, "Quick shield 2 ability"));
				list.add(new DecNumber(buffer, offset + 72, 2, "Quick weapon 3 ability"));
				list.add(new DecNumber(buffer, offset + 74, 2, "Quick shield 3 ability"));
				list.add(new DecNumber(buffer, offset + 76, 2, "Quick weapon 4 ability"));
				list.add(new DecNumber(buffer, offset + 78, 2, "Quick shield 4 ability"));
				list.add(new ResourceRef(buffer, offset + 80, "Quick spell 1", "SPL"));
				list.add(new ResourceRef(buffer, offset + 88, "Quick spell 2", "SPL"));
				list.add(new ResourceRef(buffer, offset + 96, "Quick spell 3", "SPL"));
				list.add(new ResourceRef(buffer, offset + 104, "Quick spell 4", "SPL"));
				list.add(new ResourceRef(buffer, offset + 112, "Quick spell 5", "SPL"));
				list.add(new ResourceRef(buffer, offset + 120, "Quick spell 6", "SPL"));
				list.add(new ResourceRef(buffer, offset + 128, "Quick spell 7", "SPL"));
				list.add(new ResourceRef(buffer, offset + 136, "Quick spell 8", "SPL"));
				list.add(new ResourceRef(buffer, offset + 144, "Quick spell 9", "SPL"));
				list.add(new IdsBitmap(buffer, offset + 152, 1, "Quick spell 1 class", "CLASS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 153, 1, "Quick spell 2 class", "CLASS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 154, 1, "Quick spell 3 class", "CLASS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 155, 1, "Quick spell 4 class", "CLASS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 156, 1, "Quick spell 5 class", "CLASS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 157, 1, "Quick spell 6 class", "CLASS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 158, 1, "Quick spell 7 class", "CLASS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 159, 1, "Quick spell 8 class", "CLASS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 160, 1, "Quick spell 9 class", "CLASS.IDS"));
				list.add(new Unknown(buffer, offset + 161, 1));
				list.add(new IdsBitmap(buffer, offset + 162, 2, "Quick item slot 1", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 164, 2, "Quick item slot 2", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 166, 2, "Quick item slot 3", "SLOTS.IDS"));
				list.add(new DecNumber(buffer, offset + 168, 2, "Quick item 1 ability"));
				list.add(new DecNumber(buffer, offset + 170, 2, "Quick item 2 ability"));
				list.add(new DecNumber(buffer, offset + 172, 2, "Quick item 3 ability"));
				list.add(new ResourceRef(buffer, offset + 174, "Quick ability 1", "SPL"));
				list.add(new ResourceRef(buffer, offset + 182, "Quick ability 2", "SPL"));
				list.add(new ResourceRef(buffer, offset + 190, "Quick ability 3", "SPL"));
				list.add(new ResourceRef(buffer, offset + 198, "Quick ability 4", "SPL"));
				list.add(new ResourceRef(buffer, offset + 206, "Quick ability 5", "SPL"));
				list.add(new ResourceRef(buffer, offset + 214, "Quick ability 6", "SPL"));
				list.add(new ResourceRef(buffer, offset + 222, "Quick ability 7", "SPL"));
				list.add(new ResourceRef(buffer, offset + 230, "Quick ability 8", "SPL"));
				list.add(new ResourceRef(buffer, offset + 238, "Quick ability 9", "SPL"));
				list.add(new ResourceRef(buffer, offset + 246, "Quick song 1", "SPL"));
				list.add(new ResourceRef(buffer, offset + 254, "Quick song 2", "SPL"));
				list.add(new ResourceRef(buffer, offset + 262, "Quick song 3", "SPL"));
				list.add(new ResourceRef(buffer, offset + 270, "Quick song 4", "SPL"));
				list.add(new ResourceRef(buffer, offset + 278, "Quick song 5", "SPL"));
				list.add(new ResourceRef(buffer, offset + 286, "Quick song 6", "SPL"));
				list.add(new ResourceRef(buffer, offset + 294, "Quick song 7", "SPL"));
				list.add(new ResourceRef(buffer, offset + 302, "Quick song 8", "SPL"));
				list.add(new ResourceRef(buffer, offset + 310, "Quick song 9", "SPL"));
				list.add(new DecNumber(buffer, offset + 318, 4, "Quick button 1"));
				list.add(new DecNumber(buffer, offset + 322, 4, "Quick button 2"));
				list.add(new DecNumber(buffer, offset + 326, 4, "Quick button 3"));
				list.add(new DecNumber(buffer, offset + 330, 4, "Quick button 4"));
				list.add(new DecNumber(buffer, offset + 334, 4, "Quick button 5"));
				list.add(new DecNumber(buffer, offset + 338, 4, "Quick button 6"));
				list.add(new DecNumber(buffer, offset + 342, 4, "Quick button 7"));
				list.add(new DecNumber(buffer, offset + 346, 4, "Quick button 8"));
				list.add(new DecNumber(buffer, offset + 350, 4, "Quick button 9"));
				list.add(new Unknown(buffer, offset + 354, 26));
				list.add(new TextString(buffer, offset + 380, 8, "Voice set prefix"));
				list.add(new TextString(buffer, offset + 388, 32, "Voice set"));
				list.add(new Unknown(buffer, offset + 420, 128));
			} else if (version.toString().equalsIgnoreCase("V1.0") || version.toString().equalsIgnoreCase("V2.0")) {
				list.add(new IdsBitmap(buffer, offset + 48, 2, "Quick weapon slot 1", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 50, 2, "Quick weapon slot 2", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 52, 2, "Quick weapon slot 3", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 54, 2, "Quick weapon slot 4", "SLOTS.IDS"));
				list.add(new DecNumber(buffer, offset + 56, 2, "Quick weapon 1 ability"));
				list.add(new DecNumber(buffer, offset + 58, 2, "Quick weapon 2 ability"));
				list.add(new DecNumber(buffer, offset + 60, 2, "Quick weapon 3 ability"));
				list.add(new DecNumber(buffer, offset + 62, 2, "Quick weapon 4 ability"));
				list.add(new ResourceRef(buffer, offset + 64, "Quick spell 1", "SPL"));
				list.add(new ResourceRef(buffer, offset + 72, "Quick spell 2", "SPL"));
				list.add(new ResourceRef(buffer, offset + 80, "Quick spell 3", "SPL"));
				list.add(new IdsBitmap(buffer, offset + 88, 2, "Quick item slot 1", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 90, 2, "Quick item slot 2", "SLOTS.IDS"));
				list.add(new IdsBitmap(buffer, offset + 92, 2, "Quick item slot 3", "SLOTS.IDS"));
				list.add(new DecNumber(buffer, offset + 94, 2, "Quick item 1 ability"));
				list.add(new DecNumber(buffer, offset + 96, 2, "Quick item 2 ability"));
				list.add(new DecNumber(buffer, offset + 98, 2, "Quick item 3 ability"));
			} else
				list.add(new Unknown(buffer, offset + 48, structOffset.getValue() - 48));
			offset = structOffset.getValue();
			list.add(new TextString(buffer, offset, 4, "Signature 2"));
			version = new TextString(buffer, offset + 4, 4, "Version 2");
			list.add(version);
			setExtraOffset(getExtraOffset() + structOffset.getValue());
		}
		offset += 8;
		return readOther(version.toString(), buffer, offset);
	}

	// //////////////////////
	// Other IE games
	// //////////////////////

	private int readOther(String version, byte buffer[], int offset) throws Exception {
		list.add(new StringRef(buffer, offset, "Name"));
		list.add(new StringRef(buffer, offset + 4, "Tooltip"));
		list.add(new Flag(buffer, offset + 8, 4, "Flags", s_flag));
		list.add(new DecNumber(buffer, offset + 12, 4, "XP value"));
		list.add(new DecNumber(buffer, offset + 16, 4, "XP"));
		list.add(new DecNumber(buffer, offset + 20, 4, "Gold"));
		list.add(new IdsFlag(buffer, offset + 24, 4, "Status", "STATE.IDS"));
		list.add(new DecNumber(buffer, offset + 28, 2, "Current HP"));
		list.add(new DecNumber(buffer, offset + 30, 2, "Maximum HP"));
		list.add(new IdsBitmap(buffer, offset + 32, 4, "Animation", "ANIMATE.IDS"));
		// list.add(new Unknown(buffer, offset + 34, 2));
		// if (version.equalsIgnoreCase("V1.2") || version.equalsIgnoreCase("V1.1"))
		// list.add(new Unknown(buffer, offset + 36, 7));
		// else {
		list.add(new ColorValue(buffer, offset + 36, 1, "Metal color"));
		list.add(new ColorValue(buffer, offset + 37, 1, "Minor color"));
		list.add(new ColorValue(buffer, offset + 38, 1, "Major color"));
		list.add(new ColorValue(buffer, offset + 39, 1, "Skin color"));
		list.add(new ColorValue(buffer, offset + 40, 1, "Leather color"));
		list.add(new ColorValue(buffer, offset + 41, 1, "Armor color"));
		list.add(new ColorValue(buffer, offset + 42, 1, "Hair color"));
		// }
		DecNumber effect_flag = new DecNumber(buffer, offset + 43, 1, "Effect flag");
		list.add(effect_flag);
		list.add(new ResourceRef(buffer, offset + 44, "Small portrait", "BMP"));
		if (version.equalsIgnoreCase("V1.2") || version.equalsIgnoreCase("V1.1"))
			list.add(new ResourceRef(buffer, offset + 52, "Large portrait", "BAM"));
		else
			list.add(new ResourceRef(buffer, offset + 52, "Large portrait", "BMP"));
		list.add(new UnsignDecNumber(buffer, offset + 60, 1, "Reputation"));
		list.add(new UnsignDecNumber(buffer, offset + 61, 1, "Hide in shadows"));
		list.add(new DecNumber(buffer, offset + 62, 2, "Natural AC"));
		list.add(new DecNumber(buffer, offset + 64, 2, "Effective AC"));
		list.add(new DecNumber(buffer, offset + 66, 2, "Crushing AC modifier"));
		list.add(new DecNumber(buffer, offset + 68, 2, "Missile AC modifier"));
		list.add(new DecNumber(buffer, offset + 70, 2, "Piercing AC modifier"));
		list.add(new DecNumber(buffer, offset + 72, 2, "Slashing AC modifier"));
		list.add(new DecNumber(buffer, offset + 74, 1, "THAC0"));
		// if (version.equalsIgnoreCase("V1.2") || version.equalsIgnoreCase("V1.1"))
		list.add(new Bitmap(buffer, offset + 75, 1, "# attacks", s_attacks));
		// else
		// list.add(new DecNumber(buffer, offset + 75, 1, "# attacks"));
		list.add(new DecNumber(buffer, offset + 76, 1, "Save vs. death"));
		list.add(new DecNumber(buffer, offset + 77, 1, "Save vs. wand"));
		list.add(new DecNumber(buffer, offset + 78, 1, "Save vs. polymorph"));
		list.add(new DecNumber(buffer, offset + 79, 1, "Save vs. breath"));
		list.add(new DecNumber(buffer, offset + 80, 1, "Save vs. spell"));
		list.add(new DecNumber(buffer, offset + 81, 1, "Resist fire"));
		list.add(new DecNumber(buffer, offset + 82, 1, "Resist cold"));
		list.add(new DecNumber(buffer, offset + 83, 1, "Resist electricity"));
		list.add(new DecNumber(buffer, offset + 84, 1, "Resist acid"));
		list.add(new DecNumber(buffer, offset + 85, 1, "Resist magic"));
		list.add(new DecNumber(buffer, offset + 86, 1, "Resist magic fire"));
		list.add(new DecNumber(buffer, offset + 87, 1, "Resist magic cold"));
		list.add(new DecNumber(buffer, offset + 88, 1, "Resist slashing"));
		list.add(new DecNumber(buffer, offset + 89, 1, "Resist crushing"));
		list.add(new DecNumber(buffer, offset + 90, 1, "Resist piercing"));
		list.add(new DecNumber(buffer, offset + 91, 1, "Resist missile"));
		if (version.equalsIgnoreCase("V1.2") || version.equalsIgnoreCase("V1.1")) {
			list.add(new DecNumber(buffer, offset + 92, 1, "Unspent proficiencies"));
			// list.add(new Unknown(buffer, offset + 93, 1));
		} else {
			list.add(new UnsignDecNumber(buffer, offset + 92, 1, "Detect illusions"));
		}
		list.add(new UnsignDecNumber(buffer, offset + 93, 1, "Set traps"));
		list.add(new DecNumber(buffer, offset + 94, 1, "Lore"));
		list.add(new UnsignDecNumber(buffer, offset + 95, 1, "Open locks"));
		list.add(new UnsignDecNumber(buffer, offset + 96, 1, "Move silently"));
		list.add(new UnsignDecNumber(buffer, offset + 97, 1, "Find traps"));
		list.add(new UnsignDecNumber(buffer, offset + 98, 1, "Pick pockets"));
		list.add(new DecNumber(buffer, offset + 99, 1, "Fatigue"));
		list.add(new DecNumber(buffer, offset + 100, 1, "Intoxication"));
		list.add(new DecNumber(buffer, offset + 101, 1, "Luck"));
		if (version.equals("V1.0")) {
			list.add(new DecNumber(buffer, offset + 102, 1, "Large sword proficiency"));
			list.add(new DecNumber(buffer, offset + 103, 1, "Small sword proficiency"));
			list.add(new DecNumber(buffer, offset + 104, 1, "Bow proficiency"));
			list.add(new DecNumber(buffer, offset + 105, 1, "Spear proficiency"));
			list.add(new DecNumber(buffer, offset + 106, 1, "Blunt proficiency"));
			list.add(new DecNumber(buffer, offset + 107, 1, "Spiked proficiency"));
			list.add(new DecNumber(buffer, offset + 108, 1, "Axe proficiency"));
			list.add(new DecNumber(buffer, offset + 109, 1, "Missile proficiency"));
			list.add(new Unknown(buffer, offset + 110, 12));
		} else if (version.equalsIgnoreCase("V1.2") || version.equalsIgnoreCase("V1.1")) {
			list.add(new DecNumber(buffer, offset + 102, 1, "Fist proficiency"));
			list.add(new DecNumber(buffer, offset + 103, 1, "Edged-weapon proficiency"));
			list.add(new DecNumber(buffer, offset + 104, 1, "Hammer proficiency"));
			list.add(new DecNumber(buffer, offset + 105, 1, "Axe proficiency"));
			list.add(new DecNumber(buffer, offset + 106, 1, "Club proficiency"));
			list.add(new DecNumber(buffer, offset + 107, 1, "Bow proficiency"));
			// list.add(new DecNumber(buffer, offset + 108, 1, "Extra proficiency 1"));
			// list.add(new DecNumber(buffer, offset + 109, 1, "Extra proficiency 2"));
			// list.add(new DecNumber(buffer, offset + 110, 1, "Extra proficiency 3"));
			// list.add(new DecNumber(buffer, offset + 111, 1, "Extra proficiency 4"));
			// list.add(new DecNumber(buffer, offset + 112, 1, "Extra proficiency 5"));
			// list.add(new DecNumber(buffer, offset + 113, 1, "Extra proficiency 6"));
			// list.add(new DecNumber(buffer, offset + 114, 1, "Extra proficiency 7"));
			// list.add(new DecNumber(buffer, offset + 115, 1, "Extra proficiency 8"));
			// list.add(new DecNumber(buffer, offset + 116, 1, "Extra proficiency 9"));
			list.add(new Unknown(buffer, offset + 108, 14));
		} else if (version.equalsIgnoreCase("V9.0")) {
			list.add(new DecNumber(buffer, offset + 102, 1, "Large sword proficiency"));
			list.add(new DecNumber(buffer, offset + 103, 1, "Small sword proficiency"));
			list.add(new DecNumber(buffer, offset + 104, 1, "Bow proficiency"));
			list.add(new DecNumber(buffer, offset + 105, 1, "Spear proficiency"));
			list.add(new DecNumber(buffer, offset + 106, 1, "Axe proficiency"));
			list.add(new DecNumber(buffer, offset + 107, 1, "Missile proficiency"));
			list.add(new DecNumber(buffer, offset + 108, 1, "Greatsword proficiency"));
			list.add(new DecNumber(buffer, offset + 109, 1, "Dagger proficiency"));
			list.add(new DecNumber(buffer, offset + 110, 1, "Halberd proficiency"));
			list.add(new DecNumber(buffer, offset + 111, 1, "Mace proficiency"));
			list.add(new DecNumber(buffer, offset + 112, 1, "Flail proficiency"));
			list.add(new DecNumber(buffer, offset + 113, 1, "Hammer proficiency"));
			list.add(new DecNumber(buffer, offset + 114, 1, "Club proficiency"));
			list.add(new DecNumber(buffer, offset + 115, 1, "Quarterstaff proficiency"));
			list.add(new DecNumber(buffer, offset + 116, 1, "Crossbow proficiency"));
			list.add(new Unknown(buffer, offset + 117, 5));
		} else {
			list.clear();
			throw new Exception("Unsupported version: " + version);
		}
		list.add(new DecNumber(buffer, offset + 122, 1, "Undead level"));
		list.add(new DecNumber(buffer, offset + 123, 1, "Tracking"));
		list.add(new TextString(buffer, offset + 124, 32, "Target"));
		LongIntegerHashMap<IdsMapEntry> sndmap = null;
		if (Keyfile.getInstance().resourceExists("SNDSLOT.IDS"))
			sndmap = IdsMapCache.get("SNDSLOT.IDS").getMap();
		else if (Keyfile.getInstance().resourceExists("SOUNDOFF.IDS"))
			sndmap = IdsMapCache.get("SOUNDOFF.IDS").getMap();
		if (sndmap != null) {
			for (int i = 0; i < 100; i++)
				if (sndmap.containsKey((long) i))
					list.add(new StringRef(buffer, offset + 156 + i * 4, "Sound: " + ((IdsMapEntry) sndmap.get((long) i)).getString()));
				else
					list.add(new StringRef(buffer, offset + 156 + i * 4, "Sound: Unknown"));
		} else {
			for (int i = 0; i < 100; i++)
				list.add(new StringRef(buffer, offset + 156 + i * 4, "Soundset string"));
		}
		list.add(new DecNumber(buffer, offset + 556, 1, "Level first class"));
		list.add(new DecNumber(buffer, offset + 557, 1, "Level second class"));
		list.add(new DecNumber(buffer, offset + 558, 1, "Level third class"));
		list.add(new IdsBitmap(buffer, offset + 559, 1, "Sex", "GENDER.IDS"));
		// new Bitmap(buffer, offset + 559, 1, "Sex", new String[]{"", "Male", "Female", "Neither", "Both"}));
		list.add(new DecNumber(buffer, offset + 560, 1, "Strength"));
		list.add(new DecNumber(buffer, offset + 561, 1, "Strength bonus"));
		list.add(new DecNumber(buffer, offset + 562, 1, "Intelligence"));
		list.add(new DecNumber(buffer, offset + 563, 1, "Wisdom"));
		list.add(new DecNumber(buffer, offset + 564, 1, "Dexterity"));
		list.add(new DecNumber(buffer, offset + 565, 1, "Constitution"));
		list.add(new DecNumber(buffer, offset + 566, 1, "Charisma"));
		list.add(new DecNumber(buffer, offset + 567, 1, "Morale"));
		list.add(new DecNumber(buffer, offset + 568, 1, "Morale break"));
		list.add(new IdsBitmap(buffer, offset + 569, 1, "Racial enemy", "RACE.IDS"));
		list.add(new DecNumber(buffer, offset + 570, 2, "Morale recovery"));
		// list.add(new Unknown(buffer, offset + 571, 1));
		if (Keyfile.getInstance().resourceExists("KITLIST.2DA"))
			list.add(new Kit2daBitmap(buffer, offset + 572));
		else {
			if (Keyfile.getInstance().resourceExists("DEITY.IDS"))
				list.add(new IdsBitmap(buffer, offset + 572, 2, "Deity", "DEITY.IDS"));
			else if (Keyfile.getInstance().resourceExists("DIETY.IDS"))
				list.add(new IdsBitmap(buffer, offset + 572, 2, "Deity", "DIETY.IDS"));
			else
				list.add(new Unknown(buffer, offset + 572, 2));
			if (Keyfile.getInstance().resourceExists("MAGESPEC.IDS"))
				list.add(new IdsBitmap(buffer, offset + 574, 2, "Mage type", "MAGESPEC.IDS"));
			else
				list.add(new HashBitmap(buffer, offset + 574, 2, "Mage type", m_magetype));
		}
		list.add(new ResourceRef(buffer, offset + 576, "Override script", "BCS"));
		list.add(new ResourceRef(buffer, offset + 584, "Class script", "BCS"));
		list.add(new ResourceRef(buffer, offset + 592, "Race script", "BCS"));
		list.add(new ResourceRef(buffer, offset + 600, "General script", "BCS"));
		list.add(new ResourceRef(buffer, offset + 608, "Default script", "BCS"));
		if (version.equalsIgnoreCase("V1.2") || version.equalsIgnoreCase("V1.1")) {
			// LongIntegerHashMap<String> m_zoom = new LongIntegerHashMap<String>();
			// m_zoom.put(0x0000L, "No");
			// m_zoom.put(0xffffL, "Yes");
			list.add(new Unknown(buffer, offset + 616, 24));
			list.add(new Unknown(buffer, offset + 640, 4));
			list.add(new Unknown(buffer, offset + 644, 8));
			list.add(new Unknown(buffer, offset + 652, 4, "Overlays offset"));
			list.add(new Unknown(buffer, offset + 656, 4, "Overlays size"));
			list.add(new DecNumber(buffer, offset + 660, 4, "XP second class"));
			list.add(new DecNumber(buffer, offset + 664, 4, "XP third class"));
			LongIntegerHashMap<IdsMapEntry> intMap = IdsMapCache.get("INTERNAL.IDS").getMap();
			for (int i = 0; i < 10; i++) {
				if (intMap.containsKey((long) i))
					list.add(new DecNumber(buffer, offset + 668 + i * 2, 2, ((IdsMapEntry) intMap.get((long) i)).getString()));
				else
					list.add(new DecNumber(buffer, offset + 668 + i * 2, 2, "Internal " + i));
			}
			list.add(new DecNumber(buffer, offset + 688, 1, "Good increment by"));
			list.add(new DecNumber(buffer, offset + 689, 1, "Law increment by"));
			list.add(new DecNumber(buffer, offset + 690, 1, "Lady increment by"));
			list.add(new DecNumber(buffer, offset + 691, 1, "Murder increment by"));
			list.add(new TextString(buffer, offset + 692, 32, "Character type"));
			list.add(new DecNumber(buffer, offset + 724, 1, "Dialogue activation radius"));
			list.add(new DecNumber(buffer, offset + 725, 1, "Collision radius")); // 0x2dd
			list.add(new Unknown(buffer, offset + 726, 1));
			list.add(new DecNumber(buffer, offset + 727, 1, "# colors"));
			list.add(new Flag(buffer, offset + 728, 4, "Attributes", new String[] { "No flags set", "", "Transparent", "", "", "Increment death variable",
					"Increment kill count", "Script name only", "Increment faction kills", "Increment team kills", "Invulnerable", "Good increment on death",
					"Law increment on death", "Lady increment on death", "Murder increment on death", "Don't face speaker", "Call for help", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "Died" }));
			// list.add(new Flag(buffer, offset + 729, 1, "Attribute flags 2",
			// new String[]{"No flags set", "", "Invulnerable"}));
			// list.add(new Unknown(buffer, offset + 730, 2));
			list.add(new IdsBitmap(buffer, offset + 732, 2, "Color 1", "CLOWNCLR.IDS"));
			list.add(new IdsBitmap(buffer, offset + 734, 2, "Color 2", "CLOWNCLR.IDS"));
			list.add(new IdsBitmap(buffer, offset + 736, 2, "Color 3", "CLOWNCLR.IDS"));
			list.add(new IdsBitmap(buffer, offset + 738, 2, "Color 4", "CLOWNCLR.IDS"));
			list.add(new IdsBitmap(buffer, offset + 740, 2, "Color 5", "CLOWNCLR.IDS"));
			list.add(new IdsBitmap(buffer, offset + 742, 2, "Color 6", "CLOWNCLR.IDS"));
			list.add(new IdsBitmap(buffer, offset + 744, 2, "Color 7", "CLOWNCLR.IDS"));
			list.add(new Unknown(buffer, offset + 746, 3));
			list.add(new HashBitmap(buffer, offset + 749, 1, "Color 1 placement", m_colorPlacement));
			list.add(new HashBitmap(buffer, offset + 750, 1, "Color 2 placement", m_colorPlacement));
			list.add(new HashBitmap(buffer, offset + 751, 1, "Color 3 placement", m_colorPlacement));
			list.add(new HashBitmap(buffer, offset + 752, 1, "Color 4 placement", m_colorPlacement));
			list.add(new HashBitmap(buffer, offset + 753, 1, "Color 5 placement", m_colorPlacement));
			list.add(new HashBitmap(buffer, offset + 754, 1, "Color 6 placement", m_colorPlacement));
			list.add(new HashBitmap(buffer, offset + 755, 1, "Color 7 placement", m_colorPlacement));
			list.add(new Unknown(buffer, offset + 756, 21));
			list.add(new IdsBitmap(buffer, offset + 777, 1, "Species", "RACE.IDS"));
			list.add(new IdsBitmap(buffer, offset + 778, 1, "Team", "TEAM.IDS"));
			list.add(new IdsBitmap(buffer, offset + 779, 1, "Faction", "FACTION.IDS"));
			offset += 164;
		} else if (version.equalsIgnoreCase("V9.0")) {
			list.add(new Bitmap(buffer, offset + 616, 1, "Default visibility", s_visible));
			list.add(new Bitmap(buffer, offset + 617, 1, "Set extra death variable?", s_noyes));
			list.add(new Bitmap(buffer, offset + 618, 1, "Increment kill count?", s_noyes));
			list.add(new Unknown(buffer, offset + 619, 1));
			list.add(new DecNumber(buffer, offset + 620, 2, "Internal 1"));
			list.add(new DecNumber(buffer, offset + 622, 2, "Internal 2"));
			list.add(new DecNumber(buffer, offset + 624, 2, "Internal 3"));
			list.add(new DecNumber(buffer, offset + 626, 2, "Internal 4"));
			list.add(new DecNumber(buffer, offset + 628, 2, "Internal 5"));
			list.add(new TextString(buffer, offset + 630, 32, "Death variable (set)"));
			list.add(new TextString(buffer, offset + 662, 32, "Death variable (increment)"));
			list.add(new Bitmap(buffer, offset + 694, 2, "Location saved?", s_noyes));
			list.add(new DecNumber(buffer, offset + 696, 2, "Saved location: X"));
			list.add(new DecNumber(buffer, offset + 698, 2, "Saved location: Y"));
			list.add(new DecNumber(buffer, offset + 700, 2, "Saved orientation"));
			list.add(new Unknown(buffer, offset + 702, 18));
			offset += 104;
		}
		list.add(new IdsBitmap(buffer, offset + 616, 1, "Allegiance", "EA.IDS"));
		list.add(new IdsBitmap(buffer, offset + 617, 1, "General", "GENERAL.IDS"));
		list.add(new IdsBitmap(buffer, offset + 618, 1, "Race", "RACE.IDS"));
		list.add(new IdsBitmap(buffer, offset + 619, 1, "Class", "CLASS.IDS"));
		list.add(new IdsBitmap(buffer, offset + 620, 1, "Specifics", "SPECIFIC.IDS"));
		list.add(new IdsBitmap(buffer, offset + 621, 1, "Gender", "GENDER.IDS"));
		list.add(new IdsBitmap(buffer, offset + 622, 1, "Object spec 1", "OBJECT.IDS"));
		list.add(new IdsBitmap(buffer, offset + 623, 1, "Object spec 2", "OBJECT.IDS"));
		list.add(new IdsBitmap(buffer, offset + 624, 1, "Object spec 3", "OBJECT.IDS"));
		list.add(new IdsBitmap(buffer, offset + 625, 1, "Object spec 4", "OBJECT.IDS"));
		list.add(new IdsBitmap(buffer, offset + 626, 1, "Object spec 5", "OBJECT.IDS"));
		// if (ResourceFactory.getGameID() == ResourceFactory.ID_BG2 ||
		// ResourceFactory.getGameID() == ResourceFactory.ID_BG2TOB)
		// list.add(new IdsBitmap(buffer, offset + 627, 1, "Alignment", "ALIGN.IDS"));
		// else
		list.add(new IdsBitmap(buffer, offset + 627, 1, "Alignment", "ALIGNMEN.IDS"));
		list.add(new DecNumber(buffer, offset + 628, 2, "Global identifier"));
		list.add(new DecNumber(buffer, offset + 630, 2, "Local identifier"));
		list.add(new TextString(buffer, offset + 632, 32, "Script name"));
		// list.add(new Unknown(buffer, offset + 650, 14));

		SectionOffset offsetKnownSpells = new SectionOffset(buffer, offset + 664, "Known spells offset", KnownSpells.class);
		list.add(offsetKnownSpells);
		SectionCount countKnownSpells = new SectionCount(buffer, offset + 668, 4, "# known spells", KnownSpells.class);
		list.add(countKnownSpells);
		SectionOffset offsetMemSpellInfo = new SectionOffset(buffer, offset + 672, "Memorization info offset", SpellMemorization.class);
		list.add(offsetMemSpellInfo);
		SectionCount countMemSpellInfo = new SectionCount(buffer, offset + 676, 4, "# memorization info", SpellMemorization.class);
		list.add(countMemSpellInfo);
		SectionOffset offsetMemSpells = new SectionOffset(buffer, offset + 680, "Memorized spells offset", MemorizedSpells.class);
		list.add(offsetMemSpells);
		SectionCount countMemSpells = new SectionCount(buffer, offset + 684, 4, "# memorized spells", MemorizedSpells.class);
		list.add(countMemSpells);
		SectionOffset offsetItemslots = new SectionOffset(buffer, offset + 688, "Item slots offset", null);
		list.add(offsetItemslots);
		SectionOffset offsetItems = new SectionOffset(buffer, offset + 692, "Items offset", Item.class);
		list.add(offsetItems);
		SectionCount countItems = new SectionCount(buffer, offset + 696, 4, "# items", Item.class);
		list.add(countItems);
		SectionOffset offsetEffects;
		SectionCount countEffects;
		if (effect_flag.getValue() == 1) {
			offsetEffects = new SectionOffset(buffer, offset + 700, "Effects offset", Effect2.class);
			countEffects = new SectionCount(buffer, offset + 704, 4, "# effects", Effect2.class);
		} else {
			offsetEffects = new SectionOffset(buffer, offset + 700, "Effects offset", Effect.class);
			countEffects = new SectionCount(buffer, offset + 704, 4, "# effects", Effect.class);
		}
		list.add(offsetEffects);
		list.add(countEffects);
		list.add(new ResourceRef(buffer, offset + 708, "Dialogue", "DLG"));

		offset = getExtraOffset() + offsetKnownSpells.getValue();
		for (int i = 0; i < countKnownSpells.getValue(); i++) {
			KnownSpells known = new KnownSpells(this, buffer, offset, i);
			offset = known.getEndOffset();
			list.add(known);
		}

		offset = getExtraOffset() + offsetMemSpellInfo.getValue();
		for (int i = 0; i < countMemSpellInfo.getValue(); i++) {
			SpellMemorization mem = new SpellMemorization(this, buffer, offset, i);
			offset = mem.getEndOffset();
			mem.readMemorizedSpells(buffer, offsetMemSpells.getValue() + getExtraOffset());
			list.add(mem);
		}

		offset = getExtraOffset() + offsetEffects.getValue();
		if (effect_flag.getValue() == 1)
			for (int i = 0; i < countEffects.getValue(); i++) {
				Effect2 eff = new Effect2(this, buffer, offset, i);
				offset = eff.getEndOffset();
				list.add(eff);
			}
		else
			for (int i = 0; i < countEffects.getValue(); i++) {
				Effect eff = new Effect(this, buffer, offset, i);
				offset = eff.getEndOffset();
				list.add(eff);
			}

		offset = getExtraOffset() + offsetItems.getValue();
		for (int i = 0; i < countItems.getValue(); i++) {
			Item item = new Item(this, buffer, offset, i);
			offset = item.getEndOffset();
			list.add(item);
		}

		offset = getExtraOffset() + offsetItemslots.getValue();
		if (version.equalsIgnoreCase("V1.2")) {
			list.add(new DecNumber(buffer, offset, 2, "Right earring"));
			list.add(new DecNumber(buffer, offset + 2, 2, "Chest"));
			list.add(new DecNumber(buffer, offset + 4, 2, "Left tattoo"));
			list.add(new DecNumber(buffer, offset + 6, 2, "Hand"));
			list.add(new DecNumber(buffer, offset + 8, 2, "Left ring"));
			list.add(new DecNumber(buffer, offset + 10, 2, "Right ring"));
			list.add(new DecNumber(buffer, offset + 12, 2, "Left earring"));
			list.add(new DecNumber(buffer, offset + 14, 2, "Right tattoo (lower)"));
			list.add(new DecNumber(buffer, offset + 16, 2, "Wrist"));
			list.add(new DecNumber(buffer, offset + 18, 2, "Weapon 1"));
			list.add(new DecNumber(buffer, offset + 20, 2, "Weapon 2"));
			list.add(new DecNumber(buffer, offset + 22, 2, "Weapon 3"));
			list.add(new DecNumber(buffer, offset + 24, 2, "Weapon 4"));
			list.add(new DecNumber(buffer, offset + 26, 2, "Quiver 1"));
			list.add(new DecNumber(buffer, offset + 28, 2, "Quiver 2"));
			list.add(new DecNumber(buffer, offset + 30, 2, "Quiver 3"));
			list.add(new DecNumber(buffer, offset + 32, 2, "Quiver 4"));
			list.add(new DecNumber(buffer, offset + 34, 2, "Quiver 5"));
			list.add(new DecNumber(buffer, offset + 36, 2, "Quiver 6"));
			list.add(new DecNumber(buffer, offset + 38, 2, "Right tattoo (upper)"));
			list.add(new DecNumber(buffer, offset + 40, 2, "Quick item 1"));
			list.add(new DecNumber(buffer, offset + 42, 2, "Quick item 2"));
			list.add(new DecNumber(buffer, offset + 44, 2, "Quick item 3"));
			list.add(new DecNumber(buffer, offset + 46, 2, "Quick item 4"));
			list.add(new DecNumber(buffer, offset + 48, 2, "Quick item 5"));
			list.add(new DecNumber(buffer, offset + 50, 2, "Inventory 1"));
			list.add(new DecNumber(buffer, offset + 52, 2, "Inventory 2"));
			list.add(new DecNumber(buffer, offset + 54, 2, "Inventory 3"));
			list.add(new DecNumber(buffer, offset + 56, 2, "Inventory 4"));
			list.add(new DecNumber(buffer, offset + 58, 2, "Inventory 5"));
			list.add(new DecNumber(buffer, offset + 60, 2, "Inventory 6"));
			list.add(new DecNumber(buffer, offset + 62, 2, "Inventory 7"));
			list.add(new DecNumber(buffer, offset + 64, 2, "Inventory 8"));
			list.add(new DecNumber(buffer, offset + 66, 2, "Inventory 9"));
			list.add(new DecNumber(buffer, offset + 68, 2, "Inventory 10"));
			list.add(new DecNumber(buffer, offset + 70, 2, "Inventory 11"));
			list.add(new DecNumber(buffer, offset + 72, 2, "Inventory 12"));
			list.add(new DecNumber(buffer, offset + 74, 2, "Inventory 13"));
			list.add(new DecNumber(buffer, offset + 76, 2, "Inventory 14"));
			list.add(new DecNumber(buffer, offset + 78, 2, "Inventory 15"));
			list.add(new DecNumber(buffer, offset + 80, 2, "Inventory 16"));
			list.add(new DecNumber(buffer, offset + 82, 2, "Inventory 17"));
			list.add(new DecNumber(buffer, offset + 84, 2, "Inventory 18"));
			list.add(new DecNumber(buffer, offset + 86, 2, "Inventory 19"));
			list.add(new DecNumber(buffer, offset + 88, 2, "Inventory 20"));
			list.add(new DecNumber(buffer, offset + 90, 2, "Magically created weapon"));
			list.add(new DecNumber(buffer, offset + 92, 2, "Weapon slot selected"));
			list.add(new DecNumber(buffer, offset + 94, 2, "Weapon ability selected"));
		} else {
			list.add(new DecNumber(buffer, offset, 2, "Helmet"));
			list.add(new DecNumber(buffer, offset + 2, 2, "Armor"));
			list.add(new DecNumber(buffer, offset + 4, 2, "Shield"));
			list.add(new DecNumber(buffer, offset + 6, 2, "Gloves"));
			list.add(new DecNumber(buffer, offset + 8, 2, "Left ring"));
			list.add(new DecNumber(buffer, offset + 10, 2, "Right ring"));
			list.add(new DecNumber(buffer, offset + 12, 2, "Amulet"));
			list.add(new DecNumber(buffer, offset + 14, 2, "Belt"));
			list.add(new DecNumber(buffer, offset + 16, 2, "Boots"));
			list.add(new DecNumber(buffer, offset + 18, 2, "Weapon 1"));
			list.add(new DecNumber(buffer, offset + 20, 2, "Weapon 2"));
			list.add(new DecNumber(buffer, offset + 22, 2, "Weapon 3"));
			list.add(new DecNumber(buffer, offset + 24, 2, "Weapon 4"));
			list.add(new DecNumber(buffer, offset + 26, 2, "Quiver 1"));
			list.add(new DecNumber(buffer, offset + 28, 2, "Quiver 2"));
			list.add(new DecNumber(buffer, offset + 30, 2, "Quiver 3"));
			list.add(new DecNumber(buffer, offset + 32, 2, "Quiver 4"));
			list.add(new DecNumber(buffer, offset + 34, 2, "Cloak"));
			list.add(new DecNumber(buffer, offset + 36, 2, "Quick item 1"));
			list.add(new DecNumber(buffer, offset + 38, 2, "Quick item 2"));
			list.add(new DecNumber(buffer, offset + 40, 2, "Quick item 3"));
			list.add(new DecNumber(buffer, offset + 42, 2, "Inventory 1"));
			list.add(new DecNumber(buffer, offset + 44, 2, "Inventory 2"));
			list.add(new DecNumber(buffer, offset + 46, 2, "Inventory 3"));
			list.add(new DecNumber(buffer, offset + 48, 2, "Inventory 4"));
			list.add(new DecNumber(buffer, offset + 50, 2, "Inventory 5"));
			list.add(new DecNumber(buffer, offset + 52, 2, "Inventory 6"));
			list.add(new DecNumber(buffer, offset + 54, 2, "Inventory 7"));
			list.add(new DecNumber(buffer, offset + 56, 2, "Inventory 8"));
			list.add(new DecNumber(buffer, offset + 58, 2, "Inventory 9"));
			list.add(new DecNumber(buffer, offset + 60, 2, "Inventory 10"));
			list.add(new DecNumber(buffer, offset + 62, 2, "Inventory 11"));
			list.add(new DecNumber(buffer, offset + 64, 2, "Inventory 12"));
			list.add(new DecNumber(buffer, offset + 66, 2, "Inventory 13"));
			list.add(new DecNumber(buffer, offset + 68, 2, "Inventory 14"));
			list.add(new DecNumber(buffer, offset + 70, 2, "Inventory 15"));
			list.add(new DecNumber(buffer, offset + 72, 2, "Inventory 16"));
			list.add(new DecNumber(buffer, offset + 74, 2, "Magically created weapon"));
			list.add(new DecNumber(buffer, offset + 76, 2, "Weapon slot selected"));
			list.add(new DecNumber(buffer, offset + 78, 2, "Weapon ability selected"));
		}
		int endoffset = offset;
		for (int i = 0; i < list.size(); i++) {
			StructEntry entry = list.get(i);
			if (entry.getOffset() + entry.getSize() > endoffset)
				endoffset = entry.getOffset() + entry.getSize();
		}
		return endoffset;
	}

}
