package old;
import java.util.ArrayList;
import java.util.List;

public class Response implements Cloneable {
	
	private int num;
	private List<String> lines = new ArrayList<String>();
	
	public Response() {
		
	}

	public Response(int num) {
		this(num, null);
	}
	
	public Response(int num, String line) {
		this.num = num;
		if (line != null)
			this.lines.add(line);
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public void addLine(ActionEnum action, String ... params) {
		addLine(action, false, params);
	}
	
	public void addLine(ActionEnum action, boolean insertTop, String ... params) {
		String line = "";
		if (action == ActionEnum.SetGlobalTimer || action == ActionEnum.SetGlobal || action == ActionEnum.IncrementGlobal)
			line = Tools.generateString(action.toString(), Tools.encloseString(params[0]), Tools.encloseString("LOCALS"), params[1]);
		else if (action == ActionEnum.ApplySpellRES)
			line = Tools.generateString(action.toString(), Tools.encloseString(params[0]), params[1]);
		else {
			line = Tools.generateString(action.toString(), params);
		}
		if (insertTop) {
			addTopLine(line);
		} else {
			addLine(line);
		}
	}
	
	public void addLine(String line) {
		this.lines.add(line);
	}

	public void addTopLine(String line) {
		this.lines.add(0, line);
	}
	
	public Response clone() {
		Response response = new Response();
		response.setNum(num);
		List<String> lines = new ArrayList<String>();
		for (String l : this.lines) {
			lines.add(l);
		}
		response.setLines(lines);
		return response;
	}
	
}
