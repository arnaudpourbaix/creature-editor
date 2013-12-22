package com.pourbaix.infinity.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.pourbaix.infinity.datatype.IdsEnum;
import com.pourbaix.infinity.entity.IdentifierEntry;
import com.pourbaix.infinity.entity.IdentifierFile;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.Constant;

@Service
public class IdentifierFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(IdentifierFactory.class);

	private final Map<String, IdentifierFile> cache = new HashMap<>();

	private static final BiMap<String, String> IDENTIFIER_KEY_RESOURCES = ImmutableBiMap.of("1", "SPPR", "2", "SPWI", "3", "SPIN", "4", "SPCL");

	public String getFirstValueByKey(String ids, Long key) throws FactoryException {
		IdentifierFile file = getIdentifierFile(ids);
		if (file == null) {
			throw new FactoryException(ids + " is missing!");
		}
		IdentifierEntry entry = file.getEntryByKey(key);
		if (entry == null) {
			return null;
		}
		return entry.getFirstValue();
	}

	public IdentifierEntry getSpellIdentifierByResource(String resource) throws FactoryException {
		IdentifierFile spellIdentifier = getIdentifierFile(IdsEnum.Spell);
		Pattern pattern = Pattern.compile("^(SPPR|SPWI|SPIN|SPCL)(\\d+)(.SPL)?$");
		Matcher matcher = pattern.matcher(resource.toUpperCase());
		if (!matcher.find()) {
			return null;
		}
		Long key = Long.valueOf(IDENTIFIER_KEY_RESOURCES.inverse().get(matcher.group(1)) + matcher.group(2));
		return spellIdentifier.getEntryByKey(key);
	}

	public String getResourceNameByIdentifier(String identifier) throws FactoryException {
		IdentifierFile spellIdentifier = getIdentifierFile(IdsEnum.Spell);
		IdentifierEntry entry = spellIdentifier.getEntryByValue(identifier);
		if (entry == null) {
			return null;
		}
		String key = entry.getKey().toString();
		return IDENTIFIER_KEY_RESOURCES.get(key.substring(0, 1)) + key.substring(1);
	}

	public IdentifierFile getIdentifierFile(IdsEnum name) throws FactoryException {
		return getIdentifierFile(name.getResource());
	}

	public IdentifierFile getIdentifierFile(ResourceEntry entry) throws FactoryException {
		return getIdentifierFile(entry.getResourceName());
	}

	public IdentifierFile getIdentifierFile(String name) throws FactoryException {
		String uName = name.toUpperCase();
		IdentifierFile file = cache.get(uName);
		if (file == null) {
			file = get(uName);
			cache.put(uName, file);
		}
		return file;
	}

	private IdentifierFile get(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException("Entry doesn't exist: " + entryName);
		}
		return get(entry);
	}

	private IdentifierFile get(ResourceEntry entry) throws FactoryException {
		String content = "";
		try {
			IdentifierFile identifierFile = new IdentifierFile();
			content = entry.getResourceTextData();
			// determine number of columns
			Multiset<Integer> columns = HashMultiset.create();
			for (String line : content.split(Constant.CARRIAGE_RETURN)) {
				String trimLine = line.trim();
				int colCount = trimLine.split("\\s+").length;
				columns.add(colCount);
			}
			int colCount = Multisets.copyHighestCountFirst(columns).iterator().next();
			// add entries
			for (String line : content.split(Constant.CARRIAGE_RETURN)) {
				if (line.contains("IDS V1.0")) {
					continue;
				}
				String[] cols = line.trim().split("[\\s\\t]+");
				if (cols.length < colCount) {
					continue;
				}
				long key = cols[0].startsWith("0x") ? Long.parseLong(cols[0].substring(2), 16) : Long.parseLong(cols[0]);
				IdentifierEntry identifierEntry = new IdentifierEntry(key);
				for (int i = 1; i < cols.length; i++) {
					identifierEntry.addValue(cols[i]);
				}
				identifierFile.addEntry(identifierEntry);
			}
			return identifierFile;
		} catch (IOException e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}

}
