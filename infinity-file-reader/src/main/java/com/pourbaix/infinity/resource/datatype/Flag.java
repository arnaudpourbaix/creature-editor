package com.pourbaix.infinity.resource.datatype;

import java.io.IOException;
import java.io.OutputStream;

import com.pourbaix.infinity.util.DynamicArray;

public class Flag extends Datatype {
	String nodesc;
	String[] table;
	private long value;

	Flag(byte buffer[], int offset, int length, String name) {
		super(offset, length, name);
		if (length == 4)
			value = (long) DynamicArray.getInt(buffer, offset);
		else if (length == 2)
			value = (long) DynamicArray.getShort(buffer, offset);
		else if (length == 1)
			value = (long) DynamicArray.getByte(buffer, offset);
		else
			throw new IllegalArgumentException();
	}

	public Flag(byte buffer[], int offset, int length, String name, String[] stable) {
		this(buffer, offset, length, name);
		nodesc = stable[0];
		table = new String[8 * length];
		for (int i = 1; i < stable.length; i++)
			table[i - 1] = stable[i];
	}

	@Override
	public void write(OutputStream os) throws IOException {
		super.writeLong(os, value);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("( ");
		if (value == 0)
			sb.append(nodesc).append(' ');
		else {
			for (int i = 0; i < 8 * getSize(); i++)
				if (isFlagSet(i)) {
					if (i < table.length && table[i] != null && !table[i].equals(""))
						sb.append(table[i]).append('(').append(i).append(") ");
					else
						sb.append("Unknown(").append(i).append(") ");
				}
		}
		sb.append(')');
		return sb.toString();
	}

	public String getString(int i) {
		return table[i];
	}

	public boolean isFlagSet(int i) {
		long bitnr = (long) Math.pow((double) 2, (double) i);
		return (value & bitnr) == bitnr;
	}

}
