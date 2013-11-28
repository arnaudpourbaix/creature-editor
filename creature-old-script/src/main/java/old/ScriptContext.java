package old;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pourbaix.script.creature.generator.GeneratorException;


public class ScriptContext {

	private boolean hasInit;
	private int casterLevel;
	private int casterMaxSpellLevel;
	private String listenToObject;
	private String minorSequencerInclude;
	private String spellSequencerInclude;
	private String spellTriggerInclude;
	private List<String> teleportLocations;
	private boolean randomTeleport;
	private boolean randomLocation;
	private boolean runAwayFromAOESpells;
	
	private String singleTargetSpell;
	private String areaOfEffectSpellTarget;
	private String targetAllegiance;
	private boolean auraCleansing;
	private String classe;
	private boolean isWizard;
	private boolean isPriest;
	private boolean seeInvisible;
	private String offensiveSpells;
	private boolean trackingWithTeleport;
	private String initVariables;
	
	private Monster monster;
	private SectionEnum lastSectionUsed;
	Map<SectionEnum, List<String>> sections;
	
	public ScriptContext(boolean auraCleansing, String defaultTargetAllegiance, String defaultSingleTargetSpell, String defaultAreaOfEffectSpellTarget) {
		this.hasInit = false;
		this.casterLevel = 1;
		this.casterMaxSpellLevel = 10;
		this.minorSequencerInclude = "";
		this.spellSequencerInclude = "";
		this.spellTriggerInclude = "";
		this.auraCleansing = auraCleansing;
		this.targetAllegiance = defaultTargetAllegiance;
		this.singleTargetSpell = defaultSingleTargetSpell;
		this.areaOfEffectSpellTarget = defaultAreaOfEffectSpellTarget;
		this.isWizard = false;
		this.isPriest = false;
		this.listenToObject = "[ANYONE]";
		this.seeInvisible = false;
		this.runAwayFromAOESpells = true;
		this.trackingWithTeleport = false;
		this.monster = null;
		this.sections = new HashMap<SectionEnum, List<String>>();
		this.lastSectionUsed = null;
		this.initVariables = null;
	}
	
	public String getTargetSpell(Block block) throws GeneratorException {
		if (block.getItem() == null && block.getSpell() == null)
			throw new GeneratorException("getTargetSpell : unkown spell or item");
		else if (block.getItem() != null)
			return block.getItem().getRadius() > 0 ? areaOfEffectSpellTarget : singleTargetSpell;
		else
			return block.getSpell().getRadius() > 0 ? areaOfEffectSpellTarget : singleTargetSpell;
	}
	
	public void addToSection(String line) {
		addToSection(lastSectionUsed, line);
	}

	public void addToSection(SectionEnum section, String line) {
		if (!sections.containsKey(section)) {
			sections.put(section, new ArrayList<String>());
		}
		sections.get(section).add(line);
	}

	public String getGlobalClasse() {
		String globalClasse = null;
		if ("DRUID".equals(classe) || "CLERIC".equals(classe)) {
			globalClasse = "PRIEST";
		} else if ("RANGER".equals(classe) || "PALADIN".equals(classe)) {
			globalClasse = "FIGHTER";
		} else if ("MAGE".equals(classe) || "BARD".equals(classe)) {
			globalClasse = "WIZARD";
		} 
		return globalClasse;
	}
	
