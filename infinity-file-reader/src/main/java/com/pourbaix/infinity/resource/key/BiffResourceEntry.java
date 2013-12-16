package com.pourbaix.infinity.resource.key;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.Filereader;

public class BiffResourceEntry extends BaseResourceEntry {
	private final int type;
	private int locator;

	public BiffResourceEntry(byte buffer[], int offset, int stringLength) {
		StringBuffer sb = new StringBuffer(DynamicArray.getString(buffer, offset, stringLength));
		type = (int) DynamicArray.getShort(buffer, offset + stringLength);
		locator = DynamicArray.getInt(buffer, offset + stringLength + 2);
		extension = Keyfile.EXTENSIONS.get(type);
		if (extension != null) {
			sb.append('.').append(extension);
		}
		resourceName = sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BiffResourceEntry))
			return false;
		BiffResourceEntry other = (BiffResourceEntry) o;
		return locator == other.locator && resourceName.equals(other.resourceName) && type == other.type;
	}

	@Override
	public byte[] getResourceData() throws Exception {
		if (overrideFile != null) {
			InputStream is = new BufferedInputStream(new FileInputStream(overrideFile));
			byte buffer[] = Filereader.readBytes(is, (int) overrideFile.length());
			is.close();
			return buffer;
		}
		BiffArchive biff = Keyfile.getInstance().getBIFFFile(getBIFFEntry());
		if (type == 0x3eb) // TIS
			return biff.getResource(locator >> 14 & 0x3f, true);
		return biff.getResource(locator & 0x3fff, false);
	}

	public BiffEntry getBIFFEntry() {
		int sourceindex = locator >> 20 & 0xfff;
		return Keyfile.getInstance().getBIFFEntry(sourceindex);
	}

}
