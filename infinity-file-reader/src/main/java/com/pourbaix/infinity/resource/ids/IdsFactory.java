package com.pourbaix.infinity.resource.ids;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pourbaix.infinity.entity.Ids;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.Constant;

public abstract class IdsFactory {

	public static Ids getIds(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException("Entry doesn't exist: " + entryName);
		}
		return getIds(entry);
	}

	public static Ids getIds(ResourceEntry entry) throws FactoryException {
		try {
			Ids ids = new Ids();
			String content = new String(entry.getResourceData());
			for (String line : content.split(Constant.CARRIAGE_RETURN)) {
				String trimLine = line.trim();
				if (trimLine.isEmpty()) {
					continue;
				}
				String[] cols = trimLine.split("\\s+");
				List<String> values = new ArrayList<>();
				for (int i = 1; i < cols.length; i++) {
					values.add(cols[i]);
				}
				ids.getEntries().put(cols[0], values);
			}
			return ids;
		} catch (IOException e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}
}
