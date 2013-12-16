package com.pourbaix.infinity.resource;

import java.io.IOException;
import java.io.OutputStream;

import com.pourbaix.infinity.resource.datatype.Datatype;
import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.Filewriter;
import com.pourbaix.infinity.util.IdsMapCache;
import com.pourbaix.infinity.util.IdsMapEntry;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public final class IDSTargetEffect extends Datatype {
	private final String sIDS[] = new String[] { "", "", "EA.IDS", "GENERAL.IDS", "RACE.IDS", "CLASS.IDS", "SPECIFIC.IDS", "GENDER.IDS", "ALIGN.IDS" };
	private LongIntegerHashMap<IdsMapEntry> idsMap;
	private long idsValue, idsFile;

	public IDSTargetEffect(byte buffer[], int offset) {
		this(buffer, offset, "EA.IDS");
	}

	public IDSTargetEffect(byte buffer[], int offset, String secondIDS) {
		super(offset, 8, "IDS target");
		idsValue = DynamicArray.getUnsignedInt(buffer, offset);
		idsFile = DynamicArray.getUnsignedInt(buffer, offset + 4);
		sIDS[2] = secondIDS;
		if (idsFile < sIDS.length && !sIDS[(int) idsFile].equals(""))
			idsMap = IdsMapCache.get(sIDS[(int) idsFile]).getMap();
		else
			idsMap = new LongIntegerHashMap<IdsMapEntry>();
	}

	@Override
	public String toString() {
		String idsFileStr = getString((int) idsFile) + " / ";
		Object o = idsMap.get(idsValue);
		if (o == null)
			return idsFileStr + "Unknown value - " + idsValue;
		else if (o instanceof IdsMapEntry)
			return idsFileStr + idsMap.get(idsValue).toString();
		else
			return idsFileStr + idsMap.get(idsValue).toString() + " - " + idsValue;
	}

	public long getValue() {
		return idsValue;
	}

	@Override
	public void write(OutputStream os) throws IOException {
		Filewriter.writeInt(os, (int) idsValue);
		Filewriter.writeInt(os, (int) idsFile);
	}

	private String getString(int nr) {
		if (nr >= sIDS.length)
			return "Unknown - " + nr;
		if (nr < 0)
			return "Error - " + nr;
		if (sIDS[nr].equals(""))
			return "Unknown - " + nr;
		return new StringBuffer(sIDS[nr]).append(" - ").append(nr).toString();
	}
}
