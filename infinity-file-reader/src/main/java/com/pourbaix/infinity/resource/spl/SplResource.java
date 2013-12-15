// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2005 Jon Olav Hauglid
// See LICENSE.txt for license information

package com.pourbaix.infinity.resource.spl;

import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.Resource;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.Flag;
import com.pourbaix.infinity.resource.datatype.IdsBitmap;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.SectionCount;
import com.pourbaix.infinity.resource.datatype.SectionOffset;
import com.pourbaix.infinity.resource.datatype.StringRef;
import com.pourbaix.infinity.resource.datatype.TextString;
import com.pourbaix.infinity.resource.datatype.Unknown;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;

public final class SplResource extends AbstractStruct implements Resource {
	public static final String[] s_category = { "None", "Spell protections", "Specific protections", "Illusionary protections", "Magic attack",
			"Divination attack", "Conjuration", "Combat protections", "Contingency", "Battleground", "Offensive damage", "Disabling", "Combination",
			"Non-combat" };
	public static final String[] s_school = { "None", "Abjurer", "Conjurer", "Diviner", "Enchanter", "Illusionist", "Invoker", "Necromancer", "Transmuter",
			"Generalist" };
	private static final String[] s_spelltype = { "Special", "Wizard", "Priest", "Psionic", "Innate", "Bard song" };
	private static final String[] s_anim = { "None", "Fire aqua", "Fire blue", "Fire gold", "Fire green", "Fire magenta", "Fire purple", "Fire red",
			"Fire white", "Necromancy", "Alteration", "Enchantment", "Abjuration", "Illusion", "Conjuration", "Invocation", "Divination", "Fountain aqua",
			"Fountain black", "Fountain blue", "Fountain gold", "Fountain green", "Fountain magenta", "Fountain purple", "Fountain red", "Fountain white",
			"Swirl aqua", "Swirl black", "Swirl blue", "Swirl gold", "Swirl green", "Swirl magenta", "Swirl purple", "Swirl red", "Swirl white" };
	private static final String[] s_spellflag = { "No flags set", "", "", "", "", "", "", "", "", "", "", "Hostile", "No LOS required", "Allow spotting",
			"Outdoors only", "Non-magical ability", "Trigger/Contingency", "Non-combat ability", "", "", "", "", "", "", "", "Ex: can target invisible",
			"Ex: castable when silenced" };
	private static final String[] s_exclude = { "None", "Chaotic", "Evil", "Good", "... Neutral", "Lawful", "Neutral ...", "Abjurer", "Conjurer", "Diviner",
			"Enchanter", "Illusionist", "Invoker", "Necromancer", "Transmuter", "Generalist", "", "", "", "", "", "", "", "", "Elf", "Dwarf", "Half-elf",
			"Halfling", "Human", "Gnome", "", "Cleric", "Druid" };

	public static String getSearchString(byte buffer[]) {
		return new StringRef(buffer, 8, "").toString().trim();
	}

	public SplResource(ResourceEntry entry) throws Exception {
		super(entry);
	}

	@Override
	protected int read(byte buffer[], int offset) throws Exception {
		list.add(new TextString(buffer, offset, 4, "Signature"));
		TextString version = new TextString(buffer, offset + 4, 4, "Version");
		list.add(version);
		list.add(new StringRef(buffer, offset + 8, "Spell name"));
		list.add(new StringRef(buffer, offset + 12, "Identified name"));
		list.add(new ResourceRef(buffer, offset + 16, "Casting sound", "WAV"));
		list.add(new Flag(buffer, offset + 24, 4, "Flags", s_spellflag));
		list.add(new Bitmap(buffer, offset + 28, 2, "Spell type", s_spelltype));
		list.add(new Flag(buffer, offset + 30, 4, "Exclusion flags", s_exclude)); // 0x1e
		//	    list.add(new HashBitmap(buffer, offset + 32, 2, "Priest type", m_priesttype));     // 0x20
		list.add(new Bitmap(buffer, offset + 34, 2, "Casting animation", s_anim)); // 0x22
		list.add(new Unknown(buffer, offset + 36, 1)); // 0x23
		if (Keyfile.getInstance().resourceExists("SCHOOL.IDS"))
			list.add(new IdsBitmap(buffer, offset + 37, 1, "Primary type (school)", "SCHOOL.IDS")); // 0x25
		else
			list.add(new Bitmap(buffer, offset + 37, 1, "Primary type (school)", s_school)); // 0x25
		list.add(new Unknown(buffer, offset + 38, 1));
		list.add(new Bitmap(buffer, offset + 39, 1, "Secondary type", s_category)); // 0x27
		list.add(new Unknown(buffer, offset + 40, 12));
		list.add(new DecNumber(buffer, offset + 52, 4, "Spell level"));
		list.add(new Unknown(buffer, offset + 56, 2));
		list.add(new ResourceRef(buffer, offset + 58, "Spell icon", "BAM"));
		list.add(new Unknown(buffer, offset + 66, 2));
		list.add(new ResourceRef(buffer, offset + 68, "Ground icon", "BAM"));
		list.add(new Unknown(buffer, offset + 76, 4));
		list.add(new StringRef(buffer, offset + 80, "Spell description"));
		list.add(new StringRef(buffer, offset + 84, "Identified description"));
		list.add(new ResourceRef(buffer, offset + 88, "Description image", "BAM"));
		list.add(new Unknown(buffer, offset + 96, 4));
		SectionOffset abil_offset = new SectionOffset(buffer, offset + 100, "Abilities offset", Ability.class);
		list.add(abil_offset);
		SectionCount abil_count = new SectionCount(buffer, offset + 104, 2, "# abilities", Ability.class);
		list.add(abil_count);
		SectionOffset global_offset = new SectionOffset(buffer, offset + 106, "Effects offset", Effect.class);
		list.add(global_offset);
		list.add(new DecNumber(buffer, offset + 110, 2, "First effect index"));
		SectionCount global_count = new SectionCount(buffer, offset + 112, 2, "# global effects", Effect.class);
		list.add(global_count);

		if (version.toString().equalsIgnoreCase("V2.0")) {
			list.add(new DecNumber(buffer, offset + 114, 1, "Spell duration rounds/level"));
			list.add(new DecNumber(buffer, offset + 115, 1, "Spell duration rounds base"));
			list.add(new Unknown(buffer, offset + 116, 14));
		}

		offset = abil_offset.getValue();
		Ability abilities[] = new Ability[abil_count.getValue()];
		for (int i = 0; i < abilities.length; i++) {
			abilities[i] = new Ability(this, buffer, offset, i);
			list.add(abilities[i]);
			offset = abilities[i].getEndOffset();
		}

		int offset2 = global_offset.getValue();
		for (int i = 0; i < global_count.getValue(); i++) {
			Effect eff = new Effect(this, buffer, offset2, i);
			offset2 = eff.getEndOffset();
			list.add(eff);
		}

		for (final Ability ability : abilities)
			offset2 = ability.readEffects(buffer, offset2);

		return Math.max(offset, offset2);
	}

}
