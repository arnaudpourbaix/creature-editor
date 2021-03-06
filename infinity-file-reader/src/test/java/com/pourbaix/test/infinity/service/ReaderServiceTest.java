package com.pourbaix.test.infinity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pourbaix.infinity.domain.IdentifierEntry;
import com.pourbaix.infinity.domain.RawSpell;
import com.pourbaix.infinity.factory.FactoryException;
import com.pourbaix.infinity.factory.IdentifierFactory;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.resource.spl.SplResource;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;
import com.pourbaix.infinity.spring.RootConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
public class ReaderServiceTest {

	@InjectMocks
	@Autowired
	ReaderService readerService;

	@Autowired
	IdentifierFactory identifierFactory;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// @Test
	public void idsFile() {
		try {
			readerService.getIdentifierFiles();
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void allSpells() {
		try {
			readerService.getSpells();
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spwi112Identifier() {
		try {
			IdentifierEntry entry = identifierFactory.getSpellIdentifierByResource("spwi112");
			assertEquals("WIZARD_MAGIC_MISSILE", entry.getFirstValue());
		} catch (FactoryException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void resourceByIdentifier() {
		try {
			String resource = identifierFactory.getResourceNameByIdentifier("WIZARD_MAGIC_MISSILE");
			assertEquals("SPWI112", resource);
		} catch (FactoryException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellFireBall() {
		try {
			RawSpell spell = readerService.getSpell("spwi304.spl");
			assertEquals(3, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellSlow() {
		try {
			RawSpell spell = readerService.getSpell("spwi312.spl");
			assertEquals(3, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellDireCharm() {
		try {
			RawSpell spell = readerService.getSpell("spwi316.spl");
			assertEquals(3, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellImprovedInvisibility() {
		try {
			RawSpell spell = readerService.getSpell("spwi405.spl");
			assertEquals(4, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void splSlow() {
		try {
			ResourceEntry entry = Keyfile.getInstance().getResourceEntry("SPWI312.SPL");
			SplResource spell = new SplResource(entry);
			spell.getList();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellConeOfCold() {
		try {
			RawSpell spell = readerService.getSpell("spwi503.spl");
			assertEquals(5, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellMagicMissile() {
		try {
			RawSpell spell = readerService.getSpell("spwi112.spl");
			assertEquals(1, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellDrawUponHolyMight() {
		try {
			RawSpell spell = readerService.getSpell("SPPR214.spl");
			assertEquals(1, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellBerserkerRage() {
		try {
			RawSpell spell = readerService.getSpell("SPCL321.spl");
			assertEquals(1, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellTarss() {
		try {
			RawSpell spell = readerService.getSpell("1TARSS.SPL");
			assertEquals(4, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
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
