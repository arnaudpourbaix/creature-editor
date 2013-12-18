package com.pourbaix.infinity.cache;

import java.util.HashMap;
import java.util.Map;

import com.pourbaix.infinity.entity.IdentifierFile;
import com.pourbaix.infinity.entity.IdsEnum;
import com.pourbaix.infinity.factory.IdentifierFactory;
import com.pourbaix.infinity.resource.FactoryException;

public final class IdsCacheService {

	private static final Map<String, IdentifierFile> common = new HashMap<>();

	private IdsCacheService() {
		// hide constructor
	}

	public static IdentifierFile get(IdsEnum name) throws CacheException {
		return get(name.getResource());
	}

	public static IdentifierFile get(String name) throws CacheException {
		String uName = name.toUpperCase();
		IdentifierFile file = common.get(uName);
		if (file == null) {
			try {
				file = IdentifierFactory.getIdentifierFile(uName);
			} catch (FactoryException e) {
				throw new CacheException(e);
			}
			common.put(uName, file);
		}
		return file;
	}
}
