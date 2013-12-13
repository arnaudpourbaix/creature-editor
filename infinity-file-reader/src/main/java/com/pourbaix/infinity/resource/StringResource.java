package com.pourbaix.infinity.resource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.annotation.Resource;

import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.entity.GameVersionEnum;
import com.pourbaix.infinity.util.Filereader;

public class StringResource {

	@Resource
	private GlobalContext globalContext;

	private File ffile;
	private RandomAccessFile file;
	private String version;
	private int maxnr, startindex;
	private Charset cp1252Charset = Charset.forName("windows-1252");
	private Charset utf8Charset = Charset.forName("utf8");
	private Charset charset = cp1252Charset;
	private Charset usedCharset = charset;

	public void init(File ffile) {
		close();
		this.ffile = ffile;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(String cs) {
		charset = Charset.forName(cs);
		usedCharset = charset;
	}

	public void close() {
		if (file == null)
			return;
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		file = null;
	}

	public File getFile() {
		return ffile;
	}

	public int getMaxIndex() {
		return maxnr;
	}

	public String getResource(int index) throws Exception {
		try {
			if (file == null)
				open();
			if (index >= maxnr || index == 0xffffffff)
				return null;
			byte buffer[] = null;
			if (version.equalsIgnoreCase("V1  ")) {
				index *= 0x1a;
				file.seek((long) (0x12 + index + 0x02));
				buffer = new byte[8];
			} else if (version.equalsIgnoreCase("V3.0")) {
				index *= 0x28;
				file.seek((long) (0x14 + index + 0x04));
				buffer = new byte[16];
			}
			file.readFully(buffer);
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
			e.printStackTrace();
			throw new Exception("Error reading " + ffile.getName());
		}
	}

	public String getStringRef(int index) throws Exception {
		try {
			if (file == null)
				open();
			if (index >= maxnr || index < 0)
				return "No such index";
			// if (index == 0xffffffff) return "none";
			if (version.equalsIgnoreCase("V1  ")) {
				index *= 0x1A;
				file.seek((long) (0x12 + index + 0x12));
			} else if (version.equalsIgnoreCase("V3.0")) {
				index *= 0x28;
				file.seek((long) (0x14 + index + 0x1C));
			}
			int offset = startindex + Filereader.readInt(file);
			int length = Filereader.readInt(file);
			file.seek((long) offset);
			return Filereader.readString(file, length, usedCharset);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Error reading " + ffile.getName());
		}
	}

	private void open() throws IOException {
		file = new RandomAccessFile(ffile, "r");
		file.seek((long) 0x00);
		String signature = Filereader.readString(file, 4);
		if (!signature.equalsIgnoreCase("TLK "))
			throw new IOException("Not valid TLK file");
		version = Filereader.readString(file, 4);
		if (version.equalsIgnoreCase("V1  "))
			file.seek((long) 0x0A);
		else if (version.equalsIgnoreCase("V3.0"))
			file.seek((long) 0x0C);
		maxnr = Filereader.readInt(file);
		startindex = Filereader.readInt(file);
		if (globalContext.getGameVersion() == GameVersionEnum.BG1EE || globalContext.getGameVersion() == GameVersionEnum.BG2EE) {
			usedCharset = utf8Charset;
		}
	}

}
