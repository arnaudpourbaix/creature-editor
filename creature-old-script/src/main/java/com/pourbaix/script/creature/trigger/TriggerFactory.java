package com.pourbaix.script.creature.trigger;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.pourbaix.script.creature.generator.GeneratorException;
import com.pourbaix.script.creature.keyword.KeywordValue;
import com.pourbaix.script.creature.target.OutputTargetEnum;
import com.pourbaix.script.creature.utils.Constant;

@Service
public class TriggerFactory {

	public Trigger getTrigger(final TriggerEnum type, final OutputTargetEnum target, final boolean result, final List<KeywordValue> values)
			throws GeneratorException {
		// convert values into an array of string
		String[] params = new String[values.size()];
		for (int i = 0; i < values.size(); i++) {
			params[i] = values.get(i).getName();
		}
		return getTrigger(type, target, result, params);
	}

	public Trigger getTrigger(final TriggerEnum type, final OutputTargetEnum target, final boolean result, final String... params) throws GeneratorException {
		Trigger trigger = new Trigger(type, target, result);
		addParameters(trigger, params);
		return trigger;
	}

	public void addParameters(final Trigger trigger, final String... params) throws GeneratorException {
		if (trigger.getType().getParams().isEmpty()) {
			return;
		}
		int i = 0;
		for (String param : trigger.getType().getParams().split(",")) {
			param = param.trim();
			if (param.equals(Constant.TriggerParam.OBJECT)) {
				trigger.getParams().put(param, trigger.getTarget().getOutput());
			} else {
				if (i >= params.length) {
					throw new GeneratorException("missing parameters for " + trigger.getType().toString() + ": " + StringUtils.join(params));
				}
				trigger.getParams().put(param, params[i++]);
			}
		}
	}

}
