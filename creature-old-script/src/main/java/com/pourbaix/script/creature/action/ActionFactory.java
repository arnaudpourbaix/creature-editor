package com.pourbaix.script.creature.action;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.pourbaix.script.creature.generator.GeneratorException;
import com.pourbaix.script.creature.keyword.KeywordValue;
import com.pourbaix.script.creature.spell.SpellFactory;
import com.pourbaix.script.creature.target.OutputTargetEnum;
import com.pourbaix.script.creature.utils.Constant;

@Service
public class ActionFactory {

	@Resource
	private SpellFactory spellFactory;

	public Action getAction(final KeywordValue kv) throws GeneratorException {
		final ActionEnum name = ActionEnum.fromString(kv.getName());
		if (name == ActionEnum.Spell || name == ActionEnum.SpellRES || name == ActionEnum.SpellNoDec || name == ActionEnum.SpellPoint
				|| name == ActionEnum.SpellPointRES || name == ActionEnum.SpellPointNoDec || name == ActionEnum.ForceSpell || name == ActionEnum.ForceSpellRES
				|| name == ActionEnum.ForceSpellRange || name == ActionEnum.ForceSpellRangeRES || name == ActionEnum.ForceSpellPoint
				|| name == ActionEnum.ForceSpellPointRES || name == ActionEnum.ForceSpellPointRange || name == ActionEnum.ForceSpellPointRangeRES
				|| name == ActionEnum.ReallyForceSpell || name == ActionEnum.ReallyForceSpellRES || name == ActionEnum.ReallyForceSpellPoint
				|| name == ActionEnum.ReallyForceSpellPointRES || name == ActionEnum.ReallyForceSpellDead || name == ActionEnum.ReallyForceSpellDeadRES) {

			return getSpellAction(name, kv);
		} else {
			return getBasicAction(name, kv);
		}
	}

	public Action getBasicAction(final ActionEnum name, final KeywordValue kv) throws GeneratorException {
		final Action action = new BasicAction(name);
		action.setTarget(OutputTargetEnum.ENEMY);
		// convert values into an array of string
		String[] params = new String[kv.getValues().size()];
		for (int i = 0; i < kv.getValues().size(); i++) {
			params[i] = kv.getValues().get(i).getName();
		}
		addParameters(action, params);
		return action;
	}

	public Action getSpellAction(final ActionEnum name, final KeywordValue kv) throws GeneratorException {
		final SpellAction action = new SpellAction(name);
		if (kv == null || kv.getName().isEmpty()) {
			throw new GeneratorException("Invalid action param: " + name.toString());
		}
		action.setSpell(spellFactory.getSpell(kv.getValues().get(0).getName()));
		action.setIdentifier(StringUtils.isNotBlank(action.getSpell().getIdentifier()));
		action.setTarget(action.getSpell().isTargetEnemy() ? OutputTargetEnum.LASTSEEN : OutputTargetEnum.MYSELF);
		if (action.isIdentifier() && name.toString().endsWith(Constant.Action.RES)) {
			action.setName(ActionEnum.fromString(name.toString().substring(0, name.toString().length() - Constant.Action.RES.length())));
		} else if (!action.isIdentifier() && !name.toString().endsWith(Constant.Action.RES)) {
			action.setName(ActionEnum.fromString(name.toString() + Constant.Action.RES));
		}
		String param = action.isIdentifier() ? action.getSpell().getIdentifier() : action.getSpell().getResource();
		addParameters(action, param);
		return action;
	}

	public void addParameters(final Action action, final String... params) throws GeneratorException {
		if (action.getName().getParams().isEmpty()) {
			return;
		}
		int i = 0;
		for (String param : action.getName().getParams().split(",")) {
			param = param.trim();
			if (param.equals(Constant.ActionParam.TARGET)) {
				action.getParams().put(param, action.getTarget().getOutput());
			} else {
				if (i >= params.length) {
					throw new GeneratorException("missing parameters for " + action.getName().toString() + ": " + StringUtils.join(params));
				}
				action.getParams().put(param, params[i++]);
			}
		}
	}

}
