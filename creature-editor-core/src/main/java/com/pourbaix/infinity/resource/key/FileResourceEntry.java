package com.pourbaix.infinity.resource.key;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.pourbaix.infinity.util.Filereader;

public final class FileResourceEntry extends BaseResourceEntry {
	private File file;

	public FileResourceEntry(File file) {
		this.resourceName = file.getName().toUpperCase();
		this.extension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
		this.file = file;
	}

	@Override
	public byte[] getResourceData() throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		byte buffer[] = Filereader.readBytes(is, (int) file.length());
		is.close();
		return buffer;
	}

	public File getFile() {
		return file;
	}

}
