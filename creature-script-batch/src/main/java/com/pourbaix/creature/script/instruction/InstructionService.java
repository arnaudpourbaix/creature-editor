package com.pourbaix.creature.script.instruction;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InstructionService {

	private static final Logger logger = LoggerFactory.getLogger(InstructionService.class);

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
		List<RawKeywordParam> keywordParams = getRowKeywordParamList(instruction.getRawText());
	}

	public List<RawKeywordParam> getRowKeywordParamList(String rawText) throws InstructionException {
		List<RawKeywordParam> keywordParams = new ArrayList<>();
		if (StringUtils.isEmpty(rawText)) {
			return keywordParams;
		}
		InstructionStringReader reader = new InstructionStringReader(rawText);
		RawKeywordParam keyword;
		while ((keyword = reader.getKeyword()) != null) {
			keyword.getParams().addAll(getRowKeywordParamList(keyword.getParam()));
			keywordParams.add(keyword);
		}
		return keywordParams;
	}

}
