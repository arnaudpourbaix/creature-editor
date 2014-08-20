package com.pourbaix.test.infinity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.creature.editor.spring.CoreConfig;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.service.GameService;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfig.class)
public class ReaderServiceTest {

	@Resource
	ReaderService readerService;

	@Resource
	GameService gameService;

	@Before
	public void before() throws ServiceException {
		gameService.openGame();
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void getSpells() {
		try {
			for (ResourceEntry entry : Keyfile.getInstance().getResourceEntriesByExtension("spl")) {
				//System.out.println("=> " + entry.getResourceName());
				Spell spell = readerService.getSpell(entry);
			}
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellSpin771() {
		try {
			Spell spell = readerService.getSpell("spin771.spl");
			assertEquals(1, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellFireBall() {
		try {
			Spell spell = readerService.getSpell("spwi304.spl");
			assertEquals(3, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellHoldMonster() {
		try {
			Spell spell = readerService.getSpell("spwi507.spl");
			assertEquals(5, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellSlow() {
		try {
			Spell spell = readerService.getSpell("spwi312.spl");
			assertEquals(3, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellDireCharm() {
		try {
			Spell spell = readerService.getSpell("spwi316.spl");
			assertEquals(3, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellImprovedInvisibility() {
		try {
			Spell spell = readerService.getSpell("spwi405.spl");
			assertEquals(4, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellConeOfCold() {
		try {
			Spell spell = readerService.getSpell("spwi503.spl");
			assertEquals(5, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellMagicMissile() {
		try {
			Spell spell = readerService.getSpell("spwi112.spl");
			assertEquals(1, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellDrawUponHolyMight() {
		try {
			Spell spell = readerService.getSpell("SPPR214.spl");
			assertEquals(2, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellBerserkerRage() {
		try {
			Spell spell = readerService.getSpell("SPCL321.spl");
			assertEquals(1, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellBPDISPEL() {
		try {
			Spell spell = readerService.getSpell("BPDISPEL.SPL");
			assertEquals(3, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spellSelection() {
		try {
			readerService.getSpell("SPIN869.SPL");
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void creatureKahrk() {
		try {
			readerService.getCreature("KAHRK.CRE");
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

}
