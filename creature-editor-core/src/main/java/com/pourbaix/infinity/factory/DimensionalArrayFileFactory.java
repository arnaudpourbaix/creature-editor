package com.pourbaix.infinity.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
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
		Iterator<String> rowIterator = rows.iterator();
		rowIterator.next(); // first row contains the file signature
		rowIterator.next(); // second row contains the "default value" for the file

		DimensionalArrayFile dimensionalArrayFile = new DimensionalArrayFile();

		parseHeaders(dimensionalArrayFile, rowIterator.next()); // third row contains column headings
		parseRows(dimensionalArrayFile, rowIterator);

		return dimensionalArrayFile;
	}

	private void parseHeaders(DimensionalArrayFile dimensionalArrayFile, String header) {
		List<String> headers = Arrays.asList(header.split("[\\t\\s]+"));
		dimensionalArrayFile.setHeaders(Lists.newArrayList(headers));
	}

	private void parseRows(DimensionalArrayFile dimensionalArrayFile, Iterator<String> rowIterator) {
		while (rowIterator.hasNext()) {
			String row = rowIterator.next();
			List<String> columns = Arrays.asList(row.split("[\\t\\s]+"));
			dimensionalArrayFile.addRow(Lists.newArrayList(columns));
		}
	}

}
