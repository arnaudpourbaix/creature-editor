package old;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Builder {
	
	private File outputFile;
	private FileOutputStream fos;
	private OutputStreamWriter out;
	private BlockService blockService;
	private Context context;

	public Builder(String input, String output, Context context) throws Exception {
		this.context = context;
		blockService = new BlockService(this.context);
		context.initScriptContext();
		System.out.println("Building " + output + "...");
		try {
			outputFile = new File(output);
			fos = new FileOutputStream(outputFile);
			out = new OutputStreamWriter(fos, "UTF-8");
			ProcessFile(input);
			out.flush();
			fos.flush();
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		System.out.println("Done!");
	}
	
	public void ProcessFile(String input) throws Exception {
		try {
			File inputFile = new File(input);
			FileInputStream fis = new FileInputStream(inputFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			Pattern p1 = Pattern.compile("^[\\[]include=([^\\]]+)[\\]]$");
			Pattern p2 = Pattern.compile("^[\\[]([^\\]]*)[\\]]$");
			Pattern p3 = Pattern.compile("^\\s+.+$");
			String line = null;
			String multiLines = "";
			while ((line = br.readLine()) != null) {
				Matcher m1 = p1.matcher(line);
				Matcher m2 = p2.matcher(line);
				Matcher m3 = p3.matcher(line);
				if (line.equals("[INCLUDE_MINOR_SEQUENCER]")) {
					if (!context.getScriptContext().getMinorSequencerInclude().isEmpty()) {
						String filename = context.getIncludeDirectory() + context.getScriptContext().getMinorSequencerInclude() + Constant.SCRIPT_EXT;
						ProcessFile(filename);
					}
				} else if (line.equals("[INCLUDE_SPELL_SEQUENCER]")) {
					if (!context.getScriptContext().getSpellSequencerInclude().isEmpty()) {
						String filename = context.getIncludeDirectory() + context.getScriptContext().getSpellSequencerInclude() + Constant.SCRIPT_EXT;
						ProcessFile(filename);
					}
				} else if (line.equals("[INCLUDE_SPELL_TRIGGER]")) {
					if (!context.getScriptContext().getSpellTriggerInclude().isEmpty()) {
						String filename = context.getIncludeDirectory() + context.getScriptContext().getSpellTriggerInclude() + Constant.SCRIPT_EXT;
						ProcessFile(filename);
					}
				} else if (m1.find()) {
					String filename = context.getIncludeDirectory() + m1.group(1) + Constant.SCRIPT_EXT;
					//System.out.println("include " + filename);
					ProcessFile(filename);
				} else if (line.trim().equalsIgnoreCase("[GENERATE]")) {
					generateMonster();
				} else if (line.trim().startsWith("[") && !line.trim().endsWith("]")) {
					multiLines = line.substring(1);
				} else if (multiLines.length() > 0 && !line.trim().startsWith("[") && !line.trim().endsWith("]")) {
					multiLines += line.trim();
				} else if (multiLines.length() > 0 && !line.trim().startsWith("[") && line.trim().endsWith("]")) {
					multiLines += line.trim();
					multiLines = multiLines.substring(0, multiLines.length() - 1);
					Block block = blockService.getBlock(multiLines);
					if (block.getSpellLevel() <= context.getScriptContext().getCasterMaxSpellLevel() 
							&& context.getScriptContext().getCasterLevel() >= block.getMinCasterLevel()
							&& context.getScriptContext().getCasterLevel() <= block.getMaxCasterLevel()
							&& block.matchType(context.getScriptContext().isPriest(), context.getScriptContext().isWizard())) {
						out.write(block.generate());
					}
					multiLines = "";
				} else if (m3.find() && context.getScriptContext().getLastSectionUsed() != null) {
					//System.out.println("m3: " + line);
					context.getScriptContext().addToSection(line);
				} else if (m2.find()) {
					//System.out.println(line);
					Block block = blockService.getBlock(m2.group(1));
					if (block.getSpellLevel() <= context.getScriptContext().getCasterMaxSpellLevel() 
							&& context.getScriptContext().getCasterLevel() >= block.getMinCasterLevel()
							&& context.getScriptContext().getCasterLevel() <= block.getMaxCasterLevel()
							&& block.matchType(context.getScriptContext().isPriest(), context.getScriptContext().isWizard())) {
						out.write(block.generate());
					}
				} else if (line.trim().length() > 0) {
					out.write(line + Constant.CR);
				}
			}
			fis.close();
		} catch (FileNotFoundException e) {
			System.err.println(e);
			throw new Exception(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public void generateMonster() throws Exception {
		Pattern p1 = Pattern.compile("^\\s*[\\[]include=([^\\]]+)[\\]]$");
		// Summons are destroyed upon death
		if (context.getScriptContext().getMonster().isSummon()) {
			out.write(blockService.getBlock("name=Destroyed upon death;action=DESTROY").generate());
		}
		// Init
		if (context.getScriptContext().getSections().containsKey(SectionEnum.BEFOREINIT)) {
			for(String line : context.getScriptContext().getSections().get(SectionEnum.BEFOREINIT)) {
				Matcher m1 = p1.matcher(line);
				if (!line.trim().startsWith("[")) {
					out.write(line + Constant.CR);
				} else if (m1.find()) {
					String filename = context.getIncludeDirectory() + m1.group(1) + Constant.SCRIPT_EXT;
					ProcessFile(filename);
				}
				else {
					out.write(blockService.getBlock(line).generate());				
				}
			}
		}
		if (!context.getScriptContext().getMonster().isSummon()) {
			out.write(blockService.getBlock("action=INIT").generate());
			out.write(blockService.getBlock("action=REST").generate());
			out.write(blockService.getBlock("name=Turn hostile if attacked;action=ENEMY").generate());
		}
		if (context.getScriptContext().getSections().containsKey(SectionEnum.AFTERINIT)) {
			for(String line : context.getScriptContext().getSections().get(SectionEnum.AFTERINIT)) {
				Matcher m1 = p1.matcher(line);
				if (!line.trim().startsWith("[")) {
					out.write(line + Constant.CR);
				} else if (m1.find()) {
					String filename = context.getIncludeDirectory() + m1.group(1) + Constant.SCRIPT_EXT;
					ProcessFile(filename);
				}
				else {
					out.write(blockService.getBlock(line).generate());				
				}
			}
		}
		// Shouts
		if (!context.getScriptContext().getMonster().isSummon()) {
			String params = "";
			if (context.getScriptContext().getMonster().getRace() != null) {
				params = String.format(",[%s.%s.%s]", Constant.Allegiance.EVILCUTOFF, 0, context.getScriptContext().getMonster().getRace()); 
			} else if (context.getScriptContext().getMonster().getRace() != null) {
				params = String.format(",[%s.%s.%s.%s]", Constant.Allegiance.EVILCUTOFF, 0, 0, context.getScriptContext().getMonster().getClasse()); 
			}
			out.write(blockService.getBlock("action=SHOUT(monster)").generate());
			out.write(blockService.getBlock("action=LISTEN(monster" + params + ")").generate());
		} else {
			out.write(blockService.getBlock("action=SHOUT(summon)").generate());
			out.write(blockService.getBlock("action=LISTEN(summon)").generate());
		}
		
		// Start combat
		//out.write(blockService.getBlock("action=GLOBAL(ja#combat,1);requireSelf=GLOBAL(ja#combat,0),allegiance(enemy),seeEnemy").generate());
		
		// Follow summoner
		if (context.getScriptContext().getMonster().isSummon()) {
			out.write(blockService.getBlock("name=Follow summoner;action=MOVE;requireSelf=!blind;require=empty,GLOBAL(ja#combat,0),!seeNearest,!see,!invisible,hpgt(0),range(70);target=summoner").generate());
		}
		
		// Random walk
		if (context.getScriptContext().getMonster().isCanRandomWalk()) {
			out.write(blockService.getBlock("action=WALK;require=empty,global(ja#combat,0)").generate());
			out.write(blockService.getBlock("action=NOACTION;require=global(ja#combat,0)").generate());
		} else {
			out.write(blockService.getBlock("action=NOACTION;require=empty,global(ja#combat,0)").generate());			
		}
		
		// Hide in shadows
		if (context.getScriptContext().getMonster().isCanHideInShadows()) {
			out.write(blockService.getBlock("action=TIMER(ja#hide,9),HIDE;requireSelf=empty,statgt(49,STEALTH),!invisible,!seeNearest").generate());
		}
		// Tracking
		if (context.getScriptContext().getSections().containsKey(SectionEnum.BEFORETRACKING)) {
			for(String line : context.getScriptContext().getSections().get(SectionEnum.BEFORETRACKING)) {
				Matcher m1 = p1.matcher(line);
				if (!line.trim().startsWith("[")) {
					out.write(line + Constant.CR);
				} else if (m1.find()) {
					String filename = context.getIncludeDirectory() + m1.group(1) + Constant.SCRIPT_EXT;
					ProcessFile(filename);
				}
				else {
					out.write(blockService.getBlock(line).generate());				
				}
			}
		}
		if (context.getScriptContext().getSections().containsKey(SectionEnum.TRACKING)) {
			for(String line : context.getScriptContext().getSections().get(SectionEnum.TRACKING)) {
				Matcher m1 = p1.matcher(line);
				if (!line.trim().startsWith("[")) {
					out.write(line + Constant.CR);
				} else if (m1.find()) {
					String filename = context.getIncludeDirectory() + m1.group(1) + Constant.SCRIPT_EXT;
					ProcessFile(filename);
				}
				else {
					out.write(blockService.getBlock(line).generate());				
				}
			}
		} else if (context.getScriptContext().getMonster().isCanTrack()) {
			String tracking = context.getScriptContext().getMonster().isSummon() ? "tracking_summon" : "tracking";
			ProcessFile(context.getIncludeDirectory() + tracking + Constant.SCRIPT_EXT);
		}
		if (context.getScriptContext().getSections().containsKey(SectionEnum.AFTERTRACKING)) {
			for(String line : context.getScriptContext().getSections().get(SectionEnum.AFTERTRACKING)) {
				Matcher m1 = p1.matcher(line);
				if (!line.trim().startsWith("[")) {
					out.write(line + Constant.CR);
				} else if (m1.find()) {
					String filename = context.getIncludeDirectory() + m1.group(1) + Constant.SCRIPT_EXT;
					ProcessFile(filename);
				}
				else {
					out.write(blockService.getBlock(line).generate());				
				}
			}
		}
		
		// Combat
		if (context.getScriptContext().getSections().containsKey(SectionEnum.BEFORECOMBAT)) {
			for(String line : context.getScriptContext().getSections().get(SectionEnum.BEFORECOMBAT)) {
				Matcher m1 = p1.matcher(line);
				if (!line.trim().startsWith("[")) {
					out.write(line + Constant.CR);
				} else if (m1.find()) {
					String filename = context.getIncludeDirectory() + m1.group(1) + Constant.SCRIPT_EXT;
					ProcessFile(filename);
				}
				else {
					out.write(blockService.getBlock(line).generate());				
				}
			}
		}
		if (context.getScriptContext().getSections().containsKey(SectionEnum.COMBAT)) {
			for(String line : context.getScriptContext().getSections().get(SectionEnum.COMBAT)) {
				Matcher m1 = p1.matcher(line);
				if (!line.trim().startsWith("[")) {
					out.write(line + Constant.CR);
				} else if (m1.find()) {
					String filename = context.getIncludeDirectory() + m1.group(1) + Constant.SCRIPT_EXT;
					ProcessFile(filename);
				}
				else {
					out.write(blockService.getBlock(line).generate());				
				}
			}
		} else {
			String file = "avoid_combat";
			if (context.getScriptContext().getMonster().isCanAttack()) {
				if (context.getScriptContext().getMonster().isSmart() && context.getScriptContext().getMonster().isCanFightWithMeleeAndRange()) {
					file = "combat_smart_both";
				} else if (context.getScriptContext().getMonster().isSmart()) {
					file = "combat_smart";
				} else if (context.getScriptContext().getMonster().isCanFightWithMeleeAndRange()) {
					file = "combat_simple_both";
				} else {
					file = "combat_simple";
				}
			}
			ProcessFile(context.getIncludeDirectory() + file + Constant.SCRIPT_EXT);
		}
		if (context.getScriptContext().getSections().containsKey(SectionEnum.AFTERCOMBAT)) {
			for(String line : context.getScriptContext().getSections().get(SectionEnum.AFTERCOMBAT)) {
				Matcher m1 = p1.matcher(line);
				if (!line.trim().startsWith("[")) {
					out.write(line + Constant.CR);
				} else if (m1.find()) {
					String filename = context.getIncludeDirectory() + m1.group(1) + Constant.SCRIPT_EXT;
					ProcessFile(filename);
				}
				else {
					out.write(blockService.getBlock(line).generate());				
				}
			}
		}
	}
	
}
