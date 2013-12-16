package com.pourbaix.infinity.resource.cre;

import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.StructEntry;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.DecNumber;

final class SpellMemorization extends AbstractStruct {
	private static final String[] s_spelltype = { "Priest", "Wizard", "Innate" };

	SpellMemorization() throws Exception {
		super(null, "Memorization info", new byte[16], 0);
	}

	SpellMemorization(AbstractStruct superStruct, byte buffer[], int offset, int nr) throws Exception {
		super(superStruct, "Memorization info " + nr, buffer, offset);
	}

	@Override
	public int read(byte buffer[], int offset) {
		list.add(new DecNumber(buffer, offset, 2, "Spell level"));
		list.add(new DecNumber(buffer, offset + 2, 2, "# spells memorizable"));
		list.add(new DecNumber(buffer, offset + 4, 2, "# currently memorizable"));
		list.add(new Bitmap(buffer, offset + 6, 2, "Type", s_spelltype));
		list.add(new DecNumber(buffer, offset + 8, 4, "Spell table index"));
		list.add(new DecNumber(buffer, offset + 12, 4, "Spell count"));
		return offset + 16;
	}

	public void readMemorizedSpells(byte buffer[], int offset) throws Exception {
		DecNumber firstSpell = (DecNumber) getAttribute("Spell table index");
		DecNumber numSpell = (DecNumber) getAttribute("Spell count");
		for (int i = 0; i < numSpell.getValue(); i++)
			list.add(new MemorizedSpells(this, buffer, offset + 12 * (firstSpell.getValue() + i)));
	}

	public int updateSpells(int offset, int startIndex) {
		((DecNumber) getAttribute("Spell table index")).setValue(startIndex);
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			StructEntry entry = list.get(i);
			if (entry instanceof MemorizedSpells) {
				entry.setOffset(offset);
				((AbstractStruct) entry).realignStructOffsets();
				offset += 12;
				count++;
			}
		}
		((DecNumber) getAttribute("Spell count")).setValue(count);
		return count;
	}
}
