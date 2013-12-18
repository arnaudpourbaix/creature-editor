package com.pourbaix.infinity.resource.ids;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.pourbaix.infinity.entity.IdentifierEntry;
import com.pourbaix.infinity.entity.IdentifierFile;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.Constant;

public abstract class IdentifierFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(IdentifierFactory.class);

	private static final BiMap<String, String> IDENTIFIER_KEY_RESOURCES = ImmutableBiMap.of("1", "SPPR", "2", "SPWI", "3", "SPIN", "4", "SPCL");

	private static IdentifierFile spellIdentifier = null;

	public static IdentifierFile getIdentifierFile(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException("Entry doesn't exist: " + entryName);
		}
		return getIdentifierFile(entry);
	}

	public static IdentifierFile getIdentifierFile(ResourceEntry entry) throws FactoryException {
		try {
			IdentifierFile identifierFile = new IdentifierFile();
			String content = new String(entry.getResourceData());
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
				String trimLine = line.trim();
				String[] cols = trimLine.split("\\s+");
				if (cols.length < colCount) {
					continue;
				}
				IdentifierEntry identifierEntry = new IdentifierEntry(cols[0]);
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

	public static IdentifierEntry getSpellIdentifierByResource(String resource) throws FactoryException {
		if (spellIdentifier == null) {
			spellIdentifier = getIdentifierFile("spell.ids");
		}
		Pattern pattern = Pattern.compile("^(SPPR|SPWI|SPIN|SPCL)(\\d+)(.SPL)?$");
		Matcher matcher = pattern.matcher(resource);
		if (!matcher.find()) {
			return null;
		}
		String key = IDENTIFIER_KEY_RESOURCES.inverse().get(matcher.group(1)) + matcher.group(2);
		return spellIdentifier.getEntryByKey(key);
	}

	public static String getResourceNameByIdentifier(String identifier) throws FactoryException {
		if (spellIdentifier == null) {
			spellIdentifier = getIdentifierFile("spell.ids");
		}
		IdentifierEntry entry = spellIdentifier.getEntryByValue(identifier);
		if (entry == null) {
			return null;
		}
		return IDENTIFIER_KEY_RESOURCES.get(entry.getKey().substring(0, 1)) + entry.getKey().substring(1);
	}
}
