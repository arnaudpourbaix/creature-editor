package old;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Param {
	private String rawName;
	private String rawValue;
	
	public Param(String str) {
		Pattern p = Pattern.compile("([^<]+)([<]([^>]*)[>])?");
		Matcher m = p.matcher(str);
		if (m.find()) {
			if (m.group(2) != null) {
				rawName = m.group(1);
				rawValue = m.group(3);
			} else {
				rawValue = m.group(1);
			}
		} else {
			rawName = "";
			rawValue = "";
		}
	}

	public Param(String str, char separator) {
		String[] t = str.split("[" + separator + "]");
		if (t.length > 1) {
			rawName = t[0];
			rawValue = t[1];
			
		} else {
			rawValue = t[0];
		}
	}
	
	public String getName() {
		return rawName == null ? null : rawName.toUpperCase();
	}
	
	public String getValue() {
		return rawValue == null ? null : rawValue.toUpperCase();
	}

	public String getRawName() {
		return rawName;
	}

	public void setRawName(String rawName) {
		this.rawName = rawName;
	}

	public String getRawValue() {
		return rawValue;
	}

	public void setRawValue(String rawValue) {
		this.rawValue = rawValue;
	}
	
}
