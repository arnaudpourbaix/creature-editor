package com.pourbaix.test.creature.script.instruction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pourbaix.creature.script.config.RootConfig;
import com.pourbaix.creature.script.instruction.GeneratedInstruction;
import com.pourbaix.creature.script.instruction.InstructionException;
import com.pourbaix.creature.script.instruction.InstructionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
public class ParseGeneratedInstructionTest {

	@InjectMocks
	@Autowired
	InstructionService instructionService;

	@Test
	public void spellConfusion() throws InstructionException {
		GeneratedInstruction instruction = new GeneratedInstruction("spell(confusion)");
		instructionService.parseGeneratedInstruction(instruction);
	}

}
