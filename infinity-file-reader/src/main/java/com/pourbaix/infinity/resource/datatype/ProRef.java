package com.pourbaix.infinity.resource.datatype;

import java.io.IOException;
import java.io.OutputStream;

import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.IdsMapCache;
import com.pourbaix.infinity.util.IdsMapEntry;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public final class ProRef extends Datatype {
	private static final String NONE = "None";
	private final LongIntegerHashMap<IdsMapEntry> idsmap;
	private long value;

	public ProRef(byte buffer[], int offset, String name) {
		super(offset, 2, name);
		idsmap = IdsMapCache.get("PROJECTL.IDS").getMap();
		value = (long) DynamicArray.getUnsignedShort(buffer, offset);
	}

	@Override
	public void write(OutputStream os) throws IOException {
		super.writeLong(os, value);
	}

	@Override
	public String toString() {
		if (idsmap.containsKey(value - (long) 1))
			return Keyfile.getInstance().getResourceEntry(idsmap.get(value - 1).getString() + ".PRO") + " (" + value + ')';
		return NONE + " (" + value + ')';
	}

	public ResourceEntry getSelectedEntry() {
		if (!idsmap.containsKey(value - (long) 1))
			return null;
		return Keyfile.getInstance().getResourceEntry(((IdsMapEntry) idsmap.get(value - 1)).getString() + ".PRO");
	}

}
