package com.pourbaix.infinity.resource.datatype;

import com.pourbaix.infinity.resource.StructEntry;

public final class SectionCount extends DecNumber {
	private final Class<? extends StructEntry> section;

	public SectionCount(byte buffer[], int offset, int length, String desc, Class<? extends StructEntry> section) {
		super(buffer, offset, length, desc);
		this.section = section;
	}

	public Class<? extends StructEntry> getSection() {
		return section;
	}
}
