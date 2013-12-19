package com.pourbaix.infinity.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pourbaix.infinity.cache.CacheException;
import com.pourbaix.infinity.cache.IdsCacheService;
import com.pourbaix.infinity.entity.Flag;
import com.pourbaix.infinity.entity.IdentifierEntry;
import com.pourbaix.infinity.entity.IdentifierFile;
import com.pourbaix.infinity.entity.IdsEnum;
import com.pourbaix.infinity.entity.Projectile;
import com.pourbaix.infinity.entity.ProjectileTypeEnum;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.resource.pro.ProResource;
import com.pourbaix.infinity.util.DynamicArray;

public abstract class ProjectileFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProjectileFactory.class);

	private static final String[] s_behave = { "No flags set", "Show sparks", "Use height", "Loop fire sound", "Loop impact sound", "Ignore center",
			"Draw as background" };

	public static Projectile getProjectileByIdsKey(Long reference) throws FactoryException, CacheException {
		IdentifierFile projectileIdentifier = IdsCacheService.get(IdsEnum.Projectile);
		IdentifierEntry identifierEntry = projectileIdentifier.getEntryByKey(reference - 1);
		if (identifierEntry == null) {
			return null;
		}
		// Projectile projectile = new Projectile();
		// projectile.setReference(reference);
		// projectile.setResource(identifierEntry.getFirstValue());
		Projectile projectile = getProjectile(identifierEntry.getFirstValue() + ".PRO");
		return projectile;
	}

	public static Projectile getProjectile(String entryName) throws FactoryException, CacheException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException("Entry doesn't exist: " + entryName);
		}
		return getProjectile(entry);
	}

	public static Projectile getProjectile(ResourceEntry entry) throws FactoryException, CacheException {
		try {
			ProResource pro = new ProResource(entry);
			Projectile projectile = new Projectile();
			projectile.setResource(entry.getResourceName());
			byte buffer[] = entry.getResourceData();

			projectile.setType(ProjectileTypeEnum.valueOf((long) DynamicArray.getUnsignedShort(buffer, 8)));
			projectile.setSpeed((int) DynamicArray.getShort(buffer, 10));
			projectile.setBehaviorFlags(new Flag((long) DynamicArray.getInt(buffer, 12), 4, s_behave));

			// if (projtype.getValue() > 1L) {
			// ProSingleType single = new ProSingleType(this, buffer, offset);
			// list.add(single);
			// offset += single.getSize();
			// }
			// if (projtype.getValue() > 2L) {
			// ProAreaType area = new ProAreaType(this, buffer, offset);
			// list.add(area);
			// offset += area.getSize();
			// }

			return projectile;
		} catch (Exception e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}
}
