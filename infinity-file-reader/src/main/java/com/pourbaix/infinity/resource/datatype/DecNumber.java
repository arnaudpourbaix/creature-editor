package com.pourbaix.infinity.resource.datatype;

import java.io.IOException;
import java.io.OutputStream;

import com.pourbaix.infinity.util.DynamicArray;

public class DecNumber extends Datatype {
	private int number;

	public DecNumber(byte buffer[], int offset, int length, String name) {
		super(offset, length, name);
		number = 0;
		if (length == 4)
			number = DynamicArray.getInt(buffer, offset);
		else if (length == 2)
			number = (int) DynamicArray.getShort(buffer, offset);
		else if (length == 1)
			number = (int) DynamicArray.getByte(buffer, offset);
		else
			throw new IllegalArgumentException();
	}

	@Override
	public void write(OutputStream os) throws IOException {
		super.writeInt(os, number);
	}

	@Override
	public String toString() {
		return Integer.toString(number);
	}

	public int getValue() {
		return number;
	}

	public void incValue(int value) {
		number += value;
	}

	public void setValue(int value) {
		number = value;
	}

}
