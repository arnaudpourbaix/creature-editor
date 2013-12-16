package com.pourbaix.infinity.resource.cre;

import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.Flag;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.Unknown;

public final class Item extends AbstractStruct {
	private static final String[] s_itemflag = { "No flags set", "Identified", "Not stealable", "Stolen", "Undroppable" };

	public Item() throws Exception {
		super(null, "Item", new byte[20], 0);
	}

	public Item(AbstractStruct superStruct, byte buffer[], int offset, int nr) throws Exception {
		super(superStruct, "Item " + nr, buffer, offset);
	}

	@Override
	protected int read(byte buffer[], int offset) throws Exception {
		list.add(new ResourceRef(buffer, offset, "Item", "ITM"));
		list.add(new Unknown(buffer, offset + 8, 2));
		list.add(new DecNumber(buffer, offset + 10, 2, "Quantity/Charges 1"));
		list.add(new DecNumber(buffer, offset + 12, 2, "Quantity/Charges 2"));
		list.add(new DecNumber(buffer, offset + 14, 2, "Quantity/Charges 3"));
		list.add(new Flag(buffer, offset + 16, 4, "Flags", s_itemflag));
		return offset + 20;
	}
}
