package com.pourbaix.test.creature.script.instruction;

import static org.hamcrest.CoreMatchers.containsString;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pourbaix.creature.editor.spring.CoreConfig;
import com.pourbaix.creature.script.instruction.GeneratedInstruction;
import com.pourbaix.creature.script.instruction.InstructionException;
import com.pourbaix.creature.script.instruction.InstructionService;
import com.pourbaix.creature.script.spring.ScriptConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CoreConfig.class, ScriptConfig.class })
public class ParseGeneratedInstructionTest {

	@InjectMocks
	@Autowired
	InstructionService instructionService;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void mispelledKeyword() throws InstructionException {
		exception.expect(InstructionException.class);
		exception.expectMessage(containsString(InstructionException.UNKNOWN_KEYWORD_ERROR_MESSAGE));
		GeneratedInstruction instruction = new GeneratedInstruction("spel(confusion)");
		instructionService.parseGeneratedInstruction(instruction);
	}

	@Test
	public void spellConfusion() throws InstructionException {
		GeneratedInstruction instruction = new GeneratedInstruction("spell(confusion)");
		instructionService.parseGeneratedInstruction(instruction);
	}

}
