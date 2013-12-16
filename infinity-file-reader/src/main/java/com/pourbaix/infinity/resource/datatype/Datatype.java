package com.pourbaix.infinity.resource.datatype;

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;

import com.pourbaix.infinity.resource.StructEntry;
import com.pourbaix.infinity.util.Filewriter;

public abstract class Datatype implements StructEntry {
	protected static final Dimension DIM_BROAD = new Dimension(650, 100);
	protected static final Dimension DIM_MEDIUM = new Dimension(400, 100);
	private final int length;
	private String name;
	private int offset;

	protected Datatype(int offset, int length, String name) {
		this.offset = offset;
		this.length = length;
		this.name = name;
	}

	@Override
	public int compareTo(StructEntry o) {
		return offset - o.getOffset();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public void copyNameAndOffset(StructEntry entry) {
		name = entry.getName();
		offset = entry.getOffset();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getSize() {
		return length;
	}

	@Override
	public void setOffset(int newoffset) {
		offset = newoffset;
	}

	void writeInt(OutputStream os, int value) throws IOException {
		if (getSize() == 4)
			Filewriter.writeInt(os, value);
		else if (getSize() == 3)
			Filewriter.writeInt24(os, value);
		else if (getSize() == 2)
			Filewriter.writeShort(os, (short) value);
		else if (getSize() == 1)
			Filewriter.writeByte(os, (byte) value);
		else
			throw new IllegalArgumentException();
	}

	void writeLong(OutputStream os, long value) throws IOException {
		if (getSize() == 4)
			Filewriter.writeInt(os, (int) value);
		else if (getSize() == 3)
			Filewriter.writeInt24(os, (int) value);
		else if (getSize() == 2)
			Filewriter.writeShort(os, (short) value);
		else if (getSize() == 1)
			Filewriter.writeByte(os, (byte) value);
		else
			throw new IllegalArgumentException();
	}

}
