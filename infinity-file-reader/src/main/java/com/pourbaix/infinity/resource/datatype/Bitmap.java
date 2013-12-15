// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2005 Jon Olav Hauglid
// See LICENSE.txt for license information

package com.pourbaix.infinity.resource.datatype;

import com.pourbaix.infinity.util.DynamicArray;

public class Bitmap extends Datatype {
	private final String table[];
	private int value;

	public Bitmap(byte buffer[], int offset, int length, String name, String[] table) {
		super(offset, length, name);
		this.table = table;
		if (length == 4)
			value = DynamicArray.getInt(buffer, offset);
		else if (length == 2)
			value = (int) DynamicArray.getShort(buffer, offset);
		else if (length == 1)
			value = (int) DynamicArray.getByte(buffer, offset);
		else
			throw new IllegalArgumentException();
	}

	@Override
	public String toString() {
		return getString(value);
	}

	public int getValue() {
		return value;
	}

	private String getString(int nr) {
		if (nr >= table.length)
			return "Unknown (" + nr + ')';
		if (nr < 0)
			return "Error (" + nr + ')';
		if (table[nr].equals(""))
			return "Unknown (" + nr + ')';
		return new StringBuffer(table[nr]).append(" (").append(nr).append(')').toString();
	}
}
