package com.pourbaix.infinity.resource.key;

import java.io.File;

import com.pourbaix.infinity.resource.ResourceFactory;
import com.pourbaix.infinity.util.DynamicArray;

public final class BiffEntry implements Comparable<BiffEntry> {
	private final short location;
	private String filename;
	private int index, filelength, stringoffset;
	private short stringlength;

	public BiffEntry(String filename) {
		this.filename = filename;
		// Location: Indicates where file might be found
		// Bit 1: Data or movies (LSB)
		// Bit 2: ???
		// Bit 3: ??? (CD1-directory?)
		// Bit 4: CD2-directory
		// Bit 5: CD3-directory
		// Bit 6: CD4-directory
		// Bit 7: CD5-directory
		// Bit 8: ??? (Doesn't exist?) (MSB)
		location = (short) 1; // Data or movies
		index = -1; // Not put into keyfile yet
	}

	BiffEntry(int index, byte buffer[], int offset, boolean usesShortFormat) {
		this.index = index;
		stringoffset = DynamicArray.getInt(buffer, offset);
		stringlength = DynamicArray.getShort(buffer, offset + 4);
		location = DynamicArray.getShort(buffer, offset + 6);
		filename = new String(buffer, stringoffset, (int) stringlength - 1);
		if (filename.startsWith("\\"))
			filename = filename.substring(1);
		filename = filename.replace('\\', '/').replace(':', '/');
	}

	BiffEntry(int index, byte buffer[], int offset) {
		this.index = index;
		filelength = DynamicArray.getInt(buffer, offset);
		stringoffset = DynamicArray.getInt(buffer, offset + 4);
		stringlength = DynamicArray.getShort(buffer, offset + 8);
		location = DynamicArray.getShort(buffer, offset + 10);
		filename = new String(buffer, stringoffset, (int) stringlength - 1);
		if (filename.startsWith("\\"))
			filename = filename.substring(1);
		filename = filename.replace('\\', '/').replace(':', '/');
	}

	@Override
	public int compareTo(BiffEntry o) {
		return filename.compareTo(o.filename);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BiffEntry))
			return false;
		BiffEntry other = (BiffEntry) o;
		return filelength == other.filelength && stringoffset == other.stringoffset && stringlength == other.stringlength && location == other.location
				&& filename.equals(other.filename);
	}

	@Override
	public String toString() {
		return filename;
	}

	public File getFile() {
		File file = ResourceFactory.getInstance().getFile(filename);
		return file;
	}

	public int getIndex() {
		return index;
	}

	void setIndex(int index) {
		this.index = index;
	}

	public void setFileLength(int filelength) {
		this.filelength = filelength;
	}

}
