package com.pourbaix.infinity.resource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;

import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.Filereader;

public class StringResource {

	private static StringResource instance;

	private File file;
	private RandomAccessFile randomAccessFile;
	private String version;
	private int maxnr, startindex;
	private Charset charset = Charset.forName("windows-1252");

	public void init(File file, boolean enhancedEdition) throws StringResourceException {
		this.file = file;
		if (enhancedEdition) {
			charset = Charset.forName("utf8");
		}
		close();
	}

	public static StringResource getInstance() {
		if (StringResource.instance == null) {
			StringResource.instance = new StringResource();
		}
		return StringResource.instance;
	}

	public void close() throws StringResourceException {
		if (randomAccessFile == null) {
			return;
		}
		try {
			randomAccessFile.close();
		} catch (IOException e) {
			throw new StringResourceException("Error when closing file " + this.randomAccessFile, e);
		}
	}

	public String getStringRef(byte buffer[], int offset) throws StringResourceException {
		int index = DynamicArray.getInt(buffer, offset);
		return getStringRef(index);
	}

	public String getStringRef(int index) throws StringResourceException {
		if (index == -1) {
			return null;
		}
		try {
			if (randomAccessFile == null) {
				open();
			}
			if (index >= maxnr || index < 0) {
				// throw new StringResourceException("No such index");
				return null;
			}
			if (version.equalsIgnoreCase("V1  ")) {
				index *= 0x1A;
				randomAccessFile.seek((long) (0x12 + index + 0x12));
			} else if (version.equalsIgnoreCase("V3.0")) {
				index *= 0x28;
				randomAccessFile.seek((long) (0x14 + index + 0x1C));
			}
			int offset = startindex + Filereader.readInt(randomAccessFile);
			int length = Filereader.readInt(randomAccessFile);
			randomAccessFile.seek((long) offset);
			return Filereader.readString(randomAccessFile, length, charset);
		} catch (IOException e) {
			throw new StringResourceException("Error reading " + file.getName());
		}
	}

	public String getResource(int index) throws StringResourceException {
		try {
			if (randomAccessFile == null)
				open();
			if (index >= maxnr || index == 0xffffffff)
				return null;
			byte buffer[] = null;
			if (version.equalsIgnoreCase("V1  ")) {
				index *= 0x1a;
				randomAccessFile.seek((long) (0x12 + index + 0x02));
				buffer = new byte[8];
			} else if (version.equalsIgnoreCase("V3.0")) {
				index *= 0x28;
				randomAccessFile.seek((long) (0x14 + index + 0x04));
				buffer = new byte[16];
			}
			randomAccessFile.readFully(buffer);
			if (buffer[0] == 0)
				return null;
			int max = buffer.length;
			for (int i = 0; i < buffer.length; i++) {
				if (buffer[i] == 0x00) {
					max = i;
					break;
				}
			}
			if (max != buffer.length)
				buffer = Arrays.copyOfRange(buffer, 0, max);
			return new String(buffer);
		} catch (Exception e) {
			throw new StringResourceException("Error reading " + file.getName());
		}
	}

	private void open() throws StringResourceException {
		try {
			randomAccessFile = new RandomAccessFile(file, "r");
			randomAccessFile.seek((long) 0x00);
			String signature = Filereader.readString(randomAccessFile, 4);
			if (!signature.equalsIgnoreCase("TLK "))
				throw new IOException("Not valid TLK file");
			version = Filereader.readString(randomAccessFile, 4);
			if (version.equalsIgnoreCase("V1  "))
				randomAccessFile.seek((long) 0x0A);
			else if (version.equalsIgnoreCase("V3.0"))
				randomAccessFile.seek((long) 0x0C);
			maxnr = Filereader.readInt(randomAccessFile);
			startindex = Filereader.readInt(randomAccessFile);
		} catch (IOException e) {
			throw new StringResourceException(e);
		}
	}

}
