package com.pourbaix.infinity.resource.datatype;

import com.pourbaix.infinity.resource.StructEntry;

public final class SectionOffset extends HexNumber {
	private final Class<? extends StructEntry> section;

	public SectionOffset(byte buffer[], int offset, String desc, Class<? extends StructEntry> section) {
		super(buffer, offset, 4, desc);
		this.section = section;
	}

	public Class<? extends StructEntry> getSection() {
		return section;
	}
}
