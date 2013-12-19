package com.pourbaix.infinity.resource.key;

import java.io.File;
import java.io.IOException;

import com.pourbaix.infinity.util.Decryptor;

public abstract class BaseResourceEntry implements ResourceEntry, Comparable<BaseResourceEntry> {

	protected String resourceName;
	protected String extension;
	protected File overrideFile;

	@Override
	public int compareTo(BaseResourceEntry entry) {
		return resourceName.compareToIgnoreCase(entry.resourceName);
	}

	public abstract byte[] getResourceData() throws IOException;

	public String getResourceTextData() throws IOException {
		byte[] data = getResourceData();
		String text;
		if (data != null && data.length > 1 && data[0] == -1) {
			text = Decryptor.decrypt(data, 2, data.length);
		} else {
			text = new String(data);
		}
		return text;
	}

	public String toString() {
		return resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getExtension() {
		return extension;
	}

	public File getOverrideFile() {
		return overrideFile;
	}

	public void setOverrideFile(File overrideFile) {
		this.overrideFile = overrideFile;
	}

	public String getSearchString() {
		return ""; // FIXME
	}

}