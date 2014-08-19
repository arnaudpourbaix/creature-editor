package com.pourbaix.infinity.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.pourbaix.infinity.datatype.Flag;
import com.pourbaix.infinity.datatype.ResistanceEnum;
import com.pourbaix.infinity.datatype.TargetTypeEnum;
import com.pourbaix.infinity.datatype.TimingEnum;
import com.pourbaix.infinity.datatype.UnknownValueException;
import com.pourbaix.infinity.domain.Effect;
import com.pourbaix.infinity.domain.EffectParameter;
import com.pourbaix.infinity.domain.Opcode;
import com.pourbaix.infinity.domain.OpcodeParameter;
import com.pourbaix.infinity.domain.OpcodeParameterLink;
import com.pourbaix.infinity.domain.OpcodeParameterValue;
import com.pourbaix.infinity.repository.OpcodeRepository;
import com.pourbaix.infinity.util.DynamicArray;

@Service
@Transactional
public class EffectFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EffectFactory.class);

	@Resource
	private OpcodeRepository opcodeRepository;

	@Resource
	private IdentifierFactory identifierFactory;

	private static final String INVALID_EFFECT_TARGET_TYPE = "INVALID_EFFECT_TARGET_TYPE";
	private static final String INVALID_EFFECT_TIMING = "INVALID_EFFECT_TIMING";
	private static final String INVALID_EFFECT_RESISTANCE = "INVALID_EFFECT_RESISTANCE";

	private static final String[] SAVE_TYPES = { "No save", "Spell", "Breath weapon", "Paralyze/Poison/Death", "Rod/Staff/Wand", "Petrify/Polymorph", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "Ex: bypass mirror image", "EE: ignore difficulty" };

	public List<Effect> getEffects(String resource, byte buffer[], int offset, int count) throws FactoryException {
		List<Effect> effects = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			Effect effect = getEffect(resource, buffer, offset);
			effects.add(effect);
			offset += 48;
		}
		return effects;
	}

	private Effect getEffect(String resource, byte buffer[], int offset) throws FactoryException {
		Effect effect = new Effect();
		fetchOpcode(effect, buffer, offset);
		try {
			effect.setTarget(TargetTypeEnum.valueOf(DynamicArray.getByte(buffer, offset + 2)));
		} catch (UnknownValueException e) {
			throw new FactoryException(INVALID_EFFECT_TARGET_TYPE, resource);
		}
		effect.setPower(DynamicArray.getByte(buffer, offset + 3));
		try {
			effect.setTiming(TimingEnum.valueOf(DynamicArray.getByte(buffer, offset + 12)));
		} catch (UnknownValueException e) {
			throw new FactoryException(INVALID_EFFECT_TIMING, resource);
		}
		try {
			effect.setResistance(ResistanceEnum.valueOf(DynamicArray.getByte(buffer, offset + 13)));
		} catch (UnknownValueException e) {
			throw new FactoryException(INVALID_EFFECT_RESISTANCE, resource);
		}
		effect.setDuration(DynamicArray.getInt(buffer, offset + 14));
		effect.setProbability1(DynamicArray.getByte(buffer, offset + 18));
		effect.setProbability2(DynamicArray.getByte(buffer, offset + 19));
		effect.setDiceThrown((short) DynamicArray.getInt(buffer, offset + 28));
		effect.setDiceSides((short) DynamicArray.getInt(buffer, offset + 32));
		effect.setSavingThrowType(new Flag(DynamicArray.getInt(buffer, offset + 36), 4, SAVE_TYPES));
		effect.setSavingThrowBonus((short) DynamicArray.getInt(buffer, offset + 40));
		fetchResource(effect, buffer, offset);
		return effect;
	}

	private void fetchOpcode(Effect effect, byte buffer[], int offset) throws FactoryException {
		int opcodeId = (int) DynamicArray.getShort(buffer, offset);
		effect.setOpcodeId(opcodeId);
		Opcode opcode = opcodeRepository.findOpcodeById(opcodeId);
		if (opcode == null) {
			return;
		}
		effect.setOpcode(opcode.getName());
		int param1Value = DynamicArray.getInt(buffer, offset + 4);
		int param2Value = DynamicArray.getInt(buffer, offset + 8);
		fetchParameter(effect, opcode, effect.getParam1(), 1, param1Value);
		fetchParameter(effect, opcode, effect.getParam2(), 2, param2Value);
	}

	private void fetchResource(Effect effect, byte buffer[], int offset) throws FactoryException {
		byte[] resource = Arrays.copyOfRange(buffer, offset + 20, offset + 28);
		effect.setResource(DynamicArray.getString(resource, 0, resource.length));
	}

	private void fetchParameter(Effect effect, Opcode opcode, EffectParameter effectParameter, int order, int paramValue) throws FactoryException {
		// default value
		effectParameter.setValue(String.valueOf(paramValue));
		// find parameter
		OpcodeParameter parameter = null;
		for (OpcodeParameterLink param : opcode.getParameters()) {
			if (param.getOrder() == order) {
				parameter = param.getParameter();
			}
		}
		if (parameter == null) {
			return;
		}
		// find value
		effectParameter.setName(parameter.getName());
		if (!StringUtils.isEmpty(parameter.getIds())) {
			effectParameter.setValue(identifierFactory.getFirstValueByKey(parameter.getIds() + ".IDS", Long.valueOf(paramValue)));
		} else {
			for (OpcodeParameterValue value : parameter.getValues()) {
				if (value.getValue() == paramValue) {
					effectParameter.setValue(value.getLabel());
					effectParameter.setRawValue(value.getValue());
				}
			}
		}
	}

}
