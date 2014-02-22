package com.pourbaix.creature.editor.service;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Parameter;
import com.pourbaix.creature.editor.domain.ParameterBooleanValue;
import com.pourbaix.creature.editor.domain.ParameterFolderValue;
import com.pourbaix.creature.editor.domain.ParameterIntegerValue;
import com.pourbaix.creature.editor.domain.ParameterListValue;
import com.pourbaix.creature.editor.domain.ParameterStringValue;
import com.pourbaix.creature.editor.repository.ParameterRepository;
import com.pourbaix.infinity.datatype.GameVersionEnum;

@Service
public class ParameterService {

	enum ParamEnum {
		GAME_DIRECTORY, GAME_VERSION, DEFAULT_LANGUAGE, TOBEX_ACTIVE, TARGET_ALLEGIANCE, TARGET_NEAREST_COUNT, TARGET_TYPE_COUNT, TARGET_RANDOM, PROBABILITY_ACTION_SPELL, PROBABILITY_ACTION_ITEM, CHECK_SPELL_PROTECTION, CHECK_WEAPON_PROTECTION, CHECK_ELEMENTAL_RESISTANCE, CHECK_MIND_IMMUNITY, CHECK_DEATH_IMMUNITY, CHECK_POISON_RESISTANCE, CHECK_MAGIC_DAMAGE_RESISTANCE, CHECK_MAGIC_RESISTANCE
	}

	@Resource
	private ParameterRepository parameterRepository;

	public File getGameDirectory() {
		Parameter param = parameterRepository.findOne(ParamEnum.GAME_DIRECTORY.toString());
		return new File(((ParameterFolderValue) param).getValue());
	}

	public GameVersionEnum getGameVersion() {
		Parameter param = parameterRepository.findOne(ParamEnum.GAME_VERSION.toString());
		String value = ((ParameterListValue) param).getValue();
		return StringUtils.isNotBlank(value) ? GameVersionEnum.valueOf(value) : null;
	}

	public String getDefaultLanguage() {
		Parameter param = parameterRepository.findOne(ParamEnum.DEFAULT_LANGUAGE.toString());
		return ((ParameterListValue) param).getValue();
	}

	public boolean isTobexActive() {
		Parameter param = parameterRepository.findOne(ParamEnum.TOBEX_ACTIVE.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

	public String getTargetAllegiance() {
		Parameter param = parameterRepository.findOne(ParamEnum.TARGET_ALLEGIANCE.toString());
		return ((ParameterStringValue) param).getValue();
	}

	public int getTargetNearestCount() {
		Parameter param = parameterRepository.findOne(ParamEnum.TARGET_NEAREST_COUNT.toString());
		return ((ParameterIntegerValue) param).getValue();
	}

	public int getTargetTypeCount() {
		Parameter param = parameterRepository.findOne(ParamEnum.TARGET_TYPE_COUNT.toString());
		return ((ParameterIntegerValue) param).getValue();
	}

	public boolean isTargetRandom() {
		Parameter param = parameterRepository.findOne(ParamEnum.TARGET_RANDOM.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

	public int getProbabilityActionSpell() {
		Parameter param = parameterRepository.findOne(ParamEnum.PROBABILITY_ACTION_SPELL.toString());
		return ((ParameterIntegerValue) param).getValue();
	}

	public int getProbabilityActionItem() {
		Parameter param = parameterRepository.findOne(ParamEnum.PROBABILITY_ACTION_ITEM.toString());
		return ((ParameterIntegerValue) param).getValue();
	}

	public boolean isCheckSpellProtection() {
		Parameter param = parameterRepository.findOne(ParamEnum.CHECK_SPELL_PROTECTION.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckWeaponProtection() {
		Parameter param = parameterRepository.findOne(ParamEnum.CHECK_WEAPON_PROTECTION.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckElementalResistance() {
		Parameter param = parameterRepository.findOne(ParamEnum.CHECK_ELEMENTAL_RESISTANCE.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckMindImmunity() {
		Parameter param = parameterRepository.findOne(ParamEnum.CHECK_MIND_IMMUNITY.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckDeathImmunity() {
		Parameter param = parameterRepository.findOne(ParamEnum.CHECK_DEATH_IMMUNITY.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckPoisonResistance() {
		Parameter param = parameterRepository.findOne(ParamEnum.CHECK_POISON_RESISTANCE.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckMagicDamageResistance() {
		Parameter param = parameterRepository.findOne(ParamEnum.CHECK_MAGIC_DAMAGE_RESISTANCE.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

	public boolean isCheckMagicResistance() {
		Parameter param = parameterRepository.findOne(ParamEnum.CHECK_MAGIC_RESISTANCE.toString());
		return ((ParameterBooleanValue) param).getValue();
	}

}
