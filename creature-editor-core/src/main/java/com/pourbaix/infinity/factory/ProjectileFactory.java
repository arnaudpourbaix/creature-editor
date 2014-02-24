package com.pourbaix.infinity.factory;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.datatype.Flag;
import com.pourbaix.infinity.datatype.IdsEnum;
import com.pourbaix.infinity.datatype.ProjectileTypeEnum;
import com.pourbaix.infinity.datatype.UnknownValueException;
import com.pourbaix.infinity.domain.IdentifierEntry;
import com.pourbaix.infinity.domain.IdentifierFile;
import com.pourbaix.infinity.domain.Projectile;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.DynamicArray;

@Service
public class ProjectileFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProjectileFactory.class);

	@Resource
	private IdentifierFactory identifierFactory;

	private static final String MISSING_PROJECTILE_FILE = "MISSING_PROJECTILE_FILE";
	private static final String INVALID_PROJECTILE_FILE = "INVALID_PROJECTILE_FILE";
	private static final String UNKNOWN_PROJECTILE_TYPE = "UNKNOWN_PROJECTILE_TYPE";

	private static final String[] BEHAVIOR_FLAGS = { "No flags set", "Show sparks", "Use z coordinate", "Loop fire sound", "Loop impact sound",
			"Do not affect direct target", "Draw below animate objects" };

	private static final String[] AREA_FLAGS = { "Trap not visible", "Trap visible", "Triggered by inanimates", "Triggered by condition", "Delayed trigger",
			"Secondary projectile", "Fragments", "Not affecting allies", "Not affecting enemies", "Mage-level duration", "Cleric-level duration",
			"Draw animation", "Cone-shaped", "Ignore visibility", "Delayed explosion", "Skip first condition", "Affect only one target" };

	public Projectile getProjectileByIdsKey(Long reference) throws FactoryException {
		IdentifierFile projectileIdentifier = identifierFactory.getIdentifierFile(IdsEnum.Projectile);
		IdentifierEntry identifierEntry = projectileIdentifier.getEntryByKey(reference - 1);
		if (identifierEntry == null) {
			return null;
		}
		Projectile projectile = getProjectile(identifierEntry.getFirstValue() + ".PRO");
		projectile.setReference(reference);
		return projectile;
	}

	public Projectile getProjectile(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException(MISSING_PROJECTILE_FILE, entryName);
		}
		return getProjectile(entry);
	}

	public Projectile getProjectile(ResourceEntry entry) throws FactoryException {
		byte buffer[];
		try {
			buffer = entry.getResourceData();
		} catch (IOException e) {
			throw new FactoryException(INVALID_PROJECTILE_FILE, entry.getResourceName());
		}
		Projectile projectile = new Projectile();
		projectile.setResource(entry.getResourceName());
		try {
			projectile.setType(ProjectileTypeEnum.valueOf((long) DynamicArray.getUnsignedShort(buffer, 8)));
		} catch (UnknownValueException e) {
			throw new FactoryException(UNKNOWN_PROJECTILE_TYPE, entry.getResourceName());
		}
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
	}
}
