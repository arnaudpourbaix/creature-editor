package com.pourbaix.creature.script.instruction;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.pourbaix.creature.editor.domain.Action;
import com.pourbaix.creature.editor.domain.Keyword;
import com.pourbaix.creature.editor.domain.Trigger;
import com.pourbaix.creature.editor.repository.ActionRepository;
import com.pourbaix.creature.editor.repository.KeywordRepository;
import com.pourbaix.creature.editor.repository.TriggerRepository;

@Service
public class InstructionService {

	private static final Logger logger = LoggerFactory.getLogger(InstructionService.class);

	@Autowired
	private KeywordRepository keywordRepository;
	@Autowired
	private TriggerRepository triggerRepository;
	@Autowired
	private ActionRepository actionRepository;

	public void parseInstruction(Instruction instruction) throws InstructionException {
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
	public void parseGeneratedInstruction(GeneratedInstruction instruction) throws InstructionException {
		logger.debug(instruction.getRawText());
		List<RawKeywordParam> rawKeywordParams = getRawKeywordParamList(instruction.getRawText());
		List<KeywordParam> keywordParams = getKeywordParamList(rawKeywordParams);
		for (RawKeywordParam rawKeywordParam : rawKeywordParams) {
			KeywordParam keywordParam = getKeywordParam(rawKeywordParam);
			keywordParams.add(keywordParam);
		}
	}

	public KeywordParam getKeywordParam(RawKeywordParam rawKeywordParam) throws InstructionException {
		Keyword keyword = keywordRepository.findByName(rawKeywordParam.getKeyword().toUpperCase());
		if (keyword != null) {
			// TODO
			return new KeywordParam();
		}
		Action action = actionRepository.findByName(rawKeywordParam.getKeyword());
		if (action != null) {
			// TODO
			return new KeywordParam();
		}
		Trigger trigger = triggerRepository.findByName(rawKeywordParam.getKeyword());
		if (trigger != null) {
			// TODO
			return new KeywordParam();
		}
		throw new InstructionException(InstructionException.UNKNOWN_KEYWORD_ERROR_MESSAGE + ": " + rawKeywordParam.getKeyword());
	}

	public List<KeywordParam> getKeywordParamList(List<RawKeywordParam> rawKeywordParams) throws InstructionException {
		List<KeywordParam> keywordParams = new ArrayList<>();
		return keywordParams;
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
