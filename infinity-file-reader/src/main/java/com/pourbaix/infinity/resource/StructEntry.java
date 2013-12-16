package com.pourbaix.infinity.resource;

public interface StructEntry extends Comparable<StructEntry>, Writeable {

	void copyNameAndOffset(StructEntry fromEntry);

	String getName();

	int getOffset();

	int getSize();

	void setOffset(int newoffset);
}