	public boolean hasInit() {
		return hasInit;
	}
	public void setHasInit(boolean hasInit) {
		this.hasInit = hasInit;
	}
	public int getCasterLevel() {
		return casterLevel;
	}
	public void setCasterLevel(int casterLevel) {
		this.casterLevel = casterLevel;
	}
	public int getCasterMaxSpellLevel() {
		return casterMaxSpellLevel;
	}
	public void setCasterMaxSpellLevel(int casterMaxSpellLevel) {
		this.casterMaxSpellLevel = casterMaxSpellLevel;
	}
	public String getMinorSequencerInclude() {
		return minorSequencerInclude;
	}
	public void setMinorSequencerInclude(String minorSequencerInclude) {
		this.minorSequencerInclude = minorSequencerInclude;
	}
	public String getSpellSequencerInclude() {
		return spellSequencerInclude;
	}
	public void setSpellSequencerInclude(String spellSequencerInclude) {
		this.spellSequencerInclude = spellSequencerInclude;
	}
	public String getSpellTriggerInclude() {
		return spellTriggerInclude;
	}
	public void setSpellTriggerInclude(String spellTriggerInclude) {
		this.spellTriggerInclude = spellTriggerInclude;
	}
	public String getTargetAllegiance() {
		return targetAllegiance;
	}
	public void setTargetAllegiance(String targetAllegiance) {
		this.targetAllegiance = targetAllegiance;
	}
	public boolean isAuraCleansing() {
		return auraCleansing;
	}
	public void setAuraCleansing(boolean auraCleansing) {
		this.auraCleansing = auraCleansing;
	}
	public boolean isWizard() {
		return isWizard;
	}
	public void setWizard(boolean isWizard) {
		this.isWizard = isWizard;
	}
	public boolean isPriest() {
		return isPriest;
	}
	public void setPriest(boolean isPriest) {
		this.isPriest = isPriest;
	}
	public String getListenToObject() {
		return listenToObject;
	}
	public void setListenToObject(String listenToObject) {
		this.listenToObject = listenToObject;
	}
	public boolean isSeeInvisible() {
		return seeInvisible;
	}
	public void setSeeInvisible(boolean seeInvisible) {
		this.seeInvisible = seeInvisible;
	}
	public String getSingleTargetSpell() {
		return singleTargetSpell;
	}
	public void setSingleTargetSpell(String singleTargetSpell) {
		this.singleTargetSpell = singleTargetSpell;
	}
	public String getAreaOfEffectSpellTarget() {
		return areaOfEffectSpellTarget;
	}
	public void setAreaOfEffectSpellTarget(String areaOfEffectSpellTarget) {
		this.areaOfEffectSpellTarget = areaOfEffectSpellTarget;
	}
	public List<String> getTeleportLocations() {
		return teleportLocations;
	}
	public void setTeleportLocations(List<String> teleportLocations) {
		this.teleportLocations = teleportLocations;
	}
	public boolean isRunAwayFromAOESpells() {
		return runAwayFromAOESpells;
	}
	public void setRunAwayFromAOESpells(boolean runAwayFromAOESpells) {
		this.runAwayFromAOESpells = runAwayFromAOESpells;
	}

	public boolean isRandomTeleport() {
		return randomTeleport;
	}

	public void setRandomTeleport(boolean randomTeleport) {
		this.randomTeleport = randomTeleport;
	}

	public boolean isRandomLocation() {
		return randomLocation;
	}

	public void setRandomLocation(boolean randomLocation) {
		this.randomLocation = randomLocation;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getOffensiveSpells() {
		return offensiveSpells;
	}

	public void setOffensiveSpells(String offensiveSpells) {
		this.offensiveSpells = offensiveSpells;
	}

	public boolean isTrackingWithTeleport() {
		return trackingWithTeleport;
	}

	public void setTrackingWithTeleport(boolean trackingWithTeleport) {
		this.trackingWithTeleport = trackingWithTeleport;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public Map<SectionEnum, List<String>> getSections() {
		return sections;
	}

	public void setSections(Map<SectionEnum, List<String>> sections) {
		this.sections = sections;
	}

	public SectionEnum getLastSectionUsed() {
		return lastSectionUsed;
	}

	public void setLastSectionUsed(SectionEnum lastSectionUsed) {
		this.lastSectionUsed = lastSectionUsed;
	}

	public String getInitVariables() {
		return initVariables;
	}

	public void setInitVariables(String initVariables) {
		this.initVariables = initVariables;
	}
	
}
