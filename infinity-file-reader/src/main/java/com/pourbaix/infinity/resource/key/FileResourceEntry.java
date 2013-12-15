package com.pourbaix.infinity.resource.key;

import java.io.File;

public final class FileResourceEntry extends BaseResourceEntry {
	private File file;

	public FileResourceEntry(File file) {
		this.resourceName = file.getName().toUpperCase();
		this.extension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
		this.file = file;
	}

	@Override
	public byte[] getResourceData() throws Exception {
		return null;
	}

	public File getFile() {
		return file;
	}

}
