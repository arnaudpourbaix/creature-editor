package com.pourbaix.creature.script.instruction;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.pourbaix.creature.editor.domain.Action;
import com.pourbaix.creature.editor.domain.ActionKeyword;
import com.pourbaix.creature.editor.domain.Keyword;
import com.pourbaix.creature.editor.domain.KeywordTypeEnum;
import com.pourbaix.creature.editor.domain.Trigger;
import com.pourbaix.creature.editor.repository.ActionRepository;
import com.pourbaix.creature.editor.repository.KeywordRepository;
import com.pourbaix.creature.editor.repository.TriggerRepository;
import com.pourbaix.creature.script.keyword.RawKeywordParam;

@Service
public class InstructionService {

	private static final Logger logger = LoggerFactory.getLogger(InstructionService.class);

	@Autowired
	private KeywordRepository keywordRepository;
	@Autowired
	private TriggerRepository triggerRepository;
	@Autowired
	private ActionRepository actionRepository;

	public void parseInstruction(final Instruction instruction) throws InstructionException {
		if (instruction instanceof GeneratedInstruction) {
			parseGeneratedInstruction((GeneratedInstruction) instruction);
		}
	}

	/**
	 * Parse a generated instruction from a raw text and returns a structured list of keywords<br />
	 * raw text format: <br />
	 * - keyword1(param1,param2,...),keyword2(param1,param2,...)<br/>
	 * - keyword1(keyword2(param1),param2,...),keyword3(param1,param2,...)<br/>
	 * 
	 * a keyword is composed of a name and a list of keyword-values separated by a comma (it must have at least one keyword-value)<br />
	 * see {@link KeywordService#getKeywordValues(String)} for keyword-value composition.<br />
	 * 
	 * @param text
	 * @return list of keywords
	 * @throws InstructionException
	 */
	public void parseGeneratedInstruction(final GeneratedInstruction instruction) throws InstructionException {
		logger.debug(instruction.getRawText());
		List<RawKeywordParam> rawKeywordParams = getRawKeywordParamList(instruction.getRawText());
		for (RawKeywordParam rawKeywordParam : rawKeywordParams) {
			parseGeneratedInstruction(instruction, rawKeywordParam);
		}
	}

	public void parseGeneratedInstruction(final GeneratedInstruction instruction, RawKeywordParam rawKeywordParam) throws InstructionException {
		Keyword keyword = keywordRepository.findByName(rawKeywordParam.getKeyword().toUpperCase());
		if (keyword != null) {
			parseKeywordInstruction(instruction, rawKeywordParam, keyword);
			return;
		}
		Action action = actionRepository.findByName(rawKeywordParam.getKeyword());
		if (action != null) {
			parseActionInstruction(instruction, rawKeywordParam, action);
			return;
		}
		Trigger trigger = triggerRepository.findByName(rawKeywordParam.getKeyword());
		if (trigger != null) {
			parseTriggerInstruction(instruction, rawKeywordParam, trigger);
			return;
		}
		throw new InstructionException(InstructionException.UNKNOWN_KEYWORD_ERROR_MESSAGE + ": " + rawKeywordParam.getKeyword());
	}

	public void parseKeywordInstruction(final GeneratedInstruction instruction, final RawKeywordParam rawKeywordParam, Keyword keyword)
			throws InstructionException {
		logger.debug(rawKeywordParam.toString());
		if (keyword.getType() == KeywordTypeEnum.Action) {
			ActionKeyword action = keyword.getActionKeywords().iterator().next();
			Action a = action.getAction();
		}
	}

	public void parseActionInstruction(final GeneratedInstruction instruction, final RawKeywordParam rawKeywordParam, Action action)
			throws InstructionException {
		logger.debug(rawKeywordParam.toString());
	}

	public void parseTriggerInstruction(final GeneratedInstruction instruction, final RawKeywordParam rawKeywordParam, Trigger trigger)
			throws InstructionException {
		logger.debug(rawKeywordParam.toString());
	}

	public List<RawKeywordParam> getRawKeywordParamList(String rawText) throws InstructionException {
		List<RawKeywordParam> keywordParams = new ArrayList<>();
		if (StringUtils.isEmpty(rawText)) {
			return keywordParams;
		}
		InstructionStringReader reader = new InstructionStringReader(rawText);
		RawKeywordParam keyword;
		while ((keyword = reader.getKeyword()) != null) {
			keyword.getParams().addAll(getRawKeywordParamList(keyword.getParam()));
			keywordParams.add(keyword);
		}
		return keywordParams;
	}

}
