package com.pourbaix.infinity.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pourbaix.infinity.datatype.DimensionalArrayEnum;
import com.pourbaix.infinity.domain.DimensionalArrayFile;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.Decryptor;

@Service
@Transactional
public class DimensionalArrayFileFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(DimensionalArrayFileFactory.class);

	private static final String MISSING_DIMENSIONAL_ARRAY_FILE = "MISSING_DIMENSIONAL_ARRAY_FILE";
	private static final String INVALID_DIMENSIONAL_ARRAY_FILE = "INVALID_DIMENSIONAL_ARRAY_FILE";

	private final Map<String, DimensionalArrayFile> cache = new HashMap<>();

	public DimensionalArrayFile getDimensionalArray(DimensionalArrayEnum name) throws FactoryException {
		return getDimensionalArrayFile(name.getResource());
	}

	public DimensionalArrayFile getDimensionalArrayFile(String name) throws FactoryException {
		String uName = name.toUpperCase();
		DimensionalArrayFile file = cache.get(uName);
		if (file == null) {
			file = get(uName);
			cache.put(uName, file);
		}
		return file;
	}

	private DimensionalArrayFile get(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException(MISSING_DIMENSIONAL_ARRAY_FILE, entryName);
		}
		return get(entry);
	}

	private DimensionalArrayFile get(ResourceEntry entry) throws FactoryException {
		String text;
		try {
			byte[] data = entry.getResourceData();
			if (data != null && data.length > 1 && data[0] == -1) {
				text = Decryptor.decrypt(data, 2, data.length);
			} else {
				text = new String(data);
			}
		} catch (IOException e) {
			throw new FactoryException(INVALID_DIMENSIONAL_ARRAY_FILE, entry.getResourceName());
		}
		List<String> rows = Arrays.asList(text.split("\n"));
		DimensionalArrayFile dimensionalArrayFile = new DimensionalArrayFile();
		return dimensionalArrayFile;
	}
}
