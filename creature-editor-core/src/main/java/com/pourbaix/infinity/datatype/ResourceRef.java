package com.pourbaix.infinity.datatype;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;

public class ResourceRef extends Datatype {
	private static final String NONE = "None";
	private String curtype;
	String resname;
	private byte buffer[];

	public ResourceRef(byte h_buffer[], int offset, String name, String type) {
		this(h_buffer, offset, 8, name, type);
	}

	public ResourceRef(byte h_buffer[], int offset, int length, String name, String type) {
		this(h_buffer, offset, length, name, new String[] { type });
	}

	public ResourceRef(byte h_buffer[], int offset, String name, String[] type) {
		this(h_buffer, offset, 8, name, type);
	}

	public ResourceRef(byte h_buffer[], int offset, int length, String name, String[] type) {
		super(offset, length, name);
		curtype = type[0];
		buffer = Arrays.copyOfRange(h_buffer, offset, offset + length);
		if (buffer[0] == 0x00 || buffer[0] == 0x4e && buffer[1] == 0x6f && buffer[2] == 0x6e && buffer[3] == 0x65 && buffer[4] == 0x00) {
			resname = NONE;
		} else {
			int max = buffer.length;
			for (int i = 0; i < buffer.length; i++) {
				if (buffer[i] == 0x00) {
					max = i;
					break;
				}
			}
			if (max != buffer.length)
				buffer = Arrays.copyOfRange(buffer, 0, max);
			resname = new String(buffer).toUpperCase();
		}
		if (resname.equalsIgnoreCase(NONE))
			resname = NONE;

		// determine the correct file extension
		if (!resname.equals(NONE)) {
			for (int i = 0; i < type.length; i++) {
				if (Keyfile.getInstance().getResourceEntry(resname + "." + type[i]) != null) {
					curtype = type[i];
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		if (resname.equals(NONE))
			return resname;
		String searchName = getSearchName();
		if (searchName != null)
			return new StringBuffer(getResourceName()).append(" (").append(searchName).append(')').toString();
		return getResourceName();
	}

	public String getResourceName() {
		return resname.equals(NONE) ? null : new StringBuffer(resname).append('.').append(curtype).toString();
	}

	public String getSearchName() {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(getResourceName());
		if (entry != null)
			return entry.getSearchString();
		return null;
	}

	public String getType() {
		return curtype;
	}

	public boolean isLegalEntry(ResourceEntry entry) {
		return entry.toString().lastIndexOf('.') != 0;
	}

	void addExtraEntries(List<Object> entries) {
	}

	// -------------------------- INNER CLASSES --------------------------

	static final class ResourceRefEntry {
		private final String name;

		ResourceRefEntry(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	final class IgnoreCaseExtComparator<T> implements Comparator<T> {
		@Override
		public int compare(T o1, T o2) {
			if (o1 != null && o2 != null) {
				String s1 = o1.toString();
				String s2 = o2.toString();
				int i1 = s1.lastIndexOf('.') > 0 ? s1.lastIndexOf('.') : s1.length();
				int i2 = s2.lastIndexOf('.') > 0 ? s2.lastIndexOf('.') : s2.length();
				return s1.substring(0, i1).compareToIgnoreCase(s2.substring(0, i2));
			} else
				return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return obj.equals(this);
		}
	}
}
