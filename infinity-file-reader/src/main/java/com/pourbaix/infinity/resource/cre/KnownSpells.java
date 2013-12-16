package com.pourbaix.infinity.resource.cre;

import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.ResourceRef;

final class KnownSpells extends AbstractStruct {
	private static final String[] s_spelltype = { "Priest", "Wizard", "Innate" };

	KnownSpells() throws Exception {
		super(null, "Known spell", new byte[12], 0);
	}

	KnownSpells(AbstractStruct superStruct, byte buffer[], int offset, int number) throws Exception {
		super(superStruct, "Known spell " + number, buffer, offset);
	}

	@Override
	protected int read(byte buffer[], int offset) throws Exception {
		list.add(new ResourceRef(buffer, offset, "Spell", "SPL"));
		list.add(new DecNumber(buffer, offset + 8, 2, "Level"));
		list.add(new Bitmap(buffer, offset + 10, 2, "Type", s_spelltype));
		return offset + 12;
	}
}
