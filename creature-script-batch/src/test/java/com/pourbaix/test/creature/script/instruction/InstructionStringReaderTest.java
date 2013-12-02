package com.pourbaix.test.creature.script.instruction;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.pourbaix.creature.script.instruction.InstructionException;
import com.pourbaix.creature.script.instruction.InstructionService;
import com.pourbaix.creature.script.instruction.RawKeywordParam;

public class InstructionStringReaderTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private final InstructionService instructionService = new InstructionService();

	@Test
	public void noKeyword() throws InstructionException {
		List<RawKeywordParam> keywords = instructionService.getRawKeywordParamList("");
		assertTrue("empty string cant result to a successful read", keywords.isEmpty());
	}

	@Test
	public void wrongKeyword() throws InstructionException {
		List<RawKeywordParam> keywords = instructionService.getRawKeywordParamList("[moveToObject");
		assertTrue("wrong keyword", keywords.isEmpty());
	}

	@Test
	public void singleKeyword() throws InstructionException {
		List<RawKeywordParam> keywords = instructionService.getRawKeywordParamList("nothing");
		assertTrue(!keywords.isEmpty());
		RawKeywordParam keywordParam = keywords.get(0);
		assertEquals("nothing", keywordParam.getKeyword());
		assertEquals(true, keywordParam.isResult());
		assertEquals(null, keywordParam.getParam());
	}

	@Test
	public void singleKeywordEmptyParam() throws InstructionException {
		List<RawKeywordParam> keywords = instructionService.getRawKeywordParamList("sleep()");
		assertTrue(!keywords.isEmpty());
		RawKeywordParam keywordParam = keywords.get(0);
		assertEquals("sleep", keywordParam.getKeyword());
		assertEquals(true, keywordParam.isResult());
		assertEquals(null, keywordParam.getParam());
	}

	@Test
	public void singleKeywordNegativeResult() throws InstructionException {
		List<RawKeywordParam> keywords = instructionService.getRawKeywordParamList("!rest()");
		assertTrue(!keywords.isEmpty());
		RawKeywordParam keywordParam = keywords.get(0);
		assertEquals("rest", keywordParam.getKeyword());
		assertEquals(false, keywordParam.isResult());
		assertEquals(null, keywordParam.getParam());
	}

	@Test
	public void singleKeywordNegativeResultWithSpaces() throws InstructionException {
		List<RawKeywordParam> keywords = instructionService.getRawKeywordParamList("! spell	(  fire  )");
		assertTrue(!keywords.isEmpty());
		RawKeywordParam keywordParam = keywords.get(0);
		assertEquals("spell", keywordParam.getKeyword());
		assertEquals(false, keywordParam.isResult());
		assertEquals("fire", keywordParam.getParam());
	}

	@Test
	public void singleKeywordNestedParams() throws InstructionException {
		List<RawKeywordParam> keywords = instructionService.getRawKeywordParamList("spell(confusion,!state(confused),random(25))");
		assertTrue(!keywords.isEmpty());
		RawKeywordParam keywordParam = keywords.get(0);
		assertEquals("spell", keywordParam.getKeyword());
		assertEquals(true, keywordParam.isResult());
		assertEquals("confusion,!state(confused),random(25)", keywordParam.getParam());
	}

	@Test
	public void singleKeywordWrongNestedParams() throws InstructionException {
		exception.expect(InstructionException.class);
		exception.expectMessage(containsString(InstructionException.INVALID_INSTRUCTION_ERROR_MESSAGE));
		instructionService.getRawKeywordParamList("forcespell(confusion,!state(confused),random(25)");
	}

	@Test
	public void multipleKeyword() throws InstructionException {
		List<RawKeywordParam> keywords = instructionService.getRawKeywordParamList("nothing  , !to(   ),do(sleep())");
		assertEquals(3, keywords.size());

		RawKeywordParam keywordParam1 = keywords.get(0);
		assertEquals("nothing", keywordParam1.getKeyword());
		assertEquals(true, keywordParam1.isResult());
		assertEquals(null, keywordParam1.getParam());

		RawKeywordParam keywordParam2 = keywords.get(1);
		assertEquals("to", keywordParam2.getKeyword());
		assertEquals(false, keywordParam2.isResult());
		assertEquals(null, keywordParam2.getParam());

		RawKeywordParam keywordParam3 = keywords.get(2);
		assertEquals("do", keywordParam3.getKeyword());
		assertEquals(true, keywordParam3.isResult());
		assertEquals("sleep()", keywordParam3.getParam());
	}

	@Test
	public void multipleKeywordBadSeparator() throws InstructionException {
		exception.expect(InstructionException.class);
		exception.expectMessage(containsString(InstructionException.INVALID_INSTRUCTION_ERROR_MESSAGE));
		instructionService.getRawKeywordParamList("nothing  ; !to(   ),do(sleep())");
	}

	@Test
	public void singleKeywordMultipleParamsWithSingleParam() throws InstructionException {
		List<RawKeywordParam> keywords = instructionService.getRawKeywordParamList("config(CLASS=WIZARD,CASTER_LEVEL=12,MAX_SPELL_LEVEL=6)");
		assertEquals(1, keywords.size());
		RawKeywordParam keywordParam = keywords.get(0);
		assertEquals("config", keywordParam.getKeyword());
		assertEquals(3, keywordParam.getParams().size());
		RawKeywordParam param1 = keywordParam.getParams().get(0);
		assertEquals("CLASS", param1.getKeyword());
		assertEquals("WIZARD", param1.getParam());
		RawKeywordParam param2 = keywordParam.getParams().get(1);
		assertEquals("CASTER_LEVEL", param2.getKeyword());
		assertEquals("12", param2.getParam());
		RawKeywordParam param3 = keywordParam.getParams().get(2);
		assertEquals("MAX_SPELL_LEVEL", param3.getKeyword());
		assertEquals("6", param3.getParam());
	}

	@Test
	public void singleKeywordMultipleParamsWithSingleParamWrongBracket() throws InstructionException {
		exception.expect(InstructionException.class);
		exception.expectMessage(containsString(InstructionException.INVALID_INSTRUCTION_ERROR_MESSAGE));
		instructionService.getRawKeywordParamList("config(CLASS=WIZARD,CASTER_LEVEL=12,RESISTANCES(fire,cold),MAX_SPELL_LEVEL=6))");
	}

}
