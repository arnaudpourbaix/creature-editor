package com.pourbaix.infinity.resource.key;

import com.pourbaix.infinity.util.DynamicArray;

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
		//		if (override.exists() && !override.isDirectory()) {
		//			InputStream is = new BufferedInputStream(new FileInputStream(override));
		//			byte buffer[] = Filereader.readBytes(is, (int) override.length());
		//			is.close();
		//			return buffer;
		//		}
		//		BIFFArchive biff = ResourceFactory.getKeyfile().getBIFFFile(getBIFFEntry());
		//		if (type == 0x3eb) // TIS
		//			return biff.getResource(locator >> 14 & 0x3f, true);
		//		return biff.getResource(locator & 0x3fff, false);
		return null;
	}

}
