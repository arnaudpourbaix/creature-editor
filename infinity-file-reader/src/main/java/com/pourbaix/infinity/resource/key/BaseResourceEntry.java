package com.pourbaix.infinity.resource.key;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.FileReader;

public abstract class BaseResourceEntry implements ResourceEntry, Comparable<BaseResourceEntry> {

	static int[] getLocalFileInfo(File file) {
		if (file.getName().toUpperCase().endsWith(".TIS")) {
			try {
				InputStream is = new BufferedInputStream(new FileInputStream(file));
				byte data[] = FileReader.readBytes(is, 24);
				is.close();
				if (!new String(data, 0, 4).equalsIgnoreCase("TIS ")) {
					int tilesize = 64 * 64 + 4 * 256;
					int tilecount = (int) file.length() / tilesize;
					return new int[] { tilecount, tilesize };
				}
				return new int[] { DynamicArray.getInt(data, 8), DynamicArray.getInt(data, 12) };
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return new int[] { (int) file.length() };
	}

	@Override
	public int compareTo(BaseResourceEntry entry) {
		return getResourceName().compareToIgnoreCase(entry.getResourceName());
	}

	public abstract String getResourceName();

	public abstract String getExtension();

	public abstract boolean isOverride();

	//	public File getActualFile() {
	//		return getActualFile(false);
	//	}
	//
	//	public byte[] getResourceData() throws Exception {
	//		return getResourceData(false);
	//	}
	//
	//	public InputStream getResourceDataAsStream() throws Exception {
	//		return getResourceDataAsStream(false);
	//	}
	//
	//	public int[] getResourceInfo() throws Exception {
	//		return getResourceInfo(false);
	//	}
	//
	//	public String getSearchString() {
	//		if (searchString == null) {
	//			try {
	//				String extension = getExtension();
	//				if (extension.equalsIgnoreCase("CRE"))
	//					searchString = CreResource.getSearchString(getResourceData());
	//				else if (extension.equalsIgnoreCase("ITM"))
	//					searchString = ItmResource.getSearchString(getResourceData());
	//				else if (extension.equalsIgnoreCase("SPL"))
	//					searchString = SplResource.getSearchString(getResourceData());
	//				else if (extension.equalsIgnoreCase("STO"))
	//					searchString = StoResource.getSearchString(getResourceData());
	//			} catch (Exception e) {
	//				if (!globalContext.isIgnoreReadErrors()) {
	//					throw new Exception("Error reading " + toString(), e);
	//				}
	//			}
	//		}
	//		return searchString;
	//	}
	//
	//	protected abstract File getActualFile(boolean ignoreoverride);
	//
	//	public abstract byte[] getResourceData(boolean ignoreoverride) throws Exception;
	//
	//	protected abstract InputStream getResourceDataAsStream(boolean ignoreoverride) throws Exception;
	//
	//	public abstract int[] getResourceInfo(boolean ignoreoverride) throws Exception;
	//

}