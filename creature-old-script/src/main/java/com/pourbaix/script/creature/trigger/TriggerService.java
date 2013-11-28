package com.pourbaix.script.creature.trigger;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pourbaix.script.creature.context.GlobalContext;
import com.pourbaix.script.creature.generator.Block;
import com.pourbaix.script.creature.generator.GeneratorException;
import com.pourbaix.script.creature.keyword.KeywordValue;
import com.pourbaix.script.creature.target.OutputTargetEnum;
import com.pourbaix.script.creature.utils.Constant;

/**
 * Service related to trigger operations.
 * 
 */
@Service
public class TriggerService {

	@Resource
	private GlobalContext globalContext;
	@Resource
	private TriggerFactory triggerFactory;

	/**
	 * Add one or several triggers to block.
	 * 
	 * @param block
	 * @param keywordValues
	 * @param target
	 * @throws GeneratorException
	 */
	public void addTriggers(final Block block, final List<KeywordValue> keywordValues, final OutputTargetEnum target) throws GeneratorException {
		for (KeywordValue kv : keywordValues) {
			boolean result = !kv.getName().startsWith("!");
			String name = kv.getName().startsWith("!") ? kv.getName().substring(1) : kv.getName();
			block.addTriggers(getTriggers(name, target, result, kv.getValues()));
		}
	}

	public List<Trigger> getTriggers(final String name, final OutputTargetEnum target, final boolean result, final List<KeywordValue> params)
			throws GeneratorException {
		List<Trigger> triggers = new ArrayList<Trigger>();
		Trigger trigger = getShortcutTrigger(name, target, result, params);
		if (trigger == null) {
			trigger = triggerFactory.getTrigger(TriggerEnum.fromString(name), target, result, params);
		}
		triggers.add(trigger);
		return triggers;
	}

	public Trigger getShortcutTrigger(final String name, final OutputTargetEnum target, final boolean result, final List<KeywordValue> params)
			throws GeneratorException {
		try {
			Trigger trigger = globalContext.getTrigger(name);
			if (result) {
				trigger.setType(TriggerEnum.OR);
				trigger.getParams().put(Constant.TriggerParam.OR_COUNT, String.valueOf(trigger.getTriggers().size()));
				for (Trigger t : trigger.getTriggers()) {
					t.getParams().put(Constant.TriggerParam.OBJECT, target.getOutput());
				}
			} else {
				if (trigger.getTriggers().isEmpty()) {
					trigger.getParams().put(Constant.TriggerParam.OBJECT, target.getOutput());
					trigger.setResult(false);
				} else {
					for (Trigger t : trigger.getTriggers()) {
						t.getParams().put(Constant.TriggerParam.OBJECT, target.getOutput());
						t.setResult(!t.isResult());
					}
				}
			}
			// check special cases
			// if (quickTrigger == ShortcutEnum.OUTDOOR) {
			// triggers.add(TriggerFactory.getTrigger(TriggerEnum.AreaType, target, result, quickTrigger.toString()));
			// }

			return trigger;
		} catch (GeneratorException e) {
			return null;
		}
	}

	public void addCheckStatTriggers(final List<Trigger> triggers, final ShortcutEnum quickTrigger, final OutputTargetEnum target, final boolean result,
			final List<KeywordValue> params) throws GeneratorException {

		if (quickTrigger == ShortcutEnum.HOLD) {
			triggers.add(triggerFactory.getTrigger(TriggerEnum.CheckStatGT, target, result, "0", StatEnum.HELD.toString()));
		}
	}

}
