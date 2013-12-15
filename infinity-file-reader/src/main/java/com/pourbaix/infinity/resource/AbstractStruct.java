package com.pourbaix.infinity.resource;

import java.awt.Component;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pourbaix.infinity.resource.datatype.SectionCount;
import com.pourbaix.infinity.resource.datatype.SectionOffset;
import com.pourbaix.infinity.resource.datatype.Unknown;
import com.pourbaix.infinity.resource.dlg.AbstractCode;
import com.pourbaix.infinity.resource.key.ResourceEntry;

public abstract class AbstractStruct implements StructEntry, Closeable {
	protected List<StructEntry> list;
	private AbstractStruct superStruct;
	private Map<Class<? extends StructEntry>, SectionCount> countmap;
	private Map<Class<? extends StructEntry>, SectionOffset> offsetmap;
	private ResourceEntry entry;
	private String name;
	private int startoffset, endoffset, extraoffset;
	private Collection<Component> viewerComponents = null;

	protected AbstractStruct() {
	}

	protected AbstractStruct(ResourceEntry entry) throws Exception {
		this.entry = entry;
		list = new ArrayList<StructEntry>();
		name = entry.toString();
		byte buffer[] = entry.getResourceData();
		endoffset = read(buffer, 0);
		if (this instanceof HasAddRemovable && list.size() > 0) {// Is this enough?
			Collections.sort(list); // This way we can writeField out in the order in list - sorted by offset
			fixHoles(buffer);
			initAddStructMaps();
		}
	}

	protected AbstractStruct(AbstractStruct superStruct, String name, int startoffset, int listSize) {
		this.superStruct = superStruct;
		this.name = name;
		this.startoffset = startoffset;
		list = new ArrayList<StructEntry>(listSize);
	}

	//	protected AbstractStruct(AbstractStruct superStruct, String name, byte buffer[], int startoffset) throws Exception {
	//		this(superStruct, name, buffer, startoffset, 10);
	//	}

	//	protected AbstractStruct(AbstractStruct superStruct, String name, byte buffer[], int startoffset, int listSize) throws Exception {
	//		this(superStruct, name, startoffset, listSize);
	//		endoffset = read(buffer, startoffset);
	//		if (this instanceof HasAddRemovable) {
	//			if (!(this instanceof Actor)) // Is this enough?
	//				Collections.sort(list); // This way we can writeField out in the order in list - sorted by offset
	//			initAddStructMaps();
	//		}
	//	}

	protected abstract int read(byte buffer[], int startoffset) throws Exception;

	private void fixHoles(byte buffer[]) {
		int offset = startoffset;
		List<StructEntry> flatList = getFlatList();
		for (int i = 0; i < flatList.size(); i++) {
			StructEntry se = flatList.get(i);
			int delta = se.getOffset() - offset;
			if (delta > 0) {
				Unknown hole = new Unknown(buffer, offset, delta, "Unused bytes?");
				list.add(hole);
				flatList.add(i, hole);
				System.out.println("Hole: " + name + " off: " + Integer.toHexString(offset) + "h len: " + delta);
				i++;
			}
			offset = se.getOffset() + se.getSize();
		}
		if (endoffset < buffer.length) { // Does this break anything?
			list.add(new Unknown(buffer, endoffset, buffer.length - endoffset, "Unused bytes?"));
			System.out.println("Hole: " + name + " off: " + Integer.toHexString(offset) + "h len: " + (buffer.length - endoffset));
			endoffset = buffer.length;
		}
	}

	public int getEndOffset() {
		return endoffset;
	}

	public int getExtraOffset() {
		return extraoffset;
	}

	public List<StructEntry> getList() {
		return list;
	}

	public List<StructEntry> getFlatList() {
		List<StructEntry> flatList = new ArrayList<StructEntry>(2 * list.size());
		addFlatList(flatList);
		Collections.sort(flatList);
		return flatList;
	}

	public int getIndexOf(StructEntry structEntry) {
		return list.indexOf(structEntry);
	}

	public ResourceEntry getResourceEntry() {
		return entry;
	}

	public StructEntry getStructEntryAt(int index) {
		return list.get(index);
	}

	public AbstractStruct getSuperStruct() {
		return superStruct;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getOffset() {
		return startoffset;
	}

	@Override
	public int getSize() {
		return endoffset - startoffset;
	}

	@Override
	public void setOffset(int newoffset) {
		if (extraoffset != 0)
			extraoffset += newoffset - startoffset;
		int delta = getSize();
		startoffset = newoffset;
		endoffset = newoffset + delta;
	}

	@Override
	public void copyNameAndOffset(StructEntry structEntry) {
		name = structEntry.getName();
		setOffset(structEntry.getOffset());
	}

	@Override
	public void close() {
	}

	@Override
	public int compareTo(StructEntry o) {
		return getOffset() - o.getOffset();
	}

	private void addFlatList(List<StructEntry> flatList) {
		for (int i = 0; i < list.size(); i++) {
			StructEntry o = list.get(i);
			if (o instanceof AbstractStruct)
				((AbstractStruct) o).addFlatList(flatList);
			else if (o instanceof AbstractCode)
				((AbstractCode) o).addFlatList(flatList);
			else
				flatList.add(o);
		}
	}

	private void initAddStructMaps() {
		countmap = new HashMap<Class<? extends StructEntry>, SectionCount>();
		offsetmap = new HashMap<Class<? extends StructEntry>, SectionOffset>();
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o instanceof SectionOffset) {
				SectionOffset so = (SectionOffset) o;
				if (so.getSection() != null) {
					offsetmap.put(so.getSection(), so);
				}
			} else if (o instanceof SectionCount)
				countmap.put(((SectionCount) o).getSection(), (SectionCount) o);
		}
	}

}
