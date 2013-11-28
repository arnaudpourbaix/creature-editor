package com.pourbaix.creature.script.generator;

import java.util.ArrayList;
import java.util.List;

import com.pourbaix.creature.editor.domain.Trigger;

/**
 * A block is a simple IF-THEN-END instruction.
 * 
 * @author apourbaix
 * 
 */
public class Block {

	private final List<Trigger> triggers = new ArrayList<>();

	public List<Trigger> getTriggers() {
		return triggers;
	}

}
