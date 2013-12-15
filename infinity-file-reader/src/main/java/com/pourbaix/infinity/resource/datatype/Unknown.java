package com.pourbaix.infinity.resource.datatype;

import java.util.Arrays;

public class Unknown extends Datatype {
	private static final String UNKNOWN = "Unknown";
	byte[] data;

	public Unknown(byte[] buffer, int offset, int length) {
		super(offset, length, UNKNOWN);
		data = Arrays.copyOfRange(buffer, offset, offset + length);
	}

	public Unknown(byte[] buffer, int offset, int length, String name) {
		super(offset, length, name);
		data = Arrays.copyOfRange(buffer, offset, offset + length);
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public String toString() {
		if (data != null && data.length > 0) {
			StringBuffer sb = new StringBuffer(3 * data.length + 1);
			for (final byte d : data) {
				String text = Integer.toHexString((int) d);
				if (text.length() == 1)
					sb.append('0');
				else if (text.length() > 2)
					text = text.substring(text.length() - 2);
				sb.append(text).append(' ');
			}
			sb.append('h');
			return sb.toString();
		} else
			return new String();
	}
}
