package com.pourbaix.infinity.resource.datatype;

import java.io.IOException;
import java.io.OutputStream;

import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public class HashBitmap extends Datatype {
	private final LongIntegerHashMap<String> idsmap;
	private long value;

	public HashBitmap(byte buffer[], int offset, int length, String name, LongIntegerHashMap<String> idsmap) {
		super(offset, length, name);
		this.idsmap = new LongIntegerHashMap<String>(idsmap);

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
	public void write(OutputStream os) throws IOException {
		super.writeLong(os, value);
	}

	@Override
	public String toString() {
		Object o = idsmap.get(value);
		if (o == null)
			return "Unknown - " + value;
		return idsmap.get(value).toString() + " - " + value;
	}

	public long getValue() {
		return value;
	}
}
