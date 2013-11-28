package old;
import java.util.ArrayList;
import java.util.List;

public class Action implements Cloneable {
	
	private List<Response> responses = new ArrayList<Response>();
	private List<Condition> conditions = new ArrayList<Condition>();

	public void addLine(ActionEnum action, String ... params) {
		String line = "";
		if (action == ActionEnum.SetGlobalTimer || action == ActionEnum.SetGlobal || action == ActionEnum.IncrementGlobal)
			line = Tools.generateString(action.toString(), Tools.encloseString(params[0]), Tools.encloseString("LOCALS"), params[1]);
		else if (action == ActionEnum.ApplySpellRES)
			line = Tools.generateString(action.toString(), Tools.encloseString(params[0]), params[1]);
		else {
			line = Tools.generateString(action.toString(), params);
		}
		addLine(line);
	}
	
	public void addLine(String line) {
		if (responses.isEmpty()) {
			addDefaultResponse();
		}
		this.getResponses().get(this.getResponses().size() - 1).addLine(line);
	}
	
	public void addDefaultResponse() {
		this.addResponse(100);
	}

	public void addResponse(int num) {
		responses.add(new Response(num));
	}
	
	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	
	public void addCondition(Condition condition) {
		this.conditions.add(condition);
	}

	public void addCondition(int index, Condition condition) {
		this.conditions.add(index, condition);
	}

	public List<Response> getResponses() {
		return responses;
	}

	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}
	
	public Action clone() {
		Action action = new Action();
		List<Response> responses = new ArrayList<Response>();
		for (Response response : this.responses) {
			responses.add(response.clone());
		}
		action.setResponses(responses);
		List<Condition> conditions = new ArrayList<Condition>();
		for (Condition condition : this.conditions) {
			conditions.add(condition.clone());
		}
		action.setConditions(conditions);
		return action;
	}
	
}
