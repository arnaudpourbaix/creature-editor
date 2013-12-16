package com.pourbaix.infinity.resource.datatype;

import java.io.IOException;
import java.io.OutputStream;

import com.pourbaix.infinity.util.DynamicArray;

public final class ColorValue extends Datatype {
	private int number;

	@Override
	public void write(OutputStream os) throws IOException {
		super.writeInt(os, number);
	}

	public ColorValue(byte buffer[], int offset, int length, String name) {
		super(offset, length, name);
		if (length == 4)
			number = DynamicArray.getInt(buffer, offset);
		else if (length == 2)
			number = (int) DynamicArray.getShort(buffer, offset);
		else if (length == 1)
			number = (int) DynamicArray.getByte(buffer, offset);
		else
			throw new IllegalArgumentException();
		if (number < 0)
			number += 256;
	}

	@Override
	public String toString() {
		return "Color index " + number;
	}

}
