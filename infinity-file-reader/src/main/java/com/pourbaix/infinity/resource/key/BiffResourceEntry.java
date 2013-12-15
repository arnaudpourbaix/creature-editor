package com.pourbaix.infinity.resource.key;

import com.pourbaix.infinity.util.DynamicArray;

public class BiffResourceEntry extends BaseResourceEntry {
	private final String resourceName;
	private final int type;
	private boolean override = false;
	private int locator;

	//	public BIFFResourceEntry(BIFFEntry bifentry, String resourcename, int offset) {
	//		this.resourceName = resourcename;
	//		type = ResourceFactory.getKeyfile().getExtensionType(resourcename.substring(resourcename.indexOf((int) '.') + 1));
	//		int bifindex = bifentry.getIndex();
	//		locator = bifindex << 20;
	//		if (type == 0x3eb) // TIS
	//			locator |= offset << 14;
	//		else
	//			locator |= offset;
	//	}

	public BiffResourceEntry(byte buffer[], int offset, int stringLength) {
		StringBuffer sb = new StringBuffer(DynamicArray.getString(buffer, offset, stringLength));
		type = (int) DynamicArray.getShort(buffer, offset + stringLength);
		locator = DynamicArray.getInt(buffer, offset + stringLength + 2);
		String extension = getExtension();
		if (extension != null) {
			sb.append('.').append(extension);
		}
		resourceName = sb.toString();
	}

	@Override
	public String getResourceName() {
		return resourceName;
	}

	@Override
	public boolean isOverride() {
		//		File override = NIFile.getFile(ResourceFactory.getRootDirs(), ResourceFactory.OVERRIDEFOLDER + File.separatorChar + resourceName);
		//		hasOverride = override.exists() && !override.isDirectory();
		//		return hasOverride;
		return override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BiffResourceEntry))
			return false;
		BiffResourceEntry other = (BiffResourceEntry) o;
		return locator == other.locator && resourceName.equals(other.resourceName) && type == other.type;
	}

	@Override
	public String toString() {
		return resourceName;
	}

	@Override
	public String getExtension() {
		String ext = Keyfile.EXTENSIONS.get(type);
		return ext;
	}

	//	@Override
	//	public File getActualFile(boolean ignoreoverride) {
	//		if (!ignoreoverride) {
	//			File override = NIFile.getFile(ResourceFactory.getRootDirs(), ResourceFactory.OVERRIDEFOLDER + File.separatorChar + resourceName);
	//			if (override.exists() && !override.isDirectory())
	//				return override;
	//		}
	//		try {
	//			return ResourceFactory.getKeyfile().getBIFFFile(getBIFFEntry()).getFile();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}
	//
	//	public BIFFEntry getBIFFEntry() {
	//		int sourceindex = locator >> 20 & 0xfff;
	//		return ResourceFactory.getKeyfile().getBIFFEntry(sourceindex);
	//	}
	//
	//
	//	public int getLocator() {
	//		return locator;
	//	}
	//
	//	@Override
	//	public byte[] getResourceData(boolean ignoreoverride) throws Exception {
	//		if (!ignoreoverride) {
	//			File override = NIFile.getFile(ResourceFactory.getRootDirs(), ResourceFactory.OVERRIDEFOLDER + File.separatorChar + resourceName);
	//			if (override.exists() && !override.isDirectory()) {
	//				InputStream is = new BufferedInputStream(new FileInputStream(override));
	//				byte buffer[] = Filereader.readBytes(is, (int) override.length());
	//				is.close();
	//				return buffer;
	//			}
	//		}
	//		BIFFArchive biff = ResourceFactory.getKeyfile().getBIFFFile(getBIFFEntry());
	//		if (type == 0x3eb) // TIS
	//			return biff.getResource(locator >> 14 & 0x3f, true);
	//		return biff.getResource(locator & 0x3fff, false);
	//	}
	//
	//	@Override
	//	public InputStream getResourceDataAsStream(boolean ignoreoverride) throws Exception {
	//		if (!ignoreoverride) {
	//			File override = NIFile.getFile(ResourceFactory.getRootDirs(), ResourceFactory.OVERRIDEFOLDER + File.separatorChar + resourceName);
	//			if (override.exists() && !override.isDirectory()) {
	//				return new BufferedInputStream(new FileInputStream(override));
	//			}
	//		}
	//		BIFFArchive biff = ResourceFactory.getKeyfile().getBIFFFile(getBIFFEntry());
	//		if (type == 0x3eb) // TIS
	//			return biff.getResourceAsStream(locator >> 14 & 0x3f, true);
	//		return biff.getResourceAsStream(locator & 0x3fff, false);
	//	}
	//
	//	@Override
	//	public int[] getResourceInfo(boolean ignoreoverride) throws IOException {
	//		if (!ignoreoverride) {
	//			File override = NIFile.getFile(ResourceFactory.getRootDirs(), ResourceFactory.OVERRIDEFOLDER + File.separatorChar + resourceName);
	//			if (override.exists() && !override.isDirectory())
	//				return getLocalFileInfo(override);
	//		}
	//		BIFFArchive biff = ResourceFactory.getKeyfile().getBIFFFile(getBIFFEntry());
	//		if (type == 0x3eb) // TIS
	//			return biff.getResourceInfo(locator >> 14 & 0x3f, true);
	//		return biff.getResourceInfo(locator & 0x3fff, false);
	//	}
	//
	//	@Override
	//	public String getResourceName() {
	//		return resourceName;
	//	}
	//
	//	@Override
	//	public String getTreeFolder() {
	//		if (BrowserMenuBar.getInstance() != null && BrowserMenuBar.getInstance().getOverrideMode() == BrowserMenuBar.OVERRIDE_IN_OVERRIDE && hasOverride())
	//			return ResourceFactory.OVERRIDEFOLDER;
	//		return getExtension();
	//	}
	//
	//	public int getType() {
	//		return type;
	//	}
	//
	//
	//	public void setOverride(boolean hasOverride) {
	//		this.hasOverride = hasOverride;
	//	}
	//
	//	void adjustSourceIndex(int index) {
	//		int sourceindex = locator >> 20 & 0xfff;
	//		if (sourceindex > index) {
	//			sourceindex--;
	//			locator = sourceindex << 20 | locator & 0xfffff;
	//		}
	//	}

}
