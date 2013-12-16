package com.pourbaix.infinity.resource.datatype;

import java.io.IOException;
import java.io.OutputStream;

import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.StringResourceException;
import com.pourbaix.infinity.util.DynamicArray;

public final class StringRef extends Datatype {
	private int value;
	private String text;

	public StringRef(String name, int value) {
		super(0, 8, name);
		this.value = value;
	}

	public StringRef(byte buffer[], int offset, String name) {
		super(offset, 4, name);
		value = DynamicArray.getInt(buffer, offset);
	}

	@Override
	public void write(OutputStream os) throws IOException {
		super.writeInt(os, value);
	}

	@Override
	public String toString() {
		try {
			return StringResource.getInstance().getStringRef(value);
		} catch (StringResourceException e) {
			return null;
		}
	}

	public void setValue(int newvalue) throws StringResourceException {
		value = newvalue;
		text = StringResource.getInstance().getStringRef(value);
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

}
