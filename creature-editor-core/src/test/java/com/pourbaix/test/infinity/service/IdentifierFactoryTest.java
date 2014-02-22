package com.pourbaix.test.infinity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pourbaix.creature.editor.spring.CoreConfig;
import com.pourbaix.infinity.domain.IdentifierEntry;
import com.pourbaix.infinity.factory.FactoryException;
import com.pourbaix.infinity.factory.IdentifierFactory;
import com.pourbaix.infinity.service.GameService;
import com.pourbaix.infinity.service.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfig.class)
public class IdentifierFactoryTest {

	@Autowired
	GameService gameService;

	@Autowired
	IdentifierFactory identifierFactory;

	@Before
	public void before() throws ServiceException {
		gameService.openGame();
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void spwi112Identifier() {
		try {
			IdentifierEntry entry = identifierFactory.getSpellIdentifierByResource("spwi112");
			assertEquals("WIZARD_MAGIC_MISSILE", entry.getFirstValue());
		} catch (FactoryException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void resourceByIdentifier() {
		try {
			String resource = identifierFactory.getResourceNameByIdentifier("WIZARD_MAGIC_MISSILE");
			assertEquals("SPWI112", resource);
		} catch (FactoryException e) {
			fail(e.getMessage());
		}
	}

}
