package com.pourbaix.infinity.datatype;

import java.util.Arrays;

import com.pourbaix.infinity.util.DynamicArray;

public final class TextString extends Datatype {
	private final byte bytes[];
	private String text;

	public TextString(byte buffer[], int offset, int length, String name) {
		super(offset, length, name);
		bytes = Arrays.copyOfRange(buffer, offset, offset + length);
	}

	@Override
	public String toString() {
		if (text == null)
			text = DynamicArray.getString(bytes, 0, bytes.length);
		return text;
	}
}
