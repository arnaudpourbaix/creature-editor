package com.pourbaix.script.creature.generator;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.pourbaix.script.creature.context.GlobalContext;
import com.pourbaix.script.creature.spell.ClassEnum;
import com.pourbaix.script.creature.spell.DamageTypeEnum;
import com.pourbaix.script.creature.spell.SchoolEnum;
import com.pourbaix.script.creature.spell.Spell;
import com.pourbaix.script.creature.spell.SpellTypeEnum;
import com.pourbaix.script.creature.trigger.ShortcutEnum;
import com.pourbaix.script.creature.trigger.Trigger;
import com.pourbaix.script.creature.trigger.TriggerEnum;
import com.pourbaix.script.creature.utils.Constant;

public class OLDGenerator {

	private GlobalContext context;

	public static String getApplicationDirectory() throws Exception {
		final String mainClass = "Main.class";
		final URL main = OLDGenerator.class.getResource(mainClass);
		// return main.getPath().substring(0, main.getPath().length() -
		// mainClass.length()).replace("%20", " ");
		return "C:\\Documents and Settings\\apourbaix\\Mes documents\\Perso\\Enhanced_Creatures\\";
		// return "L:\\Mes documents\\perso\\Enhanced_Creatures\\";
	}

	public void loadContext() throws Exception {
		loadSpells();
		// loadItems();
		loadGlobals();
		loadTriggers();
	}

	public void loadSpells() throws Exception {
		final InputStream input = new FileInputStream(getApplicationDirectory() + Constant.XLS_CONFIG_FILE);
		final Workbook wb = WorkbookFactory.create(input);
		final Sheet sheet = wb.getSheet("spells");
		final Iterator<Row> rows = sheet.rowIterator();
		if (rows.hasNext()) { // skip title lines
			rows.next();
		}
		while (rows.hasNext()) {
			final Spell spell = new Spell();
			final Row row = rows.next();
			final Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				final Cell cell = cells.next();
				final String value = getCellStringValue(cell);
				if (cell.getColumnIndex() == 0) {
					spell.setResource(value);
				} else if (cell.getColumnIndex() == 1) {
					spell.setName(value);
				} else if (cell.getColumnIndex() == 2) {
					spell.setIdentifier(value);
				} else if (cell.getColumnIndex() == 3 && !value.isEmpty()) {
					spell.setLevel(Double.valueOf(value).intValue());
				} else if (cell.getColumnIndex() == 4) {
					spell.setSelfConditions(value);
				} else if (cell.getColumnIndex() == 5) {
					spell.setTargetEnemy(value.equalsIgnoreCase("ENEMY"));
				} else if (cell.getColumnIndex() == 6) {
					spell.setTargetConditions(value);
				} else if (cell.getColumnIndex() == 7 && !value.isEmpty()) {
					spell.setType(SpellTypeEnum.fromString(value));
				} else if (cell.getColumnIndex() == 8 && !value.isEmpty()) {
					spell.setDamageType(DamageTypeEnum.fromString(value));
				} else if (cell.getColumnIndex() == 9 && !value.isEmpty()) {
					spell.setRange(Double.valueOf(value).intValue());
				} else if (cell.getColumnIndex() == 10 && !value.isEmpty()) {
					spell.setRadius(Double.valueOf(value).intValue());
				} else if (cell.getColumnIndex() == 11 && !value.isEmpty()) {
					spell.setCanHurtAllies(Boolean.parseBoolean(value));
				} else if (cell.getColumnIndex() == 12 && !value.isEmpty()) {
					spell.setSchool(SchoolEnum.fromString(value));
				} else if (cell.getColumnIndex() == 13) {
					spell.setDefaultTarget(value);
				} else if (cell.getColumnIndex() == 14 && !value.isEmpty()) {
					spell.setCasterClass(ClassEnum.fromString(value));
				} else if (cell.getColumnIndex() == 15) {
					spell.setDetectableStat(value);
				} else if (cell.getColumnIndex() == 16 && !value.isEmpty()) {
					spell.setDetectableValue(Double.valueOf(value).intValue());
				}
			}
			context.addSpell(spell);
		}
		input.close();
	}

	public void loadGlobals() throws Exception {
		final InputStream input = new FileInputStream(getApplicationDirectory() + Constant.XLS_CONFIG_FILE);
		final Workbook wb = WorkbookFactory.create(input);
		final Sheet sheet = wb.getSheet("globals");
		final Iterator<Row> rows = sheet.rowIterator();
		if (rows.hasNext()) { // skip title lines
			rows.next();
		}
		while (rows.hasNext()) {
			final Row row = rows.next();
			// context.getGlobals().put(getCellStringValue(row.getCell(0)), getCellStringValue(row.getCell(1)));
		}
		input.close();
	}

	public void loadTriggers() throws Exception {
		final InputStream input = new FileInputStream(getApplicationDirectory() + Constant.XLS_CONFIG_FILE);
		final Workbook wb = WorkbookFactory.create(input);
		final Sheet sheet = wb.getSheet("triggers");
		final Iterator<Row> rows = sheet.rowIterator();
		if (rows.hasNext()) { // skip title lines
			rows.next();
		}
		while (rows.hasNext()) {
			final Trigger trigger = new Trigger();
			final Row row = rows.next();
			final Iterator<Cell> cells = row.cellIterator();
			String[] params = new String[4];
			String shortcut = null;
			while (cells.hasNext()) {
				final Cell cell = cells.next();
				final String value = getCellStringValue(cell);
				if (cell.getColumnIndex() == 0) {
					shortcut = value.toUpperCase();
				} else if (cell.getColumnIndex() == 1) {
					trigger.setShortcut(ShortcutEnum.fromString(value));
				} else if (cell.getColumnIndex() == 2 && !value.isEmpty()) {
					trigger.setResult(Boolean.valueOf(value));
				} else if (cell.getColumnIndex() == 3 && !value.isEmpty()) {
					trigger.setType(TriggerEnum.fromString(value));
				} else if (cell.getColumnIndex() == 4 && !value.isEmpty()) {
					params[0] = value;
				} else if (cell.getColumnIndex() == 5 && !value.isEmpty()) {
					params[1] = value;
				} else if (cell.getColumnIndex() == 6 && !value.isEmpty()) {
					params[2] = value;
				}
			}
			if (trigger.getType() != null) {
				int i = 0;
				for (String p : trigger.getType().getParams().split(",")) {
					if (p.equals(Constant.TriggerParam.OBJECT)) {
						continue;
					}
					if (i > params.length - 1) {
						throw new Exception("Parameters error on shortcut " + trigger.getShortcut().toString());
					}
					trigger.getParams().put(p, params[i++]);
				}
			}
			context.addTrigger(shortcut, trigger);
		}
		input.close();
	}

	public String getCellStringValue(final Cell cell) throws Exception {
		String value = "";
		try {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				value = String.valueOf(cell.getNumericCellValue());
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				value = String.valueOf(cell.getStringCellValue());
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				value = "";
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
				value = String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_NUMERIC) {
					value = String.valueOf(cell.getNumericCellValue());
				} else if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_STRING) {
					value = cell.getStringCellValue();
				} else if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_BLANK) {
					value = "";
				} else if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_BOOLEAN) {
					value = String.valueOf(cell.getBooleanCellValue());
				} else {
					throw new Exception("unhandled formulae type: " + cell.getCachedFormulaResultType());
				}
			} else {
				throw new Exception("unhandled type: " + cell.getCellType());
			}
		} catch (final Exception e) {
			throw new Exception(e);
		}
		return value.trim();
	}

}
