package com.pourbaix.creature.script.instruction;

import com.pourbaix.creature.script.utils.Constant;

public class GeneratedInstruction extends Instruction {

	public GeneratedInstruction(InstructionTypeEnum type) {
		super(type);
	}

	@Override
	public void setRawText(String rawText) {
		// strip comments, end-line separators, and semi-colon
		String strippedText = rawText.replaceAll("/\\*.*\\*/", "").replaceAll(Constant.CARRIAGE_RETURN, "").replaceAll(";", "");
		this.rawText = strippedText;
	}

}
