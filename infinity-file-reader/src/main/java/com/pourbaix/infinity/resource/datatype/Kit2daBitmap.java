package com.pourbaix.infinity.resource.datatype;

import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;

import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.other.PlainTextResource;
import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.Filewriter;
import com.pourbaix.infinity.util.LongIntegerHashMap;

public final class Kit2daBitmap extends Datatype {
	private static final LongIntegerHashMap<KitlistEntry> kitsNumber = new LongIntegerHashMap<KitlistEntry>();
	private static final LongIntegerHashMap<KitlistEntry> kitsUnusable = new LongIntegerHashMap<KitlistEntry>();
	private boolean useUnusable = true;
	private long value;

	private static void parseKitlist() {
		try {
			PlainTextResource kitlist = new PlainTextResource(Keyfile.getInstance().getResourceEntry("KITLIST.2DA"));
			StringTokenizer st = new StringTokenizer(kitlist.getText(), "\n");
			if (st.hasMoreTokens())
				st.nextToken();
			if (st.hasMoreTokens())
				st.nextToken();
			if (st.hasMoreTokens())
				st.nextToken();
			if (st.hasMoreTokens())
				st.nextToken();
			while (st.hasMoreTokens()) {
				parseKitlistLine(st.nextToken());
			}
			kitlist.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		kitsNumber.put((long) 0, new KitlistEntry((long) 0, "NO_KIT"));
		kitsUnusable.put((long) 0, new KitlistEntry((long) 0, "NO_KIT"));
	}

	private static void parseKitlistLine(String s) {
		StringTokenizer st = new StringTokenizer(s);
		int number = Integer.parseInt(st.nextToken());
		String name = st.nextToken();
		st.nextToken();
		st.nextToken();
		st.nextToken();
		st.nextToken();
		st.nextToken();
		String unusableSt = st.nextToken();
		long unusable;
		if (unusableSt.substring(0, 2).equalsIgnoreCase("0x"))
			unusable = Long.parseLong(unusableSt.substring(2), 16);
		else
			unusable = Long.parseLong(unusableSt);
		kitsNumber.put((long) number, new KitlistEntry((long) number, name));
		kitsUnusable.put(unusable, new KitlistEntry(unusable, name));
	}

	public static void resetKitlist() {
		kitsNumber.clear();
		kitsUnusable.clear();
	}

	public Kit2daBitmap(byte buffer[], int offset) {
		super(offset, 4, "Kit");
		if (kitsNumber.size() == 0)
			parseKitlist();
		if (buffer[offset + 3] == 0x40) {
			useUnusable = false;
			value = (long) buffer[offset + 2];
		} else {
			value = (long) (DynamicArray.getUnsignedShort(buffer, offset + 2) + 0x10000 * DynamicArray.getUnsignedShort(buffer, offset));
			if (value < 0)
				value += 4294967296L;
		}
	}

	@Override
	public void write(OutputStream os) throws IOException {
		if (useUnusable) {
			if (value > 2147483648L)
				value -= 4294967296L;
			byte buffer[] = DynamicArray.convertInt((int) value);
			os.write((int) buffer[2]);
			os.write((int) buffer[3]);
			os.write((int) buffer[0]);
			os.write((int) buffer[1]);
		} else
			Filewriter.writeBytes(os, new byte[] { 0x00, 0x00, (byte) value, 0x40 });
	}

	@Override
	public String toString() {
		Object o;
		if (useUnusable)
			o = kitsUnusable.get(value);
		else
			o = kitsNumber.get(value);
		if (o == null)
			return "Unknown - " + value;
		else
			return o.toString();
	}

	// -------------------------- INNER CLASSES --------------------------

	private static final class KitlistEntry {
		private final long number;
		private final String name;

		private KitlistEntry(long number, String name) {
			this.number = number;
			this.name = name;
		}

		@Override
		public String toString() {
			return name + " - " + number;
		}
	}
}
