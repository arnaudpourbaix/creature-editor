package com.pourbaix.test.creature.script.instruction;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class InstructionReaderTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// @Test
	// public void noKeyword() throws InstructionException {
	// InstructionReader reader = new InstructionReader("");
	// assertFalse("empty string cant result to a successful read", reader.next());
	// }
	//
	// @Test
	// public void wrongKeyword() throws InstructionException {
	// InstructionReader reader = new InstructionReader("[moveToObject");
	// assertFalse("wrong keyword", reader.next());
	// }
	//
	// @Test
	// public void singleKeyword() throws InstructionException {
	// InstructionReader reader = new InstructionReader("nothing");
	// assertTrue(reader.next());
	// assertEquals("nothing", reader.getKeyword());
	// assertEquals(true, reader.isResult());
	// assertEquals(null, reader.getParams());
	// }
	//
	// @Test
	// public void singleKeywordEmptyParam() throws InstructionException {
	// InstructionReader reader = new InstructionReader("sleep()");
	// assertTrue(reader.next());
	// assertEquals("sleep", reader.getKeyword());
	// assertEquals(true, reader.isResult());
	// assertEquals(null, reader.getParams());
	// }
	//
	// @Test
	// public void singleKeywordNegativeResult() throws InstructionException {
	// InstructionReader reader = new InstructionReader("!rest()");
	// assertTrue(reader.next());
	// assertEquals("rest", reader.getKeyword());
	// assertEquals(false, reader.isResult());
	// assertEquals(null, reader.getParams());
	// }
	//
	// @Test
	// public void singleKeywordNegativeResultWithSpaces() throws InstructionException {
	// InstructionReader reader = new InstructionReader("! spell	(  fire  )");
	// assertTrue(reader.next());
	// assertEquals("spell", reader.getKeyword());
	// assertEquals(false, reader.isResult());
	// assertEquals("fire", reader.getParams());
	// }
	//
	// @Test
	// public void singleKeywordNestedParams() throws InstructionException {
	// InstructionReader reader = new InstructionReader("spell(confusion,!state(confused),random(25))");
	// assertTrue(reader.next());
	// assertEquals(reader.getKeyword(), "spell");
	// assertEquals(true, reader.isResult());
	// assertEquals("confusion,!state(confused),random(25)", reader.getParams());
	// }
	//
	// @Test
	// public void singleKeywordWrongNestedParams() throws InstructionException {
	// exception.expect(InstructionException.class);
	// exception.expectMessage(containsString(InstructionException.MATCHING_BRACKET_ERROR_MESSAGE));
	// InstructionReader reader = new InstructionReader("forcespell(confusion,!state(confused),random(25)");
	// reader.next();
	// }
	//
	// @Test
	// public void multipleKeyword() throws InstructionException {
	// InstructionReader reader = new InstructionReader("nothing  , !to(   ),do(sleep())");
	// assertTrue(reader.next());
	// assertEquals("nothing", reader.getKeyword());
	// assertEquals(true, reader.isResult());
	// assertEquals(null, reader.getParams());
	// assertTrue(reader.next());
	// assertEquals("to", reader.getKeyword());
	// assertEquals(false, reader.isResult());
	// assertEquals(null, reader.getParams());
	// assertTrue(reader.next());
	// assertEquals("do", reader.getKeyword());
	// assertEquals(true, reader.isResult());
	// assertEquals("sleep", reader.getParams());
	// }

}
