package com.pourbaix.infinity.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pourbaix.infinity.cache.CacheException;
import com.pourbaix.infinity.entity.Ability;
import com.pourbaix.infinity.entity.AbilityLocationEnum;
import com.pourbaix.infinity.entity.AbilityTargetEnum;
import com.pourbaix.infinity.entity.AbilityTypeEnum;
import com.pourbaix.infinity.entity.UnknownValueException;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.util.DynamicArray;

public abstract class AbilityFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AbilityFactory.class);

	public static List<Ability> getAbilities(byte buffer[], int offset, int count, int globalEffectOffset) throws FactoryException, CacheException {
		List<Ability> abilities = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			Ability ability = getAbility(buffer, offset, globalEffectOffset);
			abilities.add(ability);
			offset += 40;
		}
		return abilities;
	}

	private static Ability getAbility(byte buffer[], int offset, int globalEffectOffset) throws FactoryException, CacheException {
		Ability ability = new Ability();
		try {
			ability.setType(AbilityTypeEnum.valueOf(DynamicArray.getShort(buffer, offset)));
			ability.setLocation(AbilityLocationEnum.valueOf(DynamicArray.getShort(buffer, offset + 2)));
			ability.setTarget(AbilityTargetEnum.valueOf(DynamicArray.getByte(buffer, offset + 12)));
			ability.setTargetCount(DynamicArray.getByte(buffer, offset + 13));
			ability.setRange(DynamicArray.getShort(buffer, offset + 14));
			ability.setLevel(DynamicArray.getShort(buffer, offset + 16));
			ability.setCastingTime(DynamicArray.getShort(buffer, offset + 18));
			fetchProjectile(ability, buffer, offset);
			fetchEffects(ability, buffer, offset, globalEffectOffset);
		} catch (UnknownValueException e) {
			throw new FactoryException(e);
		}
		return ability;
	}

	public static void fetchProjectile(Ability ability, byte buffer[], int offset) throws FactoryException, CacheException {
		Long key = (long) DynamicArray.getUnsignedShort(buffer, offset + 38);
		ability.setProjectile(ProjectileFactory.getProjectileByIdsKey(key));
	}

	public static void fetchEffects(Ability ability, byte buffer[], int offset, int globalEffectOffset) throws FactoryException, CacheException {
		int effectOffset = (int) DynamicArray.getShort(buffer, offset + 32);
		int effectCount = (int) DynamicArray.getShort(buffer, offset + 30);
		EffectFactory.getEffects(buffer, globalEffectOffset + effectOffset, effectCount);
	}

}
