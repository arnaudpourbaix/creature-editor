package com.pourbaix.infinity.resource.datatype;

import com.pourbaix.infinity.util.IdsMapCache;
import com.pourbaix.infinity.util.IdsMapEntry;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public final class IdsFlag extends Flag {
	public IdsFlag(byte buffer[], int offset, int length, String name, String resource) {
		super(buffer, offset, length, name);
		LongIntegerHashMap<IdsMapEntry> idsmap = IdsMapCache.get(resource).getMap();
		nodesc = idsmap.get(0L).getString();
		table = new String[8 * length];
		for (int i = 0; i < table.length; i++)
			table[i] = idsmap.get((long) Math.pow(2, i)).getString();
	}
}
