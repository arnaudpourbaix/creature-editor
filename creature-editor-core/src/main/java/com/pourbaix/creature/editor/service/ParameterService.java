package com.pourbaix.creature.editor.service;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Parameter;
import com.pourbaix.creature.editor.domain.ParameterBooleanValue;
import com.pourbaix.creature.editor.domain.ParameterEnum;
import com.pourbaix.creature.editor.domain.ParameterFolderValue;
import com.pourbaix.creature.editor.domain.ParameterIntegerValue;
import com.pourbaix.creature.editor.domain.ParameterListValue;
import com.pourbaix.creature.editor.domain.ParameterStringValue;
import com.pourbaix.creature.editor.repository.ParameterRepository;
import com.pourbaix.infinity.datatype.GameVersionEnum;

@Service
public class ParameterService {

	@Resource
	private ParameterRepository parameterRepository;

	public File getGameDirectory() {
		Parameter param = parameterRepository.findOne(ParameterEnum.GAME_DIRECTORY);
		return new File(((ParameterFolderValue) param).getValue());
	}

	public GameVersionEnum getGameVersion() {
		Parameter param = parameterRepository.findOne(ParameterEnum.GAME_VERSION);
		String value = ((ParameterListValue) param).getValue();
		return StringUtils.isNotBlank(value) ? GameVersionEnum.valueOf(value) : null;
	}

	public String getDefaultLanguage() {
		Parameter param = parameterRepository.findOne(ParameterEnum.DEFAULT_LANGUAGE);
		return ((ParameterListValue) param).getValue();
	}

	public boolean isTobexActive() {
		Parameter param = parameterRepository.findOne(ParameterEnum.TOBEX_ACTIVE);
		return ((ParameterBooleanValue) param).getValue();
	}

	public String getTargetAllegiance() {
		Parameter param = parameterRepository.findOne(ParameterEnum.TARGET_ALLEGIANCE);
		return ((ParameterStringValue) param).getValue();
	}

	public int getTargetNearestCount() {
		Parameter param = parameterRepository.findOne(ParameterEnum.TARGET_NEAREST_COUNT);
		return ((ParameterIntegerValue) param).getValue();
	}

	public int getTargetTypeCount() {
		Parameter param = parameterRepository.findOne(ParameterEnum.TARGET_TYPE_COUNT);
		return ((ParameterIntegerValue) param).getValue();
	}

	public boolean isTargetRandom() {
		Parameter param = parameterRepository.findOne(ParameterEnum.TARGET_RANDOM);
		return ((ParameterBooleanValue) param).getValue();
	}

	public int getProbabilityActionSpell() {
		Parameter param = parameterRepository.findOne(ParameterEnum.PROBABILITY_ACTION_SPELL);
		return ((ParameterIntegerValue) param).getValue();
	}

	public int getProbabilityActionItem() {
		Parameter param = parameterRepository.findOne(ParameterEnum.PROBABILITY_ACTION_ITEM);
		return ((ParameterIntegerValue) param).getValue();
	}

	public boolean isCheckSpellProtection() {
		Parameter param = parameterRepository.findOne(ParameterEnum.CHECK_SPELL_PROTECTION);
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckWeaponProtection() {
		Parameter param = parameterRepository.findOne(ParameterEnum.CHECK_WEAPON_PROTECTION);
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckElementalResistance() {
		Parameter param = parameterRepository.findOne(ParameterEnum.CHECK_ELEMENTAL_RESISTANCE);
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckMindImmunity() {
		Parameter param = parameterRepository.findOne(ParameterEnum.CHECK_MIND_IMMUNITY);
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckDeathImmunity() {
		Parameter param = parameterRepository.findOne(ParameterEnum.CHECK_DEATH_IMMUNITY);
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckPoisonResistance() {
		Parameter param = parameterRepository.findOne(ParameterEnum.CHECK_POISON_RESISTANCE);
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckMagicDamageResistance() {
		Parameter param = parameterRepository.findOne(ParameterEnum.CHECK_MAGIC_DAMAGE_RESISTANCE);
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckMagicResistance() {
		Parameter param = parameterRepository.findOne(ParameterEnum.CHECK_MAGIC_RESISTANCE);
		return ((ParameterBooleanValue) param).getValue();
	}

}
