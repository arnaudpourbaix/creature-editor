package old;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pourbaix.script.creature.generator.GeneratorException;

public class Tools {

	public static boolean equalsIgnoreCase(String s1, String s2) {
		if (s1 == null || s2 == null)
			return false;
		return s1.toLowerCase().equals(s2.toLowerCase());
	}

	public static boolean containsIgnoreCase(String s1, String s2) {
		if (s1 == null || s2 == null)
			return false;
		return s1.toLowerCase().contains(s2.toLowerCase());
	}

	public static String encloseString(String s) {
		return encloseString(s, '\'');
	}

	public static String encloseString(String s, char c) {
		if (c == '[' || c == ']')
			return "[" + s + "]";
		else if (c == '(' || c == ')')
			return "(" + s + ")";
		return c + s + c;
	}

	public static String generateString(String name, String... params) {
		String str = "";
		for (String p : params) {
			if (!str.isEmpty())
				str += ",";
			str += p;
		}
		str = name + "(" + str + ")";
		return str;
	}

	public static Entity getEntity(String str) {
		Entity e = new Entity();
		e.setInput(str);
		Pattern p = Pattern.compile("^([^(]+)([(][^)]*[)])?$");
		Matcher m = p.matcher(str);
		if (m.find()) {
			e.setName(m.group(1));
			if (m.group(2) != null) {
				e.setParams(getParams(m.group(2).substring(1, m.group(2).length() - 1), ','));
			}
		}
		return e;
	}

	public static List<Param> getParams(String str, char separator) {
		List<Param> params = new ArrayList<Param>();
		for (String s : str.split("[" + separator + "]")) {
			if (s.contains(":"))
				params.add(new Param(s, ':'));
			else
				params.add(new Param(s));
		}
		return params;
	}

	public static String getSpecificObject(String allegiance) {
		return encloseString(allegiance, '[');
	}

	public static Item getItem(Context context, String value) throws GeneratorException {
		value = value.trim();
		Item item = null;
		int count = 0;
		for (Item i : context.getItems()) {
			if (Tools.equalsIgnoreCase(i.getName(), value) || Tools.equalsIgnoreCase(i.getResource(), value)) {
				item = i;
				count++;
			}
		}
		if (item == null) {
			throw new GeneratorException("Item not found : " + value);
		} else if (count > 1) {
			throw new GeneratorException("Item found several times : " + value);
		}
		return item;
	}

	public static Spell getSpell(Context context, String value) throws GeneratorException {
		Spell spell = null;
		try {
			spell = Tools.getSpell(context, value, context.getScriptContext().getClasse());
			if (spell == null) {
				spell = Tools.getSpell(context, value, context.getScriptContext().getGlobalClasse());
				if (spell == null) {
					spell = Tools.getSpell(context, value, "");
				}
			}
		} catch (GeneratorException exc) {
			throw new GeneratorException(exc);
		}
		if (spell == null) {
			throw new GeneratorException("Spell not found : " + value);
		}
		return spell;
	}

	public static Spell getSpell(Context context, String value, String classe) throws GeneratorException {
		value = value.trim();
		Spell spell = null;
		int count = 0;
		for (Spell s : context.getSpells()) {
			String type = s.getCasterType() != null ? s.getCasterType() : "";
			if (classe == null || type.equals(classe)) {
				if (Tools.equalsIgnoreCase(s.getName(), value) || Tools.equalsIgnoreCase(s.getIdentifier(), value)
						|| Tools.equalsIgnoreCase(s.getResource(), value)) {
					spell = s;
					count++;
				}
			}
		}
		if (count > 1) {
			throw new GeneratorException("Spell found several times : " + value);
		}
		return spell;
	}

}
