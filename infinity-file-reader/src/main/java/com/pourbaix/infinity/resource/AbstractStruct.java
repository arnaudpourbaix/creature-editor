package com.pourbaix.infinity.resource;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pourbaix.infinity.resource.dlg.AbstractCode;
import com.pourbaix.infinity.resource.key.ResourceEntry;

public abstract class AbstractStruct implements StructEntry, Closeable {
	protected List<StructEntry> list;
	private AbstractStruct superStruct;
	private ResourceEntry entry;
	private String name;
	private int startoffset, endoffset, extraoffset;

	protected AbstractStruct() {
	}

	protected AbstractStruct(ResourceEntry entry) throws Exception {
		this.entry = entry;
		list = new ArrayList<StructEntry>();
		name = entry.toString();
		byte buffer[] = entry.getResourceData();
		endoffset = read(buffer, 0);
	}

	protected AbstractStruct(AbstractStruct superStruct, String name, int startoffset, int listSize) {
		this.superStruct = superStruct;
		this.name = name;
		this.startoffset = startoffset;
		list = new ArrayList<StructEntry>(listSize);
	}

	protected AbstractStruct(AbstractStruct superStruct, String name, byte buffer[], int startoffset) throws Exception {
		this(superStruct, name, buffer, startoffset, 10);
	}

	protected AbstractStruct(AbstractStruct superStruct, String name, byte buffer[], int startoffset, int listSize) throws Exception {
		this(superStruct, name, startoffset, listSize);
		endoffset = read(buffer, startoffset);
	}

	public void realignStructOffsets() {
		int offset = startoffset;
		for (int i = 0; i < list.size(); i++) {
			StructEntry structEntry = list.get(i);
			structEntry.setOffset(offset);
			offset += structEntry.getSize();
			if (structEntry instanceof AbstractStruct)
				((AbstractStruct) structEntry).realignStructOffsets();
		}
	}

	protected abstract int read(byte buffer[], int startoffset) throws Exception;

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

	protected void setExtraOffset(int offset) {
		extraoffset = offset;
	}

	protected void setStartOffset(int offset) {
		startoffset = offset;
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

	public StructEntry getAttribute(int offset) {
		List<StructEntry> flatList = getFlatList();
		for (int i = 0; i < flatList.size(); i++) {
			StructEntry structEntry = flatList.get(i);
			if (offset >= structEntry.getOffset() && offset < structEntry.getOffset() + structEntry.getSize())
				return structEntry;
		}
		return null;
	}

	// returns a specific StructEntry object located at the specified offset
	public StructEntry getAttribute(int offset, Class<? extends StructEntry> type) {
		for (int i = 0; i < list.size(); i++) {
			StructEntry structEntry = list.get(i);
			if (offset >= structEntry.getOffset()) {
				if (offset == structEntry.getOffset() && type.isInstance(structEntry)) {
					return structEntry;
				} else if (structEntry instanceof AbstractStruct) {
					StructEntry res = ((AbstractStruct) structEntry).getAttribute(offset, type);
					if (res != null)
						return res;
				}
			}
		}
		return null;
	}

	public StructEntry getAttribute(String ename) {
		for (int i = 0; i < list.size(); i++) {
			StructEntry structEntry = list.get(i);
			if (structEntry.getName().equalsIgnoreCase(ename))
				return structEntry;
		}
		return null;
	}

	@Override
	public void write(OutputStream os) throws IOException {
		Collections.sort(list); // This way we can writeField out in the order in list - sorted by offset
		for (int i = 0; i < list.size(); i++)
			list.get(i).write(os);
	}

	protected void writeFlatList(OutputStream os) throws IOException {
		List<StructEntry> flatList = getFlatList();
		for (int i = 0; i < flatList.size(); i++)
			flatList.get(i).write(os);
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

}
