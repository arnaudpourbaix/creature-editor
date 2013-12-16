// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2005 Jon Olav Hauglid
// See LICENSE.txt for license information

package com.pourbaix.infinity.resource.dlg;

import java.util.List;

import com.pourbaix.infinity.resource.StructEntry;
import com.pourbaix.infinity.resource.datatype.Datatype;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.TextString;

public abstract class AbstractCode extends Datatype {
	private final DecNumber len;
	private final DecNumber off;
	private String text;

	AbstractCode(String name) {
		this(new byte[8], 0, name);
		text = "";
	}

	AbstractCode(byte buffer[], int offset, String nane) {
		super(offset, 8, nane);
		off = new DecNumber(buffer, offset, 4, "Offset");
		len = new DecNumber(buffer, offset + 4, 4, "Length");
		text = new String(buffer, off.getValue(), len.getValue());
	}

	@Override
	public String toString() {
		return text;
	}

	public void addFlatList(List<StructEntry> flatList) {
		flatList.add(off);
		flatList.add(len);
		try {
			TextString ts = new TextString(text.getBytes(), 0, len.getValue(), "Text");
			ts.setOffset(off.getValue());
			flatList.add(ts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getTextLength() {
		return len.getValue();
	}

	public int updateOffset(int offs) {
		off.setValue(offs);
		len.setValue(text.length());
		return len.getValue();
	}

}
