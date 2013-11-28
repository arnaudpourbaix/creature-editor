package old;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Block {
	private String input;
	private String comment = null;
	private List<Condition> conditions = new ArrayList<Condition>();
	private List<Action> actions = new ArrayList<Action>();
	private List<Target> targets = new ArrayList<Target>();
	private boolean isRandom = true;
	private boolean isMemorized = true;
	private boolean affectedByRound = true;
	private boolean useDisableSpellcasting = true;
	private boolean isTeleportConfig = false;
	private List<String> teleportLocations;
	private boolean randomTeleport;
	private boolean randomLocation;
	private String type;
	private String defaultTarget;
	private String targetObject;
	private String targetAlly = TargetEnum.MYSELF.getCode();
	private boolean targetRandom;
	private int spellLevel;
	private int minCasterLevel;
	private int maxCasterLevel;
	private StringBuffer monsterLines;
	
	private Spell spell;
	private Item item;
	
	public Block() {
		targetObject = TargetEnum.LASTSEEN.getCode();		
		spellLevel = 0;
		minCasterLevel = 1;
		maxCasterLevel = 40;
	}

	public Action getAction() {
		if (actions.isEmpty())
			addAction();
		return this.actions.get(this.actions.size() - 1);
	}

	public Target getTarget() {
		return this.targets.get(this.targets.size() - 1);
	}
	
	public void addAction() {
		actions.add(new Action());
	}

	public void addTarget(Target target) {
		targets.add(target);
	}
	
	public void addRoundTimer(Context context) throws Exception {
		if (!this.affectedByRound) {
			return;
		}
		if (!ConditionService.find(this.getAction().getConditions(), Constant.Variable.ROUND)) {
			if (context.getScriptContext().isAuraCleansing()) {
				this.getAction().addCondition(0, new Condition(ConditionEnum.OR, "2"));
				this.getAction().addCondition(1, new Condition(ConditionEnum.GlobalTimerNotExpired, false, Constant.Variable.ROUND));
				this.getAction().addCondition(2, new Condition(ConditionEnum.CheckStatGT, TargetEnum.MYSELF.getCode(), "0", StatEnum.AURACLEANSING.toString()));
			} else {
				this.getAction().addCondition(0, new Condition(ConditionEnum.GlobalTimerNotExpired, false, Constant.Variable.ROUND));
			}
		}
		this.getAction().addLine(ActionEnum.SetGlobalTimer, Constant.Variable.ROUND, "6");
	}

	public void addCommonSpellCheck(Context context, boolean isTargetSelf, boolean isTeleport) throws Exception {
		this.getAction().getConditions().add(new Condition(ConditionEnum.StateCheck, false, (isTargetSelf ? TargetEnum.ALLY.getCode() : TargetEnum.ENEMY.getCode()), StateEnum.STATE_REALLY_DEAD.toString()));
		if (!ConditionService.find(this.getAction().getConditions(), Constant.Variable.DISABLE_SPELLCASTING)) {
			if (this.useDisableSpellcasting) {
				this.conditions.add(new Condition(ConditionEnum.Global, true, Constant.Variable.DISABLE_SPELLCASTING, "0"));
			}
			if (!isTargetSelf) {
				this.conditions.add(new Condition(ConditionEnum.General, false, Constant.Affect.SUMMON, TargetEnum.ENEMY.getCode(), Constant.General.WEAPON)); // Wizard eye and Mordenkainen sword
				this.conditions.add(new Condition(ConditionEnum.CheckStat, false, Constant.Affect.PLAYER, TargetEnum.ENEMY.getCode(), "1", StatEnum.POLYMORPH_OTHER.toString()));
				if (!isTeleport) {
					this.conditions.add(new Condition(ConditionEnum.CheckStatGT, false, Constant.Affect.PLAYER, TargetEnum.ENEMY.getCode(), "0", StatEnum.SANCTUARY.toString()));
					if (!context.getScriptContext().isSeeInvisible()) {
						this.conditions.add(new Condition(ConditionEnum.StateCheck, false, Constant.Affect.PLAYER, TargetEnum.ENEMY.getCode(), StateEnum.STATE_IMPROVEDINVISIBILITY.toString()));
					}
					this.conditions.add(new Condition(ConditionEnum.See, true, TargetEnum.ENEMY.getCode()));
				}
			}
		}
	}
	
	public void addTimer(Context context, ConditionEnum condition, boolean result, String timer, String set) throws Exception {
		if (!ConditionService.find(this.getAction().getConditions(), timer)) {
			this.getAction().addCondition(new Condition(condition, result, timer));
		}
		this.getAction().addLine(ActionEnum.SetGlobalTimer, timer, set);
	}
	
	public void addVariable(Context context, String var, boolean result, String test, String set) throws Exception {
		if (!ConditionService.find(this.getAction().getConditions(), var)) {
			this.getAction().addCondition(new Condition(ConditionEnum.Global, result, var, test));
		}
		this.getAction().addLine(ActionEnum.SetGlobal, var, set);
	}
	
	public void addRandomNum(Context context, boolean isSpell) throws Exception {
		String value = String.valueOf(isSpell ? context.getDefaultSpellSuccessPercent() : context.getDefaultItemSuccessPercent());
		addRandomNum(context, value);
	}
	
	public void addRandomNum(Context context, String value) throws Exception {
		int percent = 100;
		if (value != null && !value.isEmpty()) {
			percent = Integer.parseInt(value);
		}
		RandomNum rdm = new RandomNum(percent, 100);
		if (rdm.getNum() < rdm.getMax() && this.isRandom) {
			if (!ConditionService.find(this.getAction().getConditions(), "RandomNumGT") && !ConditionService.find(this.getConditions(), "RandomNumGT")) {
				this.getAction().addCondition(new Condition(ConditionEnum.RandomNumGT, true, rdm.getMax().toString(), rdm.getDiff().toString()));
			}
		}
	}
	
	public void addSpellProtectionConditions(Context context) throws Exception {
		boolean isTargetSelf = spell != null ? spell.isTargetSelf() : item.isTargetSelf();
		int level = spell != null ? spell.getLevel() : item.getLevel();
		if (!isTargetSelf) {
			String conditions = "!MAGICIMMUNITY,";
			if (level == 5) {
				conditions += "!MAJORGLOBE,";
			} else 	if (level == 4) {
				conditions += "!GLOBE,";
			} else 	if (level <= 3) {    
				conditions += "!MINORGLOBE,";
			}
			ConditionService.addConditions(this, context, this.conditions, conditions, TargetEnum.ENEMY.getCode());
		}
	}
	
	public String generate() throws Exception {
		if (monsterLines != null) {
			return monsterLines.toString();
		}
		if (isTeleportConfig) {
			return generateTeleport();
		}
		String output = "";
		if (targets.size() > 0) {
			StringBuilder sbTarget = new StringBuilder();
			generateTarget(sbTarget);
			output += sbTarget.toString().replace("'", "\"");
		}
		if (comment != null && !comment.isEmpty()) {
			output += "// " + comment + Constant.CR;
		}
		StringBuilder sbAction = new StringBuilder();
		for(Action action : this.actions) {
			if (action.getResponses().size() > 0) {
				if (targetObject.equalsIgnoreCase("PLAYERS")) {
					int indice = targetRandom ? 6 : 1;
					for (int i = 1; i <= 6; i++) {
						targetObject = "Player" + indice;
						if (targetRandom) {
							ConditionService.removeCondition(ConditionEnum.RandomNumGT, this.getAction().getConditions());
							if (indice != 1) {
								this.getAction().addCondition(new Condition(ConditionEnum.RandomNumGT, true, "1000", Constant.RANDOMNUM[indice - 1].toString()));
							}
						}
						generateAction(sbAction, action);
						if (targetRandom)
							indice--;
						else
							indice++;
					}
				} else {
					generateAction(sbAction, action);
				}
			}
		}
		output += sbAction.toString().replace("'", "\"") + Constant.CR;
		return output;
	}

	public String generateTeleport() throws Exception {
		if (randomLocation) {
			getAction().addCondition(new Condition(ConditionEnum.Global, "ja#teleport", "1"));
			int j = Math.min(Constant.RANDOM_RESPONSE.length - 1, teleportLocations.size() - 1);
			for (int i = 0; i < teleportLocations.size(); i++) {
				getAction().addResponse(Constant.RANDOM_RESPONSE[j--]);
				if (j < 0) {
					j = Math.min(Constant.RANDOM_RESPONSE.length - 1, teleportLocations.size() - 1);
				}
				getAction().addLine(ActionEnum.SetGlobal, "ja#teleport_location", String.valueOf(i + 1));
				getAction().addLine(ActionEnum.SetGlobal, "ja#teleport", "2");
			}
		} else {
			getAction().addCondition(new Condition(ConditionEnum.Global, "ja#teleport", "1"));
			getAction().addLine(ActionEnum.IncrementGlobal, "ja#teleport_location", "1");
			getAction().addLine(ActionEnum.SetGlobal, "ja#teleport", "2");
			addAction();
			getAction().addCondition(new Condition(ConditionEnum.Global, "ja#teleport", "2"));
			getAction().addCondition(new Condition(ConditionEnum.Global, "ja#teleport_location", String.valueOf(teleportLocations.size() + 1)));
			getAction().addLine(ActionEnum.SetGlobal, "ja#teleport_location", "1");
			
		}
		int count = 1;
		for(String t : teleportLocations) {
			addAction();
			getAction().addCondition(new Condition(ConditionEnum.Global, "ja#teleport", "2"));
			getAction().addCondition(new Condition(ConditionEnum.GlobalTimerNotExpired, false, "ja#teleport_timer"));
			getAction().addCondition(new Condition(ConditionEnum.Global, "ja#teleport_location", String.valueOf(count++)));
			getAction().addLine(ActionEnum.SetGlobalTimer, "ja#teleport_timer", "6");
			getAction().addLine(ActionEnum.SetGlobal, "ja#teleport", "0");
			getAction().addLine(ActionEnum.ForceSpellPoint, Tools.encloseString(t, '['), "WIZARD_DIMENSION_DOOR");
		}
		if (randomTeleport) {
			addAction();
			getAction().addCondition(new Condition(ConditionEnum.See, false, "NearestEnemyOf(Myself)"));
			getAction().addLine(ActionEnum.SetGlobal, "ja#teleport", "1");
		}
		StringBuilder sbAction = new StringBuilder();
		for(Action action : this.actions) {
			generateAction(sbAction, action);
		}
		return sbAction.toString().replace("'", "\"") + Constant.CR;
	}
	
	@SuppressWarnings("unchecked")
	public void generateAction(StringBuilder sb, Action action) {
		List<Condition> lstCondition = ConditionService.mergeList(this.conditions, action.getConditions(), false);
		if (ConditionService.isListCanBeSorted(lstCondition))
			Collections.sort(lstCondition, new ComparatorActionCondition());
		sb.append("IF" + Constant.CR);
		int t = 0;
		for (Condition c : lstCondition) {
			if (t > 0) {
				sb.append(Constant.TAB);
				t--;
			}
			sb.append(Constant.TAB + c.getName().replace(TargetEnum.ENEMY.getCode(), this.targetObject).replace(TargetEnum.ALLY.getCode(), this.targetAlly) + Constant.CR);
			if (c.getCode() == ConditionEnum.OR) {
				t = Integer.valueOf(c.getValues()[0]);
			}
		}
		sb.append("THEN" + Constant.CR);
		for (Response r : action.getResponses()) {
			sb.append(Constant.TAB + "RESPONSE #" + r.getNum() + Constant.CR);
			for (String s : r.getLines()) {
				sb.append(Constant.TAB + Constant.TAB + s.replace(TargetEnum.LASTSEEN.getCode(), this.targetObject).replace(TargetEnum.ALLY.getCode(), this.targetAlly) + Constant.CR);
			}
		}
		sb.append("END" + Constant.CR);
	}

	public void generateTarget(StringBuilder sb) throws Exception {
		for (Target t : targets) {
			List<Condition> lstCondition = ConditionService.mergeList(this.conditions, t.getConditions());
			ConditionService.filterList(lstCondition, t);
			if (t.getCode() == TargetEnum.WEAKEST) {
				sb.append("IF" + Constant.CR);
				sb.append(Constant.TAB + "OR(2)" + Constant.CR);
				sb.append(Constant.TAB + Constant.TAB + "!See(MostDamagedOf())" + Constant.CR);
				sb.append(Constant.TAB + Constant.TAB + "RandomNumGT(1000,500)" + Constant.CR);
				sb.append(Constant.TAB + "See(WorstAC())" + Constant.CR);
				sb.append("THEN" + Constant.CR);
				sb.append(Constant.TAB + "RESPONSE #100" + Constant.CR);
				sb.append(Constant.TAB + Constant.TAB + "Continue()" + Constant.CR);
				sb.append("END" + Constant.CR + Constant.CR);
			} else if (t.getName().equalsIgnoreCase("MYSELF")) {
				sb.append("IF" + Constant.CR);
				sb.append(Constant.TAB + "See(Myself)" + Constant.CR);
				sb.append("THEN" + Constant.CR);
				sb.append(Constant.TAB + "RESPONSE #100" + Constant.CR);
				sb.append(Constant.TAB + Constant.TAB + "Continue()" + Constant.CR);
				sb.append("END" + Constant.CR + Constant.CR);
			} else if (t.getCount() == 0) {
				//System.out.println("generateTarget (count 0): " + t.getCode());
				if (t.getCode() == TargetEnum.ALLY) {
					if (t.getType().startsWith("[")) {
						this.targetAlly = t.getType();
					} else {
						this.targetAlly = t.getCode().getCode();
					}
				} else if (t.getCode() == TargetEnum.CRE) {
					this.targetAlly = Tools.encloseString(t.getType());
					getAction().addCondition(new Condition(ConditionEnum.See, true, targetAlly));
				} else {
					this.targetObject = t.getCode().getCode();
				}
				this.targetRandom = t.isRandom();
			} else {
				//System.out.println("generateTarget: " + t.getCode());
				if (isOneBlockTarget(t, lstCondition)) {
					generateOneBlockTarget(t, sb);
				} else {
					generateManyBlockTarget(t, sb);
				}
			}
		}
	}
	
	public boolean isOneBlockTarget(Target target, List<Condition> lstCondition) {
		boolean result = true;
		int count = 0;
		for (Condition c : lstCondition) {
			if (count > 0) {
				if (c.getName().contains(TargetEnum.ENEMY.getCode())) {
					result = false;
					break;
				}
				count--;
			}
			if (c.getCode() == ConditionEnum.OR) {
				count = Integer.valueOf(c.getValues()[0]);
				break;
			}
		}
		return result;
	}
	
	public void generateOneBlockTarget(Target target, StringBuilder sb) throws Exception {
		sb.append("IF" + Constant.CR);
		List<Condition> lstCondition = ConditionService.mergeList(this.conditions, target.getConditions());
		//ConditionService.addEmptyAction(lstCondition);
		ConditionService.filterList(lstCondition, target);
		int count = 0;
		for (Condition c : ConditionService.getOthersConditions(lstCondition)) {
			if (count > 0) {
				sb.append(Constant.TAB);
				count--;
			}
			sb.append(Constant.TAB + c.getName() + Constant.CR);
			if (c.getCode() == ConditionEnum.OR) {
				count = Integer.valueOf(c.getValues()[0]);
			}
		}
		lstCondition = ConditionService.getEnemyConditions(lstCondition);
		int indice = target.isRandom() ? target.getCount() - 1 : 0;
		for (int i = 0; i < target.getCount(); i++) {
			int orCount = lstCondition.size() + (target.isRandom() && Constant.RANDOMNUM[indice] > 0 ? 1 : 0);
			if (orCount > 1) {
				sb.append(Constant.TAB + String.format("OR(%d)", orCount) + Constant.CR);
			}
			for (Condition c : lstCondition) {
				String t = "";
				if (target.getCode() == TargetEnum.PLAYER)
					t = Constant.PLAYERS[indice];
				else if (target.getCode() == TargetEnum.NEARESTENEMY)
					t = Constant.NEAREST[indice];
				else if (target.getCode() == TargetEnum.NEARESTENEMYOFTYPE) {
					if (target.getClasses() == null) {
						t = Constant.NEARESTOFTYPE[indice];
					} else {
						t = target.getClasses()[indice];
					}
				}
				String s = c.getName().replace(TargetEnum.ENEMY.getCode(), t);
				if (target.getType() != null)
					s = s.replace("<TYPE>", target.getType());
				if (orCount > 1) {
					sb.append(Constant.TAB);
				}
				s = s.startsWith("!") ? s.substring(1) : "!" + s;  
				sb.append(Constant.TAB + s + Constant.CR);
			}
			if (target.isRandom() && Constant.RANDOMNUM[indice] > 0)
				sb.append(Constant.TAB + Constant.TAB + String.format("RandomNumGT(1000,%d)", Constant.RANDOMNUM[indice]) + Constant.CR);
			if (target.isRandom()) 
				indice--;
			else
				indice++;
		}
		sb.append("THEN" + Constant.CR);
		sb.append(Constant.TAB + "RESPONSE #100" + Constant.CR);
		sb.append(Constant.TAB + Constant.TAB + "Continue()" + Constant.CR);
		sb.append("END" + Constant.CR + Constant.CR);
	}

	public void generateManyBlockTarget(Target target, StringBuilder sb) throws Exception {
		List<Condition> lstCondition = ConditionService.mergeList(this.conditions, target.getConditions());
		//ConditionService.addEmptyAction(lstCondition);
		ConditionService.filterList(lstCondition, target);
		int indice = target.isRandom() ? 0 : target.getCount() - 1;
		for (int i = 0; i < target.getCount(); i++) {
			sb.append("IF" + Constant.CR);
			int orCount = 0;
			Pattern p = Pattern.compile("OR[(](\\d+)[)]");
			for (Condition c : lstCondition) {
				String t = "";
				if (target.getCode() == TargetEnum.PLAYER)
					t = Constant.PLAYERS[indice];
				else if (target.getCode() == TargetEnum.NEARESTENEMY)
					t = Constant.NEAREST[indice];
				else if (target.getCode() == TargetEnum.NEARESTENEMYOFTYPE) {
					if (target.getClasses() == null) {
						t = Constant.NEARESTOFTYPE[indice];
					} else {
						t = target.getClasses()[indice];
					}
				}
				String s = c.getName().replace(TargetEnum.ENEMY.getCode(), t);
				if (target.getType() != null)
					s = s.replace("<TYPE>", target.getType());
				if (orCount > 0) {
					sb.append(Constant.TAB);
					orCount--;
				}
				sb.append(Constant.TAB + s + Constant.CR);
				Matcher m = p.matcher(c.getName());
				if (m.find()) {
					orCount = Integer.parseInt(m.group(1));
				}
			}
			sb.append("THEN" + Constant.CR);
			sb.append(Constant.TAB + "RESPONSE #100" + Constant.CR);
			sb.append(Constant.TAB + Constant.TAB + "Continue()" + Constant.CR);
			sb.append("END" + Constant.CR + Constant.CR);
			if (target.isRandom()) 
				indice++;
			else
				indice--;
		}
	}

	public boolean matchType(boolean isPriest, boolean isWizard) {
		boolean result = true;
		if ("MAGE".equals(type) || "WIZARD".equals(type)) {
			result = isWizard;
		}
		if ("CLERIC".equals(type) || "PRIEST".equals(type)) {
			result = isPriest;
		}
		return result;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public int getSpellLevel() {
		return spellLevel;
	}

	public void setSpellLevel(int spellLevel) {
		this.spellLevel = spellLevel;
	}

	public int getMinCasterLevel() {
		return minCasterLevel;
	}

	public void setMinCasterLevel(int minCasterLevel) {
		this.minCasterLevel = minCasterLevel;
	}

	public int getMaxCasterLevel() {
		return maxCasterLevel;
	}

	public void setMaxCasterLevel(int maxCasterLevel) {
		this.maxCasterLevel = maxCasterLevel;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public List<Target> getTargets() {
		return targets;
	}

	public void setTargets(List<Target> targets) {
		this.targets = targets;
	}

	public boolean isRandom() {
		return isRandom;
	}

	public void setRandom(boolean isRandom) {
		this.isRandom = isRandom;
	}

	public boolean isAffectedByRound() {
		return affectedByRound;
	}

	public void setAffectedByRound(boolean affectedByRound) {
		this.affectedByRound = affectedByRound;
	}

	public boolean isUseDisableSpellcasting() {
		return useDisableSpellcasting;
	}

	public void setUseDisableSpellcasting(boolean useDisableSpellcasting) {
		this.useDisableSpellcasting = useDisableSpellcasting;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public boolean isMemorized() {
		return isMemorized;
	}

	public void setMemorized(boolean isMemorized) {
		this.isMemorized = isMemorized;
	}

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isTeleportConfig() {
		return isTeleportConfig;
	}

	public void setTeleportConfig(boolean isTeleportConfig) {
		this.isTeleportConfig = isTeleportConfig;
	}

	public List<String> getTeleportLocations() {
		return teleportLocations;
	}

	public void setTeleportLocations(List<String> teleportLocations) {
		this.teleportLocations = teleportLocations;
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

	public String getDefaultTarget() {
		return defaultTarget;
	}

	public void setDefaultTarget(String defaultTarget) {
		this.defaultTarget = defaultTarget;
	}

	public StringBuffer getMonsterLines() {
		return monsterLines;
	}

	public void setMonsterLines(StringBuffer monsterLines) {
		this.monsterLines = monsterLines;
	}
	
}
