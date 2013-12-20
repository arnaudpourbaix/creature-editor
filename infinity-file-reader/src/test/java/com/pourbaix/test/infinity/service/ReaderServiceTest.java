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

import com.pourbaix.infinity.cache.CacheException;
import com.pourbaix.infinity.config.RootConfig;
import com.pourbaix.infinity.entity.IdentifierEntry;
import com.pourbaix.infinity.entity.Spell;
import com.pourbaix.infinity.factory.IdentifierFactory;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
public class ReaderServiceTest {

	@InjectMocks
	@Autowired
	ReaderService readerService;

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
	public void spwi112Identifier() {
		try {
			IdentifierEntry entry = IdentifierFactory.getSpellIdentifierByResource("spwi112");
			assertEquals("WIZARD_MAGIC_MISSILE", entry.getFirstValue());
		} catch (FactoryException | CacheException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void resourceByIdentifier() {
		try {
			String resource = IdentifierFactory.getResourceNameByIdentifier("WIZARD_MAGIC_MISSILE");
			assertEquals("SPWI112", resource);
		} catch (FactoryException | CacheException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellFireBall() {
		try {
			Spell spell = readerService.getSpell("spwi304.spl");
			assertEquals(3, spell.getLevel());
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

	// @Test
	public void spellConeOfCold() {
		try {
			Spell spell = readerService.getSpell("spwi503.spl");
			assertEquals(5, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellMagicMissile() {
		try {
			Spell spell = readerService.getSpell("spwi112.spl");
			assertEquals(1, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellDrawUponHolyMight() {
		try {
			Spell spell = readerService.getSpell("SPPR214.spl");
			assertEquals(1, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	public void spellBerserkerRage() {
		try {
			Spell spell = readerService.getSpell("SPCL321.spl");
			assertEquals(1, spell.getLevel());
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
	}

}
