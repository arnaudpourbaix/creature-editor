package com.pourbaix.infinity.resource.datatype;

import com.pourbaix.infinity.util.LongIntegerHashMap;

/**
 * Adds UpdateListener callback support to the HashBitmap class
 * 
 * @author argent77
 * 
 */
public class HashBitmapEx extends HashBitmap {

	public HashBitmapEx(byte buffer[], int offset, int length, String name, LongIntegerHashMap<String> idsmap) {
		super(buffer, offset, length, name, idsmap);
	}

}
