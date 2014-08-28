package com.pourbaix.infinity.datatype;

public interface StructEntry extends Comparable<StructEntry> {

	void copyNameAndOffset(StructEntry fromEntry);

	String getName();

	int getOffset();

	int getSize();

	void setOffset(int newoffset);
}
