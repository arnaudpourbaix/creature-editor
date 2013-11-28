package old;
import java.util.ArrayList;
import java.util.List;

import com.pourbaix.script.creature.generator.GeneratorException;


public class Target extends Entity {
	private TargetEnum code;
	private String input;
	private String type;
	private int count;
	private boolean random;
	private List<Condition> conditions = new ArrayList<Condition>();
	private String[] classes;
	
	public Target(String input, String type, int count, boolean random, String name) throws GeneratorException {
		this.input = input;
		this.type = type;
		this.count = count;
		this.random = random;
		this.name = name;
		this.code = TargetEnum.fromString(name);
		if (this.code == TargetEnum.LASTATTACKER || this.code == TargetEnum.LASTSEEN || this.code == TargetEnum.PLAYERS || this.code == TargetEnum.LASTSUMMONER
				|| this.code == TargetEnum.NEARESTENEMY || this.code == TargetEnum.PLAYER1 || this.code == TargetEnum.PLAYER2
				|| this.code == TargetEnum.PLAYER3 || this.code == TargetEnum.PLAYER4 || this.code == TargetEnum.PLAYER5
				|| this.code == TargetEnum.PLAYER6)
			this.count = 0;
		if (this.code == TargetEnum.PLAYERS) {
			this.random = false;
		}
	}
	
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public boolean isRandom() {
		return random;
	}
	public void setRandom(boolean random) {
		this.random = random;
	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	public TargetEnum getCode() {
		return code;
	}
	public void setCode(TargetEnum code) {
		this.code = code;
	}
	public String[] getClasses() {
		return classes;
	}
	public void setClasses(String[] classes) {
		this.classes = classes;
	}
	
}
