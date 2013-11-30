package com.pourbaix.creature.script.instruction;

import com.pourbaix.creature.script.utils.Constant;

public abstract class Instruction {

	protected InstructionTypeEnum type;
	protected String rawText;

	@Override
	public String toString() {
		return type.toString() + ":" + Constant.CARRIAGE_RETURN + rawText;
	}

	public Instruction() {
		// empty constructor
	}

	public Instruction(InstructionTypeEnum type) {
		this.type = type;
	}

	public Instruction(String rawText) {
		this.rawText = rawText;
	}

	public InstructionTypeEnum getType() {
		return type;
	}

	public void setType(InstructionTypeEnum type) {
		this.type = type;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

}
