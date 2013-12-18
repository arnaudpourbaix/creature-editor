package com.pourbaix.infinity.resource.ids;

import java.io.IOException;

import com.pourbaix.infinity.entity.IdentifierEntry;
import com.pourbaix.infinity.entity.IdentifierFile;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.Constant;

public abstract class IdentifierFactory {

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
			for (String line : content.split(Constant.CARRIAGE_RETURN)) {
				String trimLine = line.trim();
				if (trimLine.isEmpty()) {
					continue;
				}
				String[] cols = trimLine.split("\\s+");
				IdentifierEntry identifierEntry = new IdentifierEntry(cols[0]);
				for (int i = 1; i < cols.length; i++) {
					identifierEntry.addValue(cols[i]);
				}
				identifierFile.getEntries().add(identifierEntry);
			}
			return identifierFile;
		} catch (IOException e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}

	public static IdentifierEntry getSpellIdentifier(String resource) throws FactoryException {
		if (spellIdentifier == null) {
			spellIdentifier = getIdentifierFile("spell.ids");
		}
		// 1 SPPR
		// 2 SPWI
		// 3 SPIN
		// 4 SPCL
		return spellIdentifier.getEntries().iterator().next();
	}

}
