package old;
import java.util.List;


public class Context {

	private List<Spell> spells;
	private List<Item> items;
	
	private String includeDirectory;
	
	private String defaultSingleTargetSpell;
	private String defaultAreaOfEffectSpellTarget;
	private String defaultTargetAllegiance;
	private int defaultTargetCount;
	private int defaultTargetTypeCount;
	private boolean defaultTargetRandom;
	private int defaultSpellSuccessPercent;
	private int defaultItemSuccessPercent;

	private boolean auraCleansing;
	
	private boolean checkRage;
	private boolean checkElementalImmunity;
	private boolean checkPoisonImmunity;
	private boolean checkMagicResistance;	
	private boolean checkSpellProtections;	
	private boolean checkWeaponProtections;	
	private boolean checkSleepImmunity;	
	private boolean checkFearImmunity;	
	private boolean checkStunImmunity;	
	private boolean checkHoldImmunity;	
	private boolean checkCharmImmunity;	
	private boolean checkConfusionImmunity;
	private boolean checkDeathImmunity;
	
	private ScriptContext scriptContext;
	
	public void initScriptContext() {
		scriptContext = new ScriptContext(auraCleansing, defaultTargetAllegiance, defaultSingleTargetSpell, defaultAreaOfEffectSpellTarget);
	}

	public List<Spell> getSpells() {
		return spells;
	}

	public void setSpells(List<Spell> spells) {
		this.spells = spells;
	}

	public String getIncludeDirectory() {
		return includeDirectory;
	}

	public void setIncludeDirectory(String includeDirectory) {
		this.includeDirectory = includeDirectory;
	}

	public boolean isCheckRage() {
		return checkRage;
	}

	public void setCheckRage(boolean checkRage) {
		this.checkRage = checkRage;
	}

	public boolean isCheckElementalImmunity() {
		return checkElementalImmunity;
	}

	public void setCheckElementalImmunity(boolean checkElementalImmunity) {
		this.checkElementalImmunity = checkElementalImmunity;
	}

	public boolean isCheckPoisonImmunity() {
		return checkPoisonImmunity;
	}

	public void setCheckPoisonImmunity(boolean checkPoisonImmunity) {
		this.checkPoisonImmunity = checkPoisonImmunity;
	}

	public boolean isCheckMagicResistance() {
		return checkMagicResistance;
	}

	public void setCheckMagicResistance(boolean checkMagicResistance) {
		this.checkMagicResistance = checkMagicResistance;
	}

	public boolean isCheckSpellProtections() {
		return checkSpellProtections;
	}

	public void setCheckSpellProtections(boolean checkSpellProtections) {
		this.checkSpellProtections = checkSpellProtections;
	}

	public boolean isCheckWeaponProtections() {
		return checkWeaponProtections;
	}

	public void setCheckWeaponProtections(boolean checkWeaponProtections) {
		this.checkWeaponProtections = checkWeaponProtections;
	}

	public boolean isCheckSleepImmunity() {
		return checkSleepImmunity;
	}

	public void setCheckSleepImmunity(boolean checkSleepImmunity) {
		this.checkSleepImmunity = checkSleepImmunity;
	}

	public boolean isCheckFearImmunity() {
		return checkFearImmunity;
	}

	public void setCheckFearImmunity(boolean checkFearImmunity) {
		this.checkFearImmunity = checkFearImmunity;
	}

	public boolean isCheckStunImmunity() {
		return checkStunImmunity;
	}

	public void setCheckStunImmunity(boolean checkStunImmunity) {
		this.checkStunImmunity = checkStunImmunity;
	}

	public boolean isCheckHoldImmunity() {
		return checkHoldImmunity;
	}

	public void setCheckHoldImmunity(boolean checkHoldImmunity) {
		this.checkHoldImmunity = checkHoldImmunity;
	}

	public boolean isCheckCharmImmunity() {
		return checkCharmImmunity;
	}

	public void setCheckCharmImmunity(boolean checkCharmImmunity) {
		this.checkCharmImmunity = checkCharmImmunity;
	}

	public boolean isCheckConfusionImmunity() {
		return checkConfusionImmunity;
	}

	public void setCheckConfusionImmunity(boolean checkConfusionImmunity) {
		this.checkConfusionImmunity = checkConfusionImmunity;
	}

	public boolean isCheckDeathImmunity() {
		return checkDeathImmunity;
	}

	public void setCheckDeathImmunity(boolean checkDeathImmunity) {
		this.checkDeathImmunity = checkDeathImmunity;
	}

	public String getDefaultTargetAllegiance() {
		return defaultTargetAllegiance;
	}

	public void setDefaultTargetAllegiance(String defaultTargetAllegiance) {
		this.defaultTargetAllegiance = defaultTargetAllegiance;
	}

	public ScriptContext getScriptContext() {
		return scriptContext;
	}

	public void setScriptContext(ScriptContext scriptContext) {
		this.scriptContext = scriptContext;
	}

	public boolean isAuraCleansing() {
		return auraCleansing;
	}

	public void setAuraCleansing(boolean auraCleansing) {
		this.auraCleansing = auraCleansing;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public int getDefaultSpellSuccessPercent() {
		return defaultSpellSuccessPercent;
	}

	public void setDefaultSpellSuccessPercent(int defaultSpellSuccessPercent) {
		this.defaultSpellSuccessPercent = defaultSpellSuccessPercent;
	}

	public int getDefaultItemSuccessPercent() {
		return defaultItemSuccessPercent;
	}

	public void setDefaultItemSuccessPercent(int defaultItemSuccessPercent) {
		this.defaultItemSuccessPercent = defaultItemSuccessPercent;
	}

	public int getDefaultTargetCount() {
		return defaultTargetCount;
	}

	public void setDefaultTargetCount(int defaultTargetCount) {
		this.defaultTargetCount = defaultTargetCount;
	}

	public boolean isDefaultTargetRandom() {
		return defaultTargetRandom;
	}

	public void setDefaultTargetRandom(boolean defaultTargetRandom) {
		this.defaultTargetRandom = defaultTargetRandom;
	}

	public String getDefaultSingleTargetSpell() {
		return defaultSingleTargetSpell;
	}

	public void setDefaultSingleTargetSpell(String defaultSingleTargetSpell) {
		this.defaultSingleTargetSpell = defaultSingleTargetSpell;
	}

	public String getDefaultAreaOfEffectSpellTarget() {
		return defaultAreaOfEffectSpellTarget;
	}

	public void setDefaultAreaOfEffectSpellTarget(
			String defaultAreaOfEffectSpellTarget) {
		this.defaultAreaOfEffectSpellTarget = defaultAreaOfEffectSpellTarget;
	}

	public int getDefaultTargetTypeCount() {
		return defaultTargetTypeCount;
	}

	public void setDefaultTargetTypeCount(int defaultTargetTypeCount) {
		this.defaultTargetTypeCount = defaultTargetTypeCount;
	}
	
}
