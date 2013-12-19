// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2005 Jon Olav Hauglid
// See LICENSE.txt for license information

package com.pourbaix.infinity.resource.pro;

import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.Resource;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.Flag;
import com.pourbaix.infinity.resource.datatype.HashBitmapEx;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.TextString;
import com.pourbaix.infinity.resource.datatype.Unknown;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public final class ProResource extends AbstractStruct implements Resource {
	private static final String[] s_color = { "", "Black", "Blue", "Chromatic", "Gold", "Green", "Purple", "Red", "White", "Ice", "Stone", "Magenta", "Orange" };
	private static final String[] s_behave = { "No flags set", "Show sparks", "Use height", "Loop fire sound", "Loop impact sound", "Ignore center",
			"Draw as background" };
	private static final LongIntegerHashMap<String> m_projtype = new LongIntegerHashMap<String>();
	static {
		m_projtype.put(1L, "No BAM");
		m_projtype.put(2L, "Single target");
		m_projtype.put(3L, "Area of effect");
	}

	public ProResource(ResourceEntry entry) throws Exception {
		super(entry);
	}

	@Override
	protected int read(byte[] buffer, int offset) throws Exception {
		list.add(new TextString(buffer, offset, 4, "Signature"));
		list.add(new TextString(buffer, offset + 4, 4, "Version"));
		HashBitmapEx projtype = new HashBitmapEx(buffer, offset + 8, 2, "Projectile type", m_projtype);
		list.add(projtype);
		list.add(new DecNumber(buffer, offset + 10, 2, "Speed"));
		list.add(new Flag(buffer, offset + 12, 4, "Behavior", s_behave));
		list.add(new ResourceRef(buffer, offset + 16, "Fire sound", "WAV"));
		list.add(new ResourceRef(buffer, offset + 24, "Impact sound", "WAV"));
		list.add(new ResourceRef(buffer, offset + 32, "Source animation", new String[] { "VVC", "BAM" }));
		list.add(new Bitmap(buffer, offset + 40, 4, "Particle color", s_color));
		list.add(new Unknown(buffer, offset + 44, 212));
		offset += 256;

		if (projtype.getValue() > 1L) {
			ProSingleType single = new ProSingleType(this, buffer, offset);
			list.add(single);
			offset += single.getSize();
		}
		if (projtype.getValue() > 2L) {
			ProAreaType area = new ProAreaType(this, buffer, offset);
			list.add(area);
			offset += area.getSize();
		}

		return offset;
	}
}
