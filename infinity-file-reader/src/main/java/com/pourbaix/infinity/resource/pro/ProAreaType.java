// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2005 Jon Olav Hauglid
// See LICENSE.txt for license information

package com.pourbaix.infinity.resource.pro;

import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.datatype.ColorValue;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.Flag;
import com.pourbaix.infinity.resource.datatype.HashBitmap;
import com.pourbaix.infinity.resource.datatype.IdsBitmap;
import com.pourbaix.infinity.resource.datatype.ProRef;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.Unknown;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public final class ProAreaType extends AbstractStruct {
	private static final LongIntegerHashMap<String> s_proj = new LongIntegerHashMap<String>();
	private static final String[] s_areaflags = { "Trap not visible", "Trap visible", "Triggered by inanimates", "Triggered by condition", "Delayed trigger",
			"Secondary projectile", "Fragments", "Not affecting allies", "Not affecting enemies", "Mage-level duration", "Cleric-level duration",
			"Draw animation", "Cone-shaped", "Ignore visibility", "Delayed explosion", "Skip first condition", "Single target" };

	static {
		s_proj.put(0L, "Fireball");
		s_proj.put(1L, "Stinking cloud");
		s_proj.put(2L, "Cloudkill");
		s_proj.put(3L, "Ice storm");
		s_proj.put(4L, "Grease");
		s_proj.put(5L, "Web");
		s_proj.put(6L, "Meteor");
		s_proj.put(7L, "Horrid wilting");
		s_proj.put(8L, "Teleport field");
		s_proj.put(9L, "Entangle");
		s_proj.put(10L, "Color spray");
		s_proj.put(11L, "Cone of cold");
		s_proj.put(12L, "Holy smite");
		s_proj.put(13L, "Unholy blight");
		s_proj.put(14L, "Prismatic spray");
		s_proj.put(15L, "Red dragon blast");
		s_proj.put(16L, "Storm of vengeance");
		s_proj.put(17L, "Purple fireball");
		s_proj.put(18L, "Green dragon blast");
		s_proj.put(255L, "None");
	}

	public ProAreaType() throws Exception {
		super(null, "Area effect info", new byte[256], 0);
		setOffset(512);
	}

	public ProAreaType(AbstractStruct superStruct, byte[] buffer, int offset) throws Exception {
		super(superStruct, "Area effect info", buffer, offset);
	}

	@Override
	protected int read(byte[] buffer, int offset) throws Exception {
		list.add(new Flag(buffer, offset, 4, "Area flags", s_areaflags));
		list.add(new DecNumber(buffer, offset + 4, 2, "Trap size"));
		list.add(new DecNumber(buffer, offset + 6, 2, "Explosion size"));
		list.add(new ResourceRef(buffer, offset + 8, "Explosion sound", "WAV"));
		list.add(new DecNumber(buffer, offset + 16, 2, "Explosion frequency (frames)"));
		list.add(new IdsBitmap(buffer, offset + 18, 2, "Fragment animation", "ANIMATE.IDS"));
		list.add(new ProRef(buffer, offset + 20, "Secondary projectile"));
		list.add(new DecNumber(buffer, offset + 22, 1, "# repetitions"));
		list.add(new HashBitmap(buffer, offset + 23, 1, "Explosion effect", s_proj));
		list.add(new ColorValue(buffer, offset + 24, 1, "Explosion color"));
		list.add(new Unknown(buffer, offset + 25, 1, "Unused"));
		list.add(new ProRef(buffer, offset + 26, "Explosion projectile"));
		list.add(new ResourceRef(buffer, offset + 28, "Explosion animation", new String[] { "VVC", "BAM" }));
		list.add(new DecNumber(buffer, offset + 36, 2, "Cone width"));
		list.add(new Unknown(buffer, offset + 38, 218));

		return offset + 256;
	}
}
