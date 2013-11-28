package com.pourbaix.script.creature.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.pourbaix.script.creature.action.Action;
import com.pourbaix.script.creature.action.ActionService;
import com.pourbaix.script.creature.action.BasicAction;
import com.pourbaix.script.creature.action.SpellAction;
import com.pourbaix.script.creature.context.LocalContext;
import com.pourbaix.script.creature.keyword.Keyword;
import com.pourbaix.script.creature.spell.ClassEnum;
import com.pourbaix.script.creature.target.OutputTargetEnum;
import com.pourbaix.script.creature.trigger.Trigger;
import com.pourbaix.script.creature.trigger.TriggerEnum;
import com.pourbaix.script.creature.trigger.TriggerService;
import com.pourbaix.script.creature.utils.Constant;
import com.pourbaix.script.creature.utils.Tools;

/**
 * Service related to block operations.
 * 
 */
/**
 * @author apourbaix
 * 
 */
@Service
public class BlockService {

	@Resource
	private ActionService actionService;
	@Resource
	private TriggerService triggerService;

	/**
	 * Return a block based on keywords.
	 * 
	 * @param keywords
	 * @return
	 * @throws GeneratorException
	 */
	public Block getBlock(final List<Keyword> keywords) throws GeneratorException {
		final LocalContext context = LocalContext.getInstance();
		Block block = new Block();
		block.setKeywords(keywords);
		for (Keyword keyword : keywords) {
			if (keyword.getName().equalsIgnoreCase("action")) {
				actionService.addActions(block, keyword.getValues());
			} else if (keyword.getName().equalsIgnoreCase("target")) {

			} else if (keyword.getName().equalsIgnoreCase("requireSelf")) {
				triggerService.addTriggers(block, keyword.getValues(), OutputTargetEnum.MYSELF);
			} else if (keyword.getName().equalsIgnoreCase("require")) {
				triggerService.addTriggers(block, keyword.getValues(), OutputTargetEnum.ENEMY);
			} else if (keyword.getName().equalsIgnoreCase("contingency")) {
				actionService.addContingency(block, keyword.getValues());
			} else if (keyword.getName().equalsIgnoreCase("minorsequencer")) {

			} else if (keyword.getName().equalsIgnoreCase("include")) {

			} else if (keyword.getName().equalsIgnoreCase("spell")) {

			} else if (keyword.getName().equalsIgnoreCase("offensive_spells")) {

			} else if (keyword.getName().equalsIgnoreCase("class")) {
				context.setCasterClass(ClassEnum.fromString(keyword.getSingleValue()));
			} else if (keyword.getName().equalsIgnoreCase("caster_level")) {
				context.setCasterLevel(Integer.valueOf(keyword.getSingleValue()));
			} else if (keyword.getName().equalsIgnoreCase("max_spell_level")) {
				context.setCasterMaxSpellLevel(Integer.valueOf(keyword.getSingleValue()));
			} else {
				throw new GeneratorException("Unknown keyword: " + keyword.getName());
			}
		}
		return block;
	}

	/**
	 * Generate ouput text from a block.
	 * 
	 * @param block
	 * @return
	 * @throws GeneratorException
	 */
	public String generateOutput(final Block block) throws GeneratorException {
		if (block.getTriggers().isEmpty() || block.getResponses().isEmpty()) { // empty block
			return "";
		}
		// generate a list of lines
		List<Line> lines = new ArrayList<Line>();
		lines.add(new Line(Constant.Keyword.IF, 0));
		generateTriggers(lines, block.getTriggers(), 1);
		lines.add(new Line(Constant.Keyword.THEN, 0));
		generateResponses(lines, block.getResponses());
		lines.add(new Line(Constant.Keyword.END, 0));
		// generate an indented text
		return getIndentedText(lines);
	}

	/**
	 * Generate indented text from lines.
	 * 
	 * @param lines
	 * @return
	 * @throws GeneratorException
	 */
	public String getIndentedText(final List<Line> lines) throws GeneratorException {
		StringBuilder sb = new StringBuilder();
		for (Line line : lines) {
			sb.append(StringUtils.repeat(Constant.TAB, line.getTabulationCount())).append(line.getText()).append(Constant.CR);
		}
		return sb.toString() + Constant.CR;
	}

	/**
	 * Append all triggers in lines list.
	 * 
	 * @param lines
	 * @param triggers
	 * @param indentLevel
	 * @throws GeneratorException
	 */
	public void generateTriggers(final List<Line> lines, final List<Trigger> triggers, final int indentLevel) throws GeneratorException {
		for (Trigger trigger : triggers) {
			if (trigger.getType() != null) {
				String text = String.format("%s%s(%s)", (trigger.isResult() ? "" : "!"), trigger.getType().toString(), trigger.getType().getParams());
				for (Map.Entry<String, String> entry : trigger.getParams().entrySet()) {
					text = text.replace(entry.getKey(), entry.getValue());
				}
				lines.add(new Line(text, indentLevel));
			}
			if (!trigger.getTriggers().isEmpty()) {
				int newIndent = trigger.getType() == TriggerEnum.OR ? indentLevel + 1 : indentLevel;
				generateTriggers(lines, trigger.getTriggers(), newIndent);
			}
		}
	}

	/**
	 * Append all responses in lines list.
	 * 
	 * @param lines
	 * @param responses
	 * @throws GeneratorException
	 */
	public void generateResponses(final List<Line> lines, final List<Response> responses) throws GeneratorException {
		for (Response response : responses) {
			lines.add(new Line(String.format("%s #%d", Constant.Keyword.RESPONSE, response.getWeight()), 1));
			generateActions(lines, response.getActions());
		}
	}

	/**
	 * Append all actions in lines list.
	 * 
	 * @param lines
	 * @param actions
	 * @throws GeneratorException
	 */
	public void generateActions(final List<Line> lines, final List<Action> actions) throws GeneratorException {
		for (Action action : actions) {
			String text = "";
			if (action instanceof SpellAction) {
				text = getSpellAction((SpellAction) action);
			} else if (action instanceof BasicAction) {
				text = getBasicAction((BasicAction) action);
			} else {
				text = "Unknown action";
			}
			lines.add(new Line(text, 2));
		}
	}

	private String getBasicAction(final BasicAction action) throws GeneratorException {
		String text = String.format("%s(%s)", action.getName().toString(), action.getName().getParams());
		for (Map.Entry<String, String> entry : action.getParams().entrySet()) {
			String value = entry.getKey().equals(Constant.ActionParam.RESOURCE) ? Tools.encloseString(entry.getValue()) : entry.getValue();
			text = text.replace(entry.getKey(), value);
		}
		return text;
	}

	private String getSpellAction(final SpellAction action) throws GeneratorException {
		String text = String.format("%s(%s)", action.getName().toString(), action.getName().getParams());
		for (Map.Entry<String, String> entry : action.getParams().entrySet()) {
			String value = entry.getKey().equals(Constant.ActionParam.RESOURCE) ? Tools.encloseString(entry.getValue()) : entry.getValue();
			text = text.replace(entry.getKey(), value);
		}
		text += " // " + action.getSpell().getName();
		return text;
	}
}
