package com.pourbaix.script.creature.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import com.pourbaix.script.creature.generator.GeneratorException;
import com.pourbaix.script.creature.spell.Spell;
import com.pourbaix.script.creature.trigger.Trigger;

public class GlobalContext {

	private boolean loaded = false;
	@Resource
	private TargetContext target;
	@Resource
	private ProbablityContext probability;
	@Resource
	private CheckContext checkContext;
	@Value("${global.tobEx.active}")
	private boolean tobEx;
	private final List<Spell> spells = new ArrayList<Spell>();
	private final Map<String, List<Trigger>> triggers = new HashMap<String, List<Trigger>>();

	public void addSpell(final Spell spell) {
		this.spells.add(spell);
	}

	public void addTrigger(final String shortcut, final Trigger trigger) {
		if (!this.triggers.containsKey(shortcut)) {
			this.triggers.put(shortcut, new ArrayList<Trigger>());
		}
		this.triggers.get(shortcut).add(trigger);
	}

	public Trigger getTrigger(final String shortcut) throws GeneratorException {
		if (!this.triggers.containsKey(shortcut.toUpperCase())) {
			throw new GeneratorException("unknown shortcut: " + shortcut);
		}
		List<Trigger> list = new ArrayList<Trigger>(this.triggers.get(shortcut.toUpperCase()));
		if (list.size() == 1) {
			return list.get(0);
		} else {
			Trigger trigger = new Trigger();
			trigger.setShortcut(list.get(0).getShortcut());
			trigger.addTriggers(new LinkedList<Trigger>(list));
			return trigger;
		}
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public List<Spell> getSpells() {
		return spells;
	}

	public TargetContext getTarget() {
		return target;
	}

	public void setTarget(TargetContext target) {
		this.target = target;
	}

	public ProbablityContext getProbability() {
		return probability;
	}

	public void setProbability(ProbablityContext probability) {
		this.probability = probability;
	}

	public CheckContext getCheckContext() {
		return checkContext;
	}

	public void setCheckContext(CheckContext checkContext) {
		this.checkContext = checkContext;
	}

}
