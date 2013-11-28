package com.pourbaix.script.creature.generator;

import java.util.LinkedList;
import java.util.List;

import com.pourbaix.script.creature.keyword.Keyword;
import com.pourbaix.script.creature.trigger.Trigger;

public class Block {

	private List<Keyword> keywords;
	private final List<Trigger> triggers = new LinkedList<Trigger>();
	private final List<Response> responses = new LinkedList<Response>();

	public Response getCurrentResponse() {
		if (responses.isEmpty()) {
			responses.add(new Response(100));
		}
		return ((LinkedList<Response>) responses).getLast();
	}

	public Response addResponse(final int weight) {
		responses.add(new Response(weight));
		return ((LinkedList<Response>) responses).getLast();
	}

	public void addTriggers(final List<Trigger> triggers) {
		this.triggers.addAll(triggers);
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}

	public List<Response> getResponses() {
		return responses;
	}

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(final List<Keyword> keywords) {
		this.keywords = keywords;
	}

}
