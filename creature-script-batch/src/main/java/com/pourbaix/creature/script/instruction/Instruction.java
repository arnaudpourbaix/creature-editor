package com.pourbaix.creature.script.instruction;

import java.util.ArrayList;
import java.util.List;

import com.pourbaix.creature.editor.domain.Trigger;
import com.pourbaix.creature.script.utils.Constant;

public abstract class Instruction {

	protected InstructionTypeEnum type;
	protected String rawText;
	protected List<Trigger> triggers = new ArrayList<>();
	protected List<Response> responses = new ArrayList<>();

	public void addResponse(int weight) {
		responses.add(new Response(100));
	}

	public void addDefaultResponse() {
		addResponse(100);
	}

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
