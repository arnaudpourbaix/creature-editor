package com.pourbaix.infinity.resource.datatype;

public class HexNumber extends DecNumber {
	public HexNumber(byte buffer[], int offset, int length, String desc) {
		super(buffer, offset, length, desc);
	}

	@Override
	public String toString() {
		return Integer.toHexString(getValue()) + " h";
	}
}
