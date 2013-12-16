package com.pourbaix.infinity.resource.datatype;

import java.io.IOException;
import java.io.OutputStream;

import com.pourbaix.infinity.util.DynamicArray;

public final class UnsignDecNumber extends Datatype {
	private long number;

	public UnsignDecNumber(byte buffer[], int offset, int length, String name) {
		super(offset, length, name);
		number = (long) 0;
		if (length == 4)
			number = DynamicArray.getUnsignedInt(buffer, offset);
		else if (length == 2)
			number = (long) DynamicArray.getUnsignedShort(buffer, offset);
		else if (length == 1)
			number = (long) DynamicArray.getUnsignedByte(buffer, offset);
		else
			throw new IllegalArgumentException();
	}

	@Override
	public void write(OutputStream os) throws IOException {
		super.writeLong(os, number);
	}

	@Override
	public String toString() {
		return Long.toString(number);
	}

	public long getValue() {
		return number;
	}
}
