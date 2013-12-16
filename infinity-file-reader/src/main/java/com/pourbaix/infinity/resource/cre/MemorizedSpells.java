package com.pourbaix.infinity.resource.cre;

import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.Unknown;

final class MemorizedSpells extends AbstractStruct {
	private static final String[] s_mem = { "Spell already cast", "Spell memorized", "Spell disabled" };

	MemorizedSpells() throws Exception {
		super(null, "Memorized spell", new byte[12], 0);
	}

	MemorizedSpells(AbstractStruct superStruct, byte buffer[], int offset) throws Exception {
		super(superStruct, "Memorized spell", buffer, offset);
	}

	@Override
	protected int read(byte buffer[], int offset) throws Exception {
		list.add(new ResourceRef(buffer, offset, "Spell", "SPL"));
		list.add(new Bitmap(buffer, offset + 8, 2, "Memorization", s_mem));
		list.add(new Unknown(buffer, offset + 10, 2));
		return offset + 12;
	}
}
