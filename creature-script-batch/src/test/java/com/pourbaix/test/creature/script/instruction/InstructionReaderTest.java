package com.pourbaix.test.creature.script.instruction;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.pourbaix.creature.script.instruction.InstructionException;
import com.pourbaix.creature.script.instruction.InstructionReader;
import com.pourbaix.creature.script.instruction.RawKeywordParam;

public class InstructionReaderTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void noKeyword() throws InstructionException {
		InstructionReader reader = new InstructionReader("");
		assertTrue("empty string cant result to a successful read",
				!reader.hasNext());
	}

	@Test
	public void wrongKeyword() throws InstructionException {
		InstructionReader reader = new InstructionReader("[moveToObject");
		assertTrue("wrong keyword", !reader.hasNext());
	}

	@Test
	public void singleKeyword() throws InstructionException {
		InstructionReader reader = new InstructionReader("nothing");
		assertTrue(reader.hasNext());
		RawKeywordParam keywordParam = reader.next();
		assertEquals("nothing", keywordParam.getKeyword());
		assertEquals(true, keywordParam.isResult());
		assertEquals(null, keywordParam.getParam());
	}

	@Test
	public void singleKeywordEmptyParam() throws InstructionException {
		InstructionReader reader = new InstructionReader("sleep()");
		assertTrue(reader.hasNext());
		RawKeywordParam keywordParam = reader.next();
		assertEquals("sleep", keywordParam.getKeyword());
		assertEquals(true, keywordParam.isResult());
		assertEquals(null, keywordParam.getParam());
	}

	@Test
	public void singleKeywordNegativeResult() throws InstructionException {
		InstructionReader reader = new InstructionReader("!rest()");
		assertTrue(reader.hasNext());
		RawKeywordParam keywordParam = reader.next();
		assertEquals("rest", keywordParam.getKeyword());
		assertEquals(false, keywordParam.isResult());
		assertEquals(null, keywordParam.getParam());
	}

	@Test
	public void singleKeywordNegativeResultWithSpaces()
			throws InstructionException {
		InstructionReader reader = new InstructionReader("! spell	(  fire  )");
		assertTrue(reader.hasNext());
		RawKeywordParam keywordParam = reader.next();
		assertEquals("spell", keywordParam.getKeyword());
		assertEquals(false, keywordParam.isResult());
		assertEquals("fire", keywordParam.getParam());
	}

	@Test
	public void singleKeywordNestedParams() throws InstructionException {
		InstructionReader reader = new InstructionReader(
				"spell(confusion,!state(confused),random(25))");
		assertTrue(reader.hasNext());
		RawKeywordParam keywordParam = reader.next();
		assertEquals("spell", keywordParam.getKeyword());
		assertEquals(true, keywordParam.isResult());
		assertEquals("confusion,!state(confused),random(25)",
				keywordParam.getParam());
	}

	@Test
	public void singleKeywordWrongNestedParams() throws InstructionException {
		exception.expect(InstructionException.class);
		exception
				.expectMessage(containsString(InstructionException.MATCHING_BRACKET_ERROR_MESSAGE));
		new InstructionReader(
				"forcespell(confusion,!state(confused),random(25)");
	}

	@Test
	public void multipleKeyword() throws InstructionException {
		InstructionReader reader = new InstructionReader(
				"nothing  , !to(   ),do(sleep())");
		assertTrue(reader.hasNext());
		RawKeywordParam keywordParam1 = reader.next();
		assertEquals("nothing", keywordParam1.getKeyword());
		assertEquals(true, keywordParam1.isResult());
		assertEquals(null, keywordParam1.getParam());

		assertTrue(reader.hasNext());
		RawKeywordParam keywordParam2 = reader.next();
		assertEquals("to", keywordParam2.getKeyword());
		assertEquals(false, keywordParam2.isResult());
		assertEquals(null, keywordParam2.getParam());

		assertTrue(reader.hasNext());
		RawKeywordParam keywordParam3 = reader.next();
		assertEquals("do", keywordParam3.getKeyword());
		assertEquals(true, keywordParam3.isResult());
		assertEquals("sleep()", keywordParam3.getParam());
	}

	@Test
	public void multipleKeywordBadSeparator() throws InstructionException {
		exception.expect(InstructionException.class);
		exception.expectMessage(containsString(InstructionException.BAD_SEPARATOR_ERROR_MESSAGE));
		new InstructionReader("nothing  ; !to(   ),do(sleep())");
	}
	
}
