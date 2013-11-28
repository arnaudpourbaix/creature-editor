package old;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Missing parameter !");
			return;
		}
		try {
			Context context = getContext();
			if (args[0].toUpperCase().equals("-D")) {
				System.out.println("Scanning directory " + args[1]);
				File dir = new File(args[1]);
				FilenameFilter filter = new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.toUpperCase().endsWith(Constant.SCRIPT_EXT);
					}
				};
				String[] chld = dir.list(filter);
				for(String c : chld) {
					String filename = args[1] + "\\" + c;
					String output = args[1] + "\\" + c.substring(0, c.length() - 3) + "baf";
					System.out.println("Processing " + output);
					new Builder(filename, output, context);
				}
			} else {
				String filename = args[0];
				File f = new File(filename);
				if (!f.exists()) {
					System.out.println("File does not exist : " + filename);
					return;
				}
				String output = filename.substring(0, filename.length() - 3) + "baf";
				new Builder(filename, output, context);
			}
		} catch(Exception exc) {
			System.out.println(exc.getMessage());
		}
	}

	public static String getApplicationDirectory() throws Exception {
		String mainClass = "Main.class";
		URL main = Main.class.getResource(mainClass);
		//return main.getPath().substring(0, main.getPath().length() - mainClass.length()).replace("%20", " ");
		return "C:\\Jeux\\BGT\\Enhanced_Creatures\\";
	}
	
	public static Context getContext() throws Exception {
		Context context = new Context();
		loadSpells(context);
		loadItems(context);
		loadConfig(context);
		return context;
	}
	
	public static void loadSpells(Context context) throws Exception {
		try {
			File inputFile = new File(getApplicationDirectory() + "spells.csv");
			FileInputStream fis = new FileInputStream(inputFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			List<Spell> lst = new ArrayList<Spell>();
			while ((line = br.readLine()) != null) {
				line = line.replace("\"", "");
				//System.out.println(line);
				Spell spell = new Spell();
				String[] values = line.split("[;]");
				if ("RESOURCE".equalsIgnoreCase(values[0])) {
					continue;
				}
  				spell.setResource(values[0]);
				spell.setName(values[1]);
				spell.setIdentifier(values[2]);
				spell.setLevel(Integer.parseInt(values[3]));
				if (values.length > 4)
					spell.setSelfConditions(values[4].toUpperCase());
				if (values.length > 5)
					spell.setTarget(values[5].toUpperCase());
				if (values.length > 6)
					spell.setTargetConditions(values[6].toUpperCase());
				if (values.length > 7)
					spell.setType(values[7]);
				if (values.length > 8)
					spell.setDamageType(values[8]);
				if (values.length > 9 && values[9].length() > 0) {
					spell.setRange(Integer.valueOf(values[9]));
				}
				if (values.length > 10 && values[10].length() > 0) {
					spell.setRadius(Integer.valueOf(values[10]));
				}
				if (values.length > 11 && values[11].length() > 0) {
					spell.setCanHurtAllies(Boolean.parseBoolean(values[11]));
				}
				if (values.length > 12 && values[12].length() > 0) {
					spell.setSchool(SchoolEnum.fromString(values[12]));
				}
				if (values.length > 13) {
					spell.setDefaultTarget(values[13]);
				}
				if (values.length > 14) {
					spell.setCasterType(values[14]);
				}
				if (values.length > 15 && values[15].length() > 0) {
					spell.setDetectableStat(values[15]);
				}
				if (values.length > 16 && values[16].length() > 0) {
					spell.setDetectableValue(values[16]);
				}
				lst.add(spell);
			}
			context.setSpells(lst);
			fis.close();
		} catch(Exception exc) {
			System.out.println("Error when reading spells config : " + exc.getMessage());
			throw new Exception(exc);
		}
	}

	public static void loadItems(Context context) throws Exception {
		try {
			File inputFile = new File(getApplicationDirectory() + "items.csv");
			FileInputStream fis = new FileInputStream(inputFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			List<Item> lst = new ArrayList<Item>();
			while ((line = br.readLine()) != null) {
				line = line.replace("\"", "");
				Item item = new Item();
				String[] values = line.split("[;]");
				if ("NAME".equalsIgnoreCase(values[0])) {
					continue;
				}
				item.setName(values[0]);
				item.setResource(values[1]);
				if (values.length > 2 && !values[2].isEmpty())
					item.setOnlyBG2(Boolean.parseBoolean(values[2]));
				if (values.length > 3)
					item.setSelfConditions(values[3].toUpperCase());
				if (values.length > 4)
					item.setTarget(values[4].toUpperCase());
				if (values.length > 5)
					item.setTargetConditions(values[5].toUpperCase());
				if (values.length > 6)
					item.setLevel(Integer.parseInt(values[6]));
				if (values.length > 7)
					item.setType(values[7].toUpperCase());
				if (values.length > 8)
					item.setDamageType(values[8]);
				if (values.length > 9 && values[9].length() > 0) {
					item.setRadius(Integer.valueOf(values[9]));
				}
				if (values.length > 10 && values[10].length() > 0) {
					item.setCanHurtAllies(Boolean.parseBoolean(values[10]));
				}
				lst.add(item);
			}
			context.setItems(lst);
			fis.close();
		} catch(Exception exc) {
			System.out.println("Error when reading items config : " + exc.getMessage());
			throw new Exception(exc);
		}
	}
	
	public static void loadConfig(Context context) throws Exception {
		try {
		File inputFile = new File(getApplicationDirectory() + "config.ini");
		FileInputStream fis = new FileInputStream(inputFile);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null) {
			if (!line.startsWith(";") && !line.isEmpty()) {
				String[] array = line.split("=");
				String key = array[0].toUpperCase();
				String value = array[1];
				if (key.equals("INCLUDE_DIR")) {
					context.setIncludeDirectory(value);
				} else if (key.equals("DEFAULT_SINGLE_TARGET")) {
					context.setDefaultSingleTargetSpell(value);
				} else if (key.equals("DEFAULT_AOE_TARGET")) {
					context.setDefaultAreaOfEffectSpellTarget(value);
				} else if (key.equals("DEFAULT_TARGET_ALLEGIANCE")) {
					context.setDefaultTargetAllegiance(value);
				} else if (key.equals("DEFAULT_TARGET_COUNT")) {
					context.setDefaultTargetCount(Integer.parseInt(value));
				} else if (key.equals("DEFAULT_TARGET_TYPE_COUNT")) {
					context.setDefaultTargetTypeCount(Integer.parseInt(value));
				} else if (key.equals("DEFAULT_TARGET_RANDOM")) {
					context.setDefaultTargetRandom(Boolean.parseBoolean(value));
				} else if (key.equals("DEFAULT_SPELL_SUCCESS_PERCENT")) {
					context.setDefaultSpellSuccessPercent(Integer.parseInt(value));
				} else if (key.equals("DEFAULT_ITEM_SUCCESS_PERCENT")) {
					context.setDefaultItemSuccessPercent(Integer.parseInt(value));
				} else if (key.equals("AURACLEANSING")) {
					context.setAuraCleansing(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_RAGE")) {
					context.setCheckRage(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_ELEMENTAL_IMMUNITY")) {
					context.setCheckElementalImmunity(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_POISON_IMMUNITY")) {
					context.setCheckPoisonImmunity(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_MAGIC_RESISTANCE")) {
					context.setCheckMagicResistance(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_SPELL_PROTECTIONS")) {
					context.setCheckSpellProtections(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_WEAPON_PROTECTIONS")) {
					context.setCheckWeaponProtections(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_SLEEP_IMMUNITY")) {
					context.setCheckSleepImmunity(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_FEAR_IMMUNITY")) {
					context.setCheckFearImmunity(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_STUN_IMMUNITY")) {
					context.setCheckStunImmunity(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_HOLD_IMMUNITY")) {
					context.setCheckHoldImmunity(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_CHARM_IMMUNITY")) {
					context.setCheckCharmImmunity(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_CONFUSION_IMMUNITY")) {
					context.setCheckConfusionImmunity(Boolean.parseBoolean(value));
				} else if (key.equals("CHECK_DEATH_IMMUNITY")) {
					context.setCheckDeathImmunity(Boolean.parseBoolean(value));
				} else if (key.equals("ALL_CHECK_OVERRIDE")) {
					boolean val = Boolean.parseBoolean(value);
					context.setCheckRage(val);
					context.setCheckElementalImmunity(val);
					context.setCheckPoisonImmunity(val);
					context.setCheckMagicResistance(val);
					context.setCheckSpellProtections(val);
					context.setCheckWeaponProtections(val);
					context.setCheckSleepImmunity(val);
					context.setCheckFearImmunity(val);
					context.setCheckStunImmunity(val);
					context.setCheckHoldImmunity(val);
					context.setCheckCharmImmunity(val);
					context.setCheckConfusionImmunity(val);
					context.setCheckDeathImmunity(val);
				} else {
					throw new Exception("unknown parameter " + key);
				}
			}
		}
		fis.close();
		} catch(Exception exc) {
			System.out.println("Error when reading general config : " + exc.getMessage());
			throw new Exception(exc);
		}
	}

//	public void loadExcelSheets(Context context) {
//		 List sheetData = new ArrayList();
//
//	        FileInputStream fis = null;
//	        try {
//	            //
//	            // Create a FileInputStream that will be use to read the 
//	            // excel file.
//	            //
//	            fis = new FileInputStream("general.xls");
//
//	            //
//	            // Create an excel workbook from the file system.
//	            //
//	            HSSFWorkbook workbook = new HSSFWorkbook(fis);
//	            //
//	            // Get the first sheet on the workbook.
//	            //
//	            HSSFSheet sheet = workbook.getSheetAt(0);
//
//	            //
//	            // When we have a sheet object in hand we can iterator on 
//	            // each sheet's rows and on each row's cells. We store the 
//	            // data read on an ArrayList so that we can printed the 
//	            // content of the excel to the console.
//	            //
//	            Iterator rows = sheet.rowIterator();
//	            while (rows.hasNext()) {
//	                HSSFRow row = (HSSFRow) rows.next();
//	                Iterator cells = row.cellIterator();
//
//	                List data = new ArrayList();
//	                while (cells.hasNext()) {
//	                    HSSFCell cell = (HSSFCell) cells.next();
//	                    data.add(cell);
//	                }
//
//	                sheetData.add(data);
//	            }
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        } finally {
//	            if (fis != null) {
//	                fis.close();
//	            }
//	        }
//	}
}
