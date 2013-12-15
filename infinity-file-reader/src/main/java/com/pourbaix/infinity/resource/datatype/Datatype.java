package com.pourbaix.infinity.resource.datatype;

import java.awt.Dimension;

import com.pourbaix.infinity.resource.StructEntry;

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

}
