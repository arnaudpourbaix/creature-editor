package com.pourbaix.infinity.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.datatype.AbilityLocationEnum;
import com.pourbaix.infinity.datatype.AbilityTargetEnum;
import com.pourbaix.infinity.datatype.AbilityTypeEnum;
import com.pourbaix.infinity.datatype.UnknownValueException;
import com.pourbaix.infinity.domain.Ability;
import com.pourbaix.infinity.util.DynamicArray;

@Service
public class AbilityFactory {

	private static final Logger logger = LoggerFactory.getLogger(AbilityFactory.class);

	@Autowired
	private ProjectileFactory projectileFactory;

	@Autowired
	private EffectFactory effectFactory;

	public List<Ability> getAbilities(byte buffer[], int offset, int count, int globalEffectOffset) throws FactoryException {
		List<Ability> abilities = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			Ability ability = getAbility(buffer, offset, globalEffectOffset);
			abilities.add(ability);
			offset += 40;
		}
		return abilities;
	}

	private Ability getAbility(byte buffer[], int offset, int globalEffectOffset) throws FactoryException {
		Ability ability = new Ability();
		try {
			ability.setType(AbilityTypeEnum.valueOf(DynamicArray.getShort(buffer, offset)));
			ability.setLocation(AbilityLocationEnum.valueOf(DynamicArray.getShort(buffer, offset + 2)));
			ability.setTarget(AbilityTargetEnum.valueOf(DynamicArray.getByte(buffer, offset + 12)));
			ability.setTargetCount(DynamicArray.getByte(buffer, offset + 13));
			ability.setRange(DynamicArray.getShort(buffer, offset + 14));
			ability.setLevel((byte) DynamicArray.getShort(buffer, offset + 16));
			ability.setCastingTime((byte) DynamicArray.getShort(buffer, offset + 18));
			fetchProjectile(ability, buffer, offset);
			fetchEffects(ability, buffer, offset, globalEffectOffset);
		} catch (UnknownValueException e) {
			throw new FactoryException(e);
		}
		return ability;
	}

	private void fetchProjectile(Ability ability, byte buffer[], int offset) throws FactoryException {
		try {
			Long key = (long) DynamicArray.getUnsignedShort(buffer, offset + 38);
			ability.setProjectile(projectileFactory.getProjectileByIdsKey(key));
		} catch (FactoryException e) {
			logger.error(e.getMessage());
		}
	}

	private void fetchEffects(Ability ability, byte buffer[], int offset, int globalEffectOffset) throws FactoryException {
		int effectIndex = (int) DynamicArray.getShort(buffer, offset + 32);
		int effectCount = (int) DynamicArray.getShort(buffer, offset + 30);
		ability.setEffects(effectFactory.getEffects(buffer, globalEffectOffset + (effectIndex * 48), effectCount));
	}

}
