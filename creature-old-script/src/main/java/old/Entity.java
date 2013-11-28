package old;
import java.util.ArrayList;
import java.util.List;


public class Entity {
	public String input;
	public String name;
	public List<Param> params = new ArrayList<Param>();
	
	public Entity() {

	}

	public Entity(String name) {
		this.name = name;
	}
	
	public String getFirstParamValue() {
		return params.size() > 0 ? params.get(0).getValue() : null;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Param> getParams() {
		return params;
	}
	
	public void setParams(List<Param> params) {
		this.params = params;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

}
