package com.pourbaix.creature.script.instruction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
		InstructionReader reader = new InstructionReader(instruction.getRawText());

	}

}
