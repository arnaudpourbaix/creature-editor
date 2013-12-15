package com.pourbaix.infinity.resource.key;

import java.io.File;

public final class FileResourceEntry extends BaseResourceEntry {
	private final boolean override;
	private File file;

	public FileResourceEntry(File file) {
		this(file, false);
	}

	public FileResourceEntry(File file, boolean override) {
		this.file = file;
		this.override = override;
	}

	@Override
	public String toString() {
		return file.getName().toUpperCase();
	}

	@Override
	public String getExtension() {
		return file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
	}

	@Override
	public String getResourceName() {
		return file.getName();
	}

	@Override
	public boolean isOverride() {
		return override;
	}

}
