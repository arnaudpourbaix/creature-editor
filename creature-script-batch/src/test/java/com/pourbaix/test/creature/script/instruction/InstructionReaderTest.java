package com.pourbaix.test.creature.script.instruction;

import org.junit.Assert;
import org.junit.Test;

import com.pourbaix.creature.script.generator.GeneratorException;
import com.pourbaix.creature.script.instruction.InstructionReader;

public class InstructionReaderTest {

	@Test
	public void noKeyword() {
		InstructionReader reader = new InstructionReader("");
		try {
			Assert.assertFalse("empty string cant result to a successful read", reader.next());
		} catch (GeneratorException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void wrongKeyword() {
		InstructionReader reader = new InstructionReader("[moveToObject");
		try {
			Assert.assertFalse("should be a wrong keyword", reader.next());
		} catch (GeneratorException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void simpleKeyword() {
		InstructionReader reader = new InstructionReader("nothing");
		try {
			Assert.assertTrue(reader.next());
			Assert.assertEquals(reader.getKeyword(), "nothing");
			Assert.assertEquals(reader.isResult(), true);
		} catch (GeneratorException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void simpleKeywordEmptyParam() {
		InstructionReader reader = new InstructionReader("sleep()");
		try {
			Assert.assertTrue(reader.next());
			Assert.assertEquals(reader.getKeyword(), "sleep");
			Assert.assertEquals(reader.isResult(), true);
		} catch (GeneratorException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void simpleKeywordNegativeResult() {
		InstructionReader reader = new InstructionReader("!rest()");
		try {
			Assert.assertTrue(reader.next());
			Assert.assertEquals(reader.getKeyword(), "rest");
			Assert.assertEquals(reader.isResult(), false);
		} catch (GeneratorException e) {
			Assert.fail(e.getMessage());
		}
	}

}
