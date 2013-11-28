package com.pourbaix.script.creature.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pourbaix.script.creature.generator.Block;
import com.pourbaix.script.creature.generator.GeneratorException;
import com.pourbaix.script.creature.keyword.KeywordValue;

/**
 * Service related to action operations.
 * 
 */
@Service
public class ActionService {

	@Resource
	private ActionFactory actionFactory;

	/**
	 * Add a contingency to block.
	 * 
	 * @param block
	 * @param keywordValues
	 * @throws GeneratorException
	 */
	public void addContingency(final Block block, final List<KeywordValue> keywordValues) throws GeneratorException {
		for (KeywordValue kvResponse : keywordValues) {
			block.addResponse(Integer.valueOf(kvResponse.getName()));
			for (KeywordValue kvSpell : kvResponse.getValues()) {
				KeywordValue kv = new KeywordValue(ActionEnum.ReallyForceSpell.toString());
				kv.addValue(new KeywordValue(kvSpell.getName()));
				Action action = actionFactory.getAction(kv);
				block.getCurrentResponse().addAction(action);
			}
		}
	}

	/**
	 * Add one or several actions to block.
	 * 
	 * @param block
	 * @param keywordValues
	 * @throws GeneratorException
	 */
	public void addActions(final Block block, final List<KeywordValue> keywordValues) throws GeneratorException {
		for (KeywordValue kv : keywordValues) {
			Action action = actionFactory.getAction(kv);
			block.getCurrentResponse().addAction(action);
		}
	}

}
