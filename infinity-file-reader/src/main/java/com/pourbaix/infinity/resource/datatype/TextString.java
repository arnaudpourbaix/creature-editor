package com.pourbaix.infinity.resource.datatype;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.Filewriter;

public final class TextString extends Datatype {
	private final byte bytes[];
	private String text;

	public TextString(byte buffer[], int offset, int length, String name) {
		super(offset, length, name);
		bytes = Arrays.copyOfRange(buffer, offset, offset + length);
	}

	@Override
	public void write(OutputStream os) throws IOException {
		if (text == null)
			Filewriter.writeBytes(os, bytes);
		else
			Filewriter.writeString(os, text, getSize());
	}

	@Override
	public String toString() {
		if (text == null)
			text = DynamicArray.getString(bytes, 0, bytes.length);
		return text;
	}
}
