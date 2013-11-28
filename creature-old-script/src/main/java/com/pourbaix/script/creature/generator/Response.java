package com.pourbaix.script.creature.generator;
import java.util.LinkedList;
import java.util.List;

import com.pourbaix.script.creature.action.Action;


public class Response implements Cloneable {
	
	private int weight;
	private List<Action> actions = new LinkedList<Action>();
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public Response(int weight) {
		this.weight = weight;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public List<Action> getActions() {
		return actions;
	}
			
}
