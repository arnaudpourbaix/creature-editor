package com.pourbaix.creature.script.instruction;

import java.util.LinkedList;
import java.util.List;

import com.pourbaix.creature.editor.domain.Action;

public class Response {

	private final int weight;
	private List<Action> actions = new LinkedList<>();

	public Response(int weight) {
		this.weight = weight;
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public int getWeight() {
		return weight;
	}

	public List<Action> getActions() {
		return actions;
	}

}
