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

	private static final String[] s_behave = { "No flags set", "Show sparks", "Use z coordinate", "Loop fire sound", "Loop impact sound",
			"Do not affect direct target", "Draw below animate objects" };

	private static final String[] s_areaflags = { "Trap not visible", "Trap visible", "Triggered by inanimates", "Triggered by condition", "Delayed trigger",
			"Secondary projectile", "Fragments", "Not affecting allies", "Not affecting enemies", "Mage-level duration", "Cleric-level duration",
			"Draw animation", "Cone-shaped", "Ignore visibility", "Delayed explosion", "Skip first condition", "Affect only one target" };

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

			if (projectile.getType() == ProjectileTypeEnum.AreaOfEffect) {
				int offset = 512;
				projectile.setAreaOfEffectFlags(new Flag((long) DynamicArray.getInt(buffer, offset), 4, s_areaflags));
				projectile.setTriggerRadius((int) DynamicArray.getShort(buffer, offset + 4));
				projectile.setAreaOfEffectRadius((int) DynamicArray.getShort(buffer, offset + 6));
				//				list.add(new ResourceRef(buffer, offset + 8, "Explosion sound", "WAV"));
				//				list.add(new DecNumber(buffer, offset + 16, 2, "Explosion frequency (frames)"));
				//				list.add(new IdsBitmap(buffer, offset + 18, 2, "Fragment animation", "ANIMATE.IDS"));
				//				list.add(new ProRef(buffer, offset + 20, "Secondary projectile"));
				//				list.add(new DecNumber(buffer, offset + 22, 1, "# repetitions"));
				//				list.add(new HashBitmap(buffer, offset + 23, 1, "Explosion effect", s_proj));
				//				list.add(new ColorValue(buffer, offset + 24, 1, "Explosion color"));
				//				list.add(new Unknown(buffer, offset + 25, 1, "Unused"));
				//				list.add(new ProRef(buffer, offset + 26, "Explosion projectile"));
				//				list.add(new ResourceRef(buffer, offset + 28, "Explosion animation", new String[] { "VVC", "BAM" }));
				//				list.add(new DecNumber(buffer, offset + 36, 2, "Cone width"));
				//				list.add(new Unknown(buffer, offset + 38, 218));
			}
			logger.debug(projectile.toString());
			return projectile;
		} catch (Exception e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}
}
