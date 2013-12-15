package com.pourbaix.infinity.resource.key;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.util.DynamicArray;
import com.pourbaix.infinity.util.FileReader;

public class Keyfile {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(Keyfile.class);

	public static final Map<Integer, String> EXTENSIONS = ImmutableMap.<Integer, String> builder().put(0x3ed, "ITM").put(0x3ee, "SPL").put(0x3ef, "BCS")
			.put(0x3f0, "IDS").put(0x3f1, "CRE").put(0x3f3, "DLG").put(0x3f4, "2DA").put(0x3f6, "STO").put(0x3f8, "EFF").put(0x3fd, "PRO").build();

	private List<BiffEntry> biffEntries;
	private List<ResourceEntry> resourceEntries;
	private String signature;
	private String version;

	@Autowired
	private GlobalContext globalContext;

	public void init() throws IOException {
		byte[] buffer;
		buffer = readChitinKey();
		readBiffEntries(buffer);
		readBiffResources(buffer);
	}

	public void addResourceEntry(ResourceEntry resourceEntry) {
		resourceEntries.add(resourceEntry);
	}

	public ResourceEntry getResourceEntry(final String resourcename) {
		Optional<ResourceEntry> entry = Iterables.tryFind(resourceEntries, new Predicate<ResourceEntry>() {
			public boolean apply(ResourceEntry entry) {
				return entry.getResourceName().equals(resourcename);
			}
		});
		return entry.isPresent() ? entry.get() : null;
	}

	private byte[] readChitinKey() throws IOException {
		File keyfile = globalContext.getChitinKey();
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(keyfile));
		byte[] buffer = FileReader.readBytes(is, (int) keyfile.length());
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
