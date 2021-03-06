package com.pourbaix.infinity.resource.key;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.Filereader;

public class Keyfile {

	private static Keyfile instance;

	public static final Map<Integer, String> EXTENSIONS = ImmutableMap.<Integer, String> builder().put(0x3ed, "ITM").put(0x3ee, "SPL").put(0x3ef, "BCS")
			.put(0x3f0, "IDS").put(0x3f1, "CRE").put(0x3f3, "DLG").put(0x3f4, "2DA").put(0x3f6, "STO").put(0x3f8, "EFF").put(0x3fd, "PRO").build();

	private List<BiffEntry> biffEntries;
	private List<ResourceEntry> resourceEntries;
	private BiffArchive currentBIFF; // Caching of last BifFile - improves performance
	private BiffEntry currentBIFFEntry;
	private String signature;
	private String version;
	private File keyfile;

	public static Keyfile getInstance() {
		if (Keyfile.instance == null) {
			Keyfile.instance = new Keyfile();
		}
		return Keyfile.instance;
	}

	public void init(File keyfile) throws IOException {
		this.keyfile = keyfile;
		byte[] buffer = readChitinKey();
		readBiffEntries(buffer);
		readBiffResources(buffer);
	}

	public void addResourceEntry(ResourceEntry resourceEntry) {
		resourceEntries.add(resourceEntry);
	}

	public ResourceEntry getResourceEntry(final String resourcename) {
		Optional<ResourceEntry> entry = Iterables.tryFind(resourceEntries, new Predicate<ResourceEntry>() {
			public boolean apply(ResourceEntry entry) {
				return entry.getResourceName().equalsIgnoreCase(resourcename);
			}
		});
		return entry.isPresent() ? entry.get() : null;
	}

	public List<ResourceEntry> getResourceEntriesByExtension(final String extension) {
		Iterable<ResourceEntry> result = Iterables.filter(resourceEntries, new Predicate<ResourceEntry>() {
			public boolean apply(ResourceEntry entry) {
				return entry.getExtension().equalsIgnoreCase(extension);
			}
		});
		return Lists.newArrayList(result);
	}

	public boolean resourceExists(String resourcename) {
		return getResourceEntry(resourcename) != null;
	}

	public BiffEntry getBIFFEntry(int index) {
		return biffEntries.get(index);
	}

	public BiffArchive getBIFFFile(BiffEntry entry) throws IOException {
		if (currentBIFFEntry == entry)
			return currentBIFF; // Caching
		File file = entry.getFile();
		if (file == null)
			throw new IOException(entry + " not found");
		if (currentBIFF != null)
			currentBIFF.close();
		currentBIFFEntry = entry;
		currentBIFF = new BiffArchive(file);
		return currentBIFF;
	}

	private byte[] readChitinKey() throws IOException {
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(keyfile));
		byte[] buffer = Filereader.readBytes(is, (int) keyfile.length());
		is.close();
		signature = new String(buffer, 0, 4);
		version = new String(buffer, 4, 4);
		if (!signature.equals("KEY ") || !version.equals("V1  ")) {
			throw new IOException("Unsupported keyfile: " + keyfile);
		}
		return buffer;
	}

	private void readBiffEntries(byte[] buffer) {
		int numbif = DynamicArray.getInt(buffer, 8);
		int bifoff = DynamicArray.getInt(buffer, 16);
		biffEntries = new ArrayList<>(numbif);
		for (int i = 0; i < numbif; i++) {
			BiffEntry entry = new BiffEntry(i, buffer, bifoff + 12 * i);
			biffEntries.add(entry);
		}
	}

	private void readBiffResources(byte[] buffer) {
		int numres = DynamicArray.getInt(buffer, 12);
		int resoff = DynamicArray.getInt(buffer, 20);
		resourceEntries = new ArrayList<>();
		for (int i = 0; i < numres; i++) {
			ResourceEntry entry = new BiffResourceEntry(buffer, resoff + 14 * i, 8);
			if (entry.getExtension() != null) {
				resourceEntries.add(entry);
			}
		}
	}

}
