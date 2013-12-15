package com.pourbaix.infinity.resource.datatype;

import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.IdsMapCache;
import com.pourbaix.infinity.util.IdsMapEntry;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public final class IdsBitmap extends Datatype {
	private final LongIntegerHashMap<IdsMapEntry> idsmap;
	private long value;

	public IdsBitmap(byte buffer[], int offset, int length, String name, String resource) {
		super(offset, length, name);
		idsmap = IdsMapCache.get(resource).getMap();

		if (length == 4)
			value = DynamicArray.getUnsignedInt(buffer, offset);
		else if (length == 2)
			value = (long) DynamicArray.getUnsignedShort(buffer, offset);
		else if (length == 1)
			value = (long) DynamicArray.getUnsignedByte(buffer, offset);
		else
			throw new IllegalArgumentException();
	}

	public IdsBitmap(byte buffer[], int offset, int length, String name, String resource, int idsStart) {
		super(offset, length, name);
		LongIntegerHashMap<IdsMapEntry> orgmap = IdsMapCache.get(resource).getMap();
		idsmap = new LongIntegerHashMap<IdsMapEntry>();

		long[] keys = orgmap.keys();
		for (final long id : keys) {
			if (id >= idsStart) {
				IdsMapEntry entry = orgmap.get(id);
				long newid = id - (long) idsStart;
				idsmap.put(newid, new IdsMapEntry(newid, entry.getString(), entry.getParameters()));
			}
		}

		if (length == 4)
			value = DynamicArray.getUnsignedInt(buffer, offset);
		else if (length == 2)
			value = (long) DynamicArray.getUnsignedShort(buffer, offset);
		else if (length == 1)
			value = (long) DynamicArray.getUnsignedByte(buffer, offset);
		else
			throw new IllegalArgumentException();
	}

	@Override
	public String toString() {
		Object o = idsmap.get(value);
		if (o == null)
			return "Unknown - " + value;
		return idsmap.get(value).toString(); // + "(" + value + ")";
	}

	public long getValue() {
		return value;
	}
}
