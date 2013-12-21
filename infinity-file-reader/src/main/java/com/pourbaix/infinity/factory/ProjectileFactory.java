package com.pourbaix.infinity.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pourbaix.infinity.cache.CacheException;
import com.pourbaix.infinity.cache.IdsCacheService;
import com.pourbaix.infinity.datatype.Flag;
import com.pourbaix.infinity.datatype.IdsEnum;
import com.pourbaix.infinity.datatype.ProjectileTypeEnum;
import com.pourbaix.infinity.entity.IdentifierEntry;
import com.pourbaix.infinity.entity.IdentifierFile;
import com.pourbaix.infinity.entity.Projectile;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.DynamicArray;

public abstract class ProjectileFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProjectileFactory.class);

	private static final String[] BEHAVIOR_FLAGS = { "No flags set", "Show sparks", "Use z coordinate", "Loop fire sound", "Loop impact sound",
			"Do not affect direct target", "Draw below animate objects" };

	private static final String[] AREA_FLAGS = { "Trap not visible", "Trap visible", "Triggered by inanimates", "Triggered by condition", "Delayed trigger",
			"Secondary projectile", "Fragments", "Not affecting allies", "Not affecting enemies", "Mage-level duration", "Cleric-level duration",
			"Draw animation", "Cone-shaped", "Ignore visibility", "Delayed explosion", "Skip first condition", "Affect only one target" };

	public static Projectile getProjectileByIdsKey(Long reference) throws FactoryException, CacheException {
		IdentifierFile projectileIdentifier = IdsCacheService.get(IdsEnum.Projectile);
		IdentifierEntry identifierEntry = projectileIdentifier.getEntryByKey(reference - 1);
		if (identifierEntry == null) {
			return null;
		}
		Projectile projectile = getProjectile(identifierEntry.getFirstValue() + ".PRO");
		projectile.setReference(reference);
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
			byte buffer[] = entry.getResourceData();
			Projectile projectile = new Projectile();
			projectile.setResource(entry.getResourceName());
			projectile.setType(ProjectileTypeEnum.valueOf((long) DynamicArray.getUnsignedShort(buffer, 8)));
			projectile.setSpeed((int) DynamicArray.getShort(buffer, 10));
			projectile.setBehaviorFlags(new Flag((long) DynamicArray.getInt(buffer, 12), 4, BEHAVIOR_FLAGS));
			if (projectile.getType() == ProjectileTypeEnum.AreaOfEffect) {
				int offset = 512;
				projectile.setAreaOfEffectFlags(new Flag((long) DynamicArray.getInt(buffer, offset), 4, AREA_FLAGS));
				projectile.setTriggerRadius((int) DynamicArray.getShort(buffer, offset + 4));
				projectile.setAreaOfEffectRadius((int) DynamicArray.getShort(buffer, offset + 6));
				projectile.setConeWidth((int) DynamicArray.getShort(buffer, offset + 36));
			}
			return projectile;
		} catch (Exception e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}
}
