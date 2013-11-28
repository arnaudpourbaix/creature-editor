package old;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockService {

	Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public BlockService(Context context) {
		this.context = context;
	}
	
	public Block getBlock(String str) throws Exception {
		Block block = new Block();
		str = str.trim();
		block.setInput(str);
		if (str.startsWith("[")) {
			str = str.substring(1);
		}
		if (str.endsWith("]")) {
			str = str.substring(0, str.length() - 1);
		}
		for (String s : str.split("[;]")) {
			Param param = new Param(s, '=');
			//System.out.println(s + ": name=" + param.getName() + " / value=" + param.getValue());
			try {
				if ("MAX_SPELL_LEVEL".equals(param.getName())) {
					context.getScriptContext().setCasterMaxSpellLevel(Integer.valueOf(param.getValue()));
				} else if ("CASTER_LEVEL".equals(param.getName())) {
					context.getScriptContext().setCasterLevel(Integer.valueOf(param.getValue()));
				} else if ("LEVEL".equals(param.getName())) {
					block.setSpellLevel(Integer.valueOf(param.getValue()));
				} else if ("MINCASTERLEVEL".equals(param.getName())) {
					block.setMinCasterLevel(Integer.valueOf(param.getValue()));
				} else if ("MAXCASTERLEVEL".equals(param.getName())) {
					block.setMaxCasterLevel(Integer.valueOf(param.getValue()));
				} else if ("TYPE".equals(param.getName())) {
					block.setType(param.getValue());
				} else if ("TARGET_ALLEGIANCE".equals(param.getName())) {
					context.getScriptContext().setTargetAllegiance(param.getValue());
				} else if ("AURACLEANSING".equals(param.getName())) {
					context.getScriptContext().setAuraCleansing(Boolean.parseBoolean(param.getValue()));
				} else if ("MOVEMENT".equals(param.getName())) {
					context.getScriptContext().setTrackingWithTeleport("TELEPORT".equalsIgnoreCase(param.getValue()));
				} else if ("RUN_AWAY_FROM_AOE".equals(param.getName())) {
					context.getScriptContext().setRunAwayFromAOESpells(Boolean.parseBoolean(param.getValue()));
				} else if ("CLASS".equals(param.getName())) {
					context.getScriptContext().setClasse(param.getValue());
					if (param.getValue().equals("CLERIC") || param.getValue().equals("PRIEST") || param.getValue().equals("DRUID"))
						context.getScriptContext().setPriest(true);
					else if (param.getValue().equals("MAGE") || param.getValue().equals("WIZARD"))
						context.getScriptContext().setWizard(true);
				} else if ("LISTEN".equals(param.getName())) {
					context.getScriptContext().setListenToObject(String.format("[%s]", param.getValue()));
				} else if ("MINOR_SEQUENCER".equals(param.getName())) {
					context.getScriptContext().setMinorSequencerInclude(param.getValue());
				} else if ("SPELL_SEQUENCER".equals(param.getName())) {
					context.getScriptContext().setSpellSequencerInclude(param.getValue());
				} else if ("SPELL_TRIGGER".equals(param.getName())) {
					context.getScriptContext().setSpellTriggerInclude(param.getValue());
				} else if ("RANDOM_LOCATION".equals(param.getName())) {
					block.setRandomLocation(Boolean.parseBoolean(param.getValue()));
				} else if ("RANDOM_TELEPORT".equals(param.getName())) {
					block.setRandomTeleport(Boolean.parseBoolean(param.getValue()));
				} else if ("TELEPORT_LOCATION".equals(param.getName())) {
					block.setTeleportConfig(true);
					List<String> lst = new ArrayList<String>();
					for(String loc : param.getValue().split(",")) {
						lst.add(loc);
					}
					context.getScriptContext().setTeleportLocations(lst);
					block.setTeleportLocations(lst);
				} else if (param.getName() == null && "TELEPORT".equals(param.getValue())) {
					teleport(block);
				} else if ("NAME".equals(param.getName())) {
					block.setComment(param.getRawValue());
				} else if ("INITVARIABLES".equals(param.getName())) {
					context.getScriptContext().setInitVariables(param.getValue());
				} else if ("MONSTER".equals(param.getName())) {
					parseMonster(block, param.getValue());
				} else if ("SECTION".equals(param.getName())) {
					parseSection(block, param.getValue());
				} else if ("SPELL".equals(param.getName())) {
					parseSpell(block, param.getValue(), Constant.SpellAction.SPELL);
				} else if ("FORCESPELL".equals(param.getName())) {
					parseSpell(block, param.getValue(), Constant.SpellAction.FORCESPELL);
				} else if ("REALLYFORCESPELL".equals(param.getName())) {
					parseSpell(block, param.getValue(), Constant.SpellAction.REALLYFORCESPELL);
				} else if ("ITEM".equals(param.getName())) {
					parseItem(block, param.getValue());
				} else if ("TARGET".equals(param.getName())) {
					parseTarget(block, param.getValue());
				} else if ("ACTION".equals(param.getName())) {
					parseAction(block, param.getValue());
				} else if ("REQUIRE".equals(param.getName())) {
					parseCondition(block, param.getValue(), TargetEnum.ENEMY.getCode());
				} else if ("REQUIRESELF".equals(param.getName())) {
					parseCondition(block, param.getValue(), TargetEnum.MYSELF.getCode());
				} else if ("ACTIONREQUIRE".equals(param.getName())) {
					parseActionCondition(block, param.getValue(), TargetEnum.ENEMY.getCode());
				} else if ("OFFENSIVE_SPELLS".equals(param.getName())) {
					offensiveSpells(block, param.getValue());
	 			} else throw new Exception("Unknown attribut: " + param.getName());
			} catch (Exception e) {
				System.out.println("Error in " + s);
				throw new Exception(e);
			}
		}
		if (block.getActions().size() > 0 && block.getTargets().size() == 0 && block.getDefaultTarget() != null) {
			parseTarget(block, block.getDefaultTarget());
		}
		return block;
	}

	public void teleport(Block block) {
		for(Action action : block.getActions()) {
			for(Response response : action.getResponses()) {
				response.addLine(ActionEnum.SetGlobal, true, "ja#teleport", "1");
			}
		}
	}

	public void parseMonster(Block block, String params) throws Exception {
		try {
			context.getScriptContext().setMonster(new Monster());
			Pattern p = Pattern.compile("([^,(]+([(][^)]*[)])?)");
			Matcher m = p.matcher(params);
			while (m.find()) {
				Entity entity = Tools.getEntity(m.group(1));
				if ("RACE".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setRace(entity.getFirstParamValue());
				} else if ("CLASS".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setClasse(entity.getFirstParamValue());
				} else if ("SUMMON".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setSummon(true);
				} else if ("HIDE".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setCanHideInShadows(true);
				} else if ("TELEPORT".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setCanTeleport(true);
					context.getScriptContext().setTrackingWithTeleport(true);
				} else if ("SMART".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setSmart(true);
				} else if ("BOTH".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setCanFightWithMeleeAndRange(true);
				} else if ("WALK".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setCanRandomWalk(true);
				} else if ("NOTRACKING".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setCanTrack(false);
				} else if ("NOCOMBAT".equalsIgnoreCase(entity.getName())) {
					context.getScriptContext().getMonster().setCanAttack(false);
				} else {
					throw new Exception("unknown parameter: " + entity.getName());
				}
			}
		} catch(Exception e) {
			throw new Exception("parse Monster: " + params + ". " + e);
		}
	}
	
	public void parseSection(Block block, String params) throws Exception {
		SectionEnum section = SectionEnum.fromString(params);
		context.getScriptContext().setLastSectionUsed(section);
	}
	
	public void parseTarget(Block block, String targets) throws Exception {
		if (Tools.equalsIgnoreCase(targets, "default"))
			targets = context.getScriptContext().getTargetSpell(block);
		Pattern p = Pattern.compile("([^,(]+([(][^)]*[)])?)");
		Matcher m = p.matcher(targets);
		while (m.find()) {
			Entity entity = Tools.getEntity(m.group(1));
			TargetService.addTargets(context, block.getTargets(), entity);
		}
	}
	
	public void parseAction(Block block, String actions) throws Exception {
		Pattern p = Pattern.compile("([^,(]+([(][^)]*[)])?)");
		Matcher m = p.matcher(actions);
		while (m.find()) {
			Entity entity = Tools.getEntity(m.group(1));
			if (entity.getName().equals("INIT"))
				actionInit(block, entity);
			else if (entity.getName().equals("REST"))
				actionRest(block, entity);
			else if (entity.getName().equals("ENEMY"))
				actionEnemy(block, entity);
			else if (entity.getName().equals("SHOUT"))
				actionShout(block, entity);
			else if (entity.getName().equals("LISTEN"))
				actionListen(block, entity);
			else if (entity.getName().equals("GLOBAL"))
				actionGlobal(block, entity);
			else if (entity.getName().equals("INCREMENT"))
				actionIncrement(block, entity);
			else if (entity.getName().equals("TIMER"))
				actionTimer(block, entity);			
			else if (entity.getName().equals("NOACTION"))
				block.getAction().addLine(ActionEnum.NoAction);
			else if (entity.getName().equals("WALK"))
				block.getAction().addLine(ActionEnum.RandomWalk);
			else if (entity.getName().equals("CONTINUE"))
				block.getAction().addLine(ActionEnum.Continue);
			else if (entity.getName().equals("MOVE"))
				actionMove(block, entity);
			else if (entity.getName().equals("RUN"))
				actionRun(block, entity);			
			else if (entity.getName().equals("AUTOCAST"))
				actionAutoCast(block, entity);			
			else if (entity.getName().equals("CREATECREATURE"))
				actionCreateCreature(block, entity);
			else if (entity.getName().equals("HIDE"))
				block.getAction().addLine(ActionEnum.Hide);
			else if (entity.getName().equals("DESTROY")) {
				block.getAction().addCondition(new Condition(ConditionEnum.Die));
				block.getAction().addLine(ActionEnum.DestroySelf);
			}
			else if (entity.getName().equals("ATTACK"))
				actionAttack(block, entity, false);			
			else if (entity.getName().equals("BACKSTAB"))
				actionAttack(block, entity, true);			
			else if (entity.getName().equals("RANGED")) {
				actionRanged(block, entity);
			} else if (entity.getName().equals("MELEE")) {
				actionMelee(block, entity);
			} else if (entity.getName().equals("TURNUNDEAD"))
				actionTurnUndead(block, entity);
			else if (entity.getName().equals("MINORSEQUENCER"))
				actionSequencer(block, entity, Constant.Sequencer.MINOR_SEQUENCER);			
			else if (entity.getName().equals("SEQUENCER"))
				actionSequencer(block, entity, Constant.Sequencer.SPELL_SEQUENCER);			
			else if (entity.getName().equals("TRIGGER"))
				actionSequencer(block, entity, Constant.Sequencer.SPELL_TRIGGER);			
			else if (entity.getName().equals("CONTINGENCY"))
				actionContingency(block, entity, Constant.Contingency.CONTINGENCY);			
			else if (entity.getName().equals("CHAINCONTINGENCY"))
				actionContingency(block, entity, Constant.Contingency.CHAIN_CONTINGENCIES);			
			else if (entity.getName().equals("SPELL"))
				actionSpell(block, entity, Constant.SpellAction.SPELL);			
			else if (entity.getName().equals("REMOVE"))
				removeSpell(block, entity);			
			else if (entity.getName().equals("FORCESPELL"))
				actionSpell(block, entity, Constant.SpellAction.FORCESPELL);			
			else if (entity.getName().equals("REALLYFORCESPELL"))
				actionSpell(block, entity, Constant.SpellAction.REALLYFORCESPELL);			
			else if (entity.getName().equals("APPLYSPELL"))
				actionSpell(block, entity, Constant.SpellAction.APPLYSPELL);			
			else throw new Exception("Unknown action: " + entity.getName());
		}
	}
	
	public void parseCondition(Block block, String conditions, String target) throws Exception {
		ConditionService.addConditions(block, context, block.getConditions(), conditions, target);
	}

	public void parseActionCondition(Block block, String conditions, String target) throws Exception {
		ConditionService.addConditions(block, context, block.getAction().getConditions(), conditions, target);
	}
	
	public void actionInit(Block block, Entity entity) throws Exception {
		if (context.getScriptContext().hasInit())
			return;
		context.getScriptContext().setHasInit(true);
		block.addVariable(context, "JA#INIT", true, "0", "1");
		block.getAction().addLine(ActionEnum.SetGlobalTimer, "JA#REST", "2400");
		block.getAction().addLine(ActionEnum.SetGlobal, "JA#COMBAT", "0");
		block.getAction().addLine(ActionEnum.SetGlobal, Constant.Variable.DISABLE_SPELLCASTING, "0");
		if (context.getScriptContext().getInitVariables() != null) {
			for(String s : context.getScriptContext().getInitVariables().split(",")) {
				Pattern p = Pattern.compile("([^(]+)[(]([^)]*)[)]");
				Matcher m = p.matcher(s);
				while (m.find()) {
					block.getAction().addLine(ActionEnum.SetGlobal, m.group(1), m.group(2));
				}
			}
		}
		for (int i = 0; i < entity.getParams().size(); i++) {
			String value = "0";
			String var = "";
			if (entity.getParams().get(i).getName() != null) {
				var = entity.getParams().get(i).getName().toUpperCase();
				value = entity.getParams().get(i).getValue();
			} else {
				var = entity.getParams().get(i).getValue().toUpperCase();
			}
			block.getAction().addLine(ActionEnum.SetGlobal, var, value);
		}
	}

	public void actionRest(Block block, Entity entity) throws Exception {
		block.getAction().addCondition(new Condition(ConditionEnum.GlobalTimerExpired, true, "JA#REST"));
		block.addVariable(context, "JA#INIT", true, "1", "0");
		block.getAction().addCondition(new Condition(ConditionEnum.Detect, false, Tools.encloseString(context.getScriptContext().getTargetAllegiance(), '[')));
		block.getAction().addLine(ActionEnum.Rest);
		block.getAction().addLine(ActionEnum.ApplySpellRES, "JA#HEAL", TargetEnum.MYSELF.getCode());
		if (context.getScriptContext().isPriest() || context.getScriptContext().isWizard()) {
			block.getAction().addLine(ActionEnum.SetGlobal, "JA#AUTOCAST", "0");
			block.getAction().addLine(ActionEnum.SetGlobal, "JA#SHAPESHIFT", "0");
		}
		if (context.getScriptContext().isWizard() || context.getScriptContext().isPriest() || context.getScriptContext().getMonster() != null) { 
			block.getAction().addLine(ActionEnum.SetGlobal, Constant.Variable.MELEE, "0");
		}
		if (context.getScriptContext().isWizard()) {
			if (context.getScriptContext().getCasterMaxSpellLevel() >= 6)
				block.getAction().addLine(ActionEnum.SetGlobal, "JA#CONTINGENCY", "0");
			if (context.getScriptContext().getCasterMaxSpellLevel() >= 9)
				block.getAction().addLine(ActionEnum.SetGlobal, "JA#CHAIN_CONTINGENCY", "0");
			if (context.getScriptContext().getCasterMaxSpellLevel() >= 4)
				block.getAction().addLine(ActionEnum.SetGlobal, "JA#MINOR_SEQUENCER", "0");
			if (context.getScriptContext().getCasterMaxSpellLevel() >= 6)
				block.getAction().addLine(ActionEnum.SetGlobal, "JA#SPELL_SEQUENCER", "0");
			if (context.getScriptContext().getCasterMaxSpellLevel() >= 8)
				block.getAction().addLine(ActionEnum.SetGlobal, "JA#SPELL_TRIGGER", "0");
		}
	}
	
	public void actionEnemy(Block block, Entity entity) throws Exception {
		block.getAction().addCondition(ConditionService.getAllegiance(context, false));
		block.getAction().addCondition(new Condition(ConditionEnum.AttackedBy, true, Tools.getSpecificObject(context.getScriptContext().getTargetAllegiance()), "DEFAULT"));
		block.getAction().addLine(ActionEnum.Enemy);
	}

	public void actionShout(Block block, Entity entity) throws Exception {
		boolean isSummon = false;
		ConditionEnum trigger = ConditionEnum.See;
		String num = "97";
		if (entity.getParams().get(0).getValue().equals("MONSTER")) {
			num = "95";
		} else if (entity.getParams().get(0).getValue().equals("SUMMON")) {
			num = "98";
			isSummon = true;
		}
		if (entity.getParams().size() > 1 && entity.getParams().get(1).getValue().equals("DETECT")) {
			trigger = ConditionEnum.Detect;
		}
		block.addVariable(context, "JA#COMBAT", true, "0", "1");
		if (!isSummon) {
			block.getAction().addCondition(ConditionService.getAllegiance(context, true));
			block.getAction().addCondition(new Condition(trigger, true, Tools.getSpecificObject(context.getScriptContext().getTargetAllegiance())));
			block.getAction().addLine(ActionEnum.Shout,num);
		} else {
			block.getAction().addCondition(new Condition(trigger, true, TargetEnum.NEARESTENEMY.getCode()));
			block.getAction().addLine(ActionEnum.Shout,num);
		}
	}

	public void actionListen(Block block, Entity entity) throws Exception {
		boolean isSummon = false;
		String num = "97";
		String source = context.getScriptContext().getListenToObject();
		if (entity.getParams().get(0).getValue().equals("MONSTER")) {
			num = "95";
		} else if (entity.getParams().get(0).getValue().equals("SUMMON")) {
			num = "98";
			isSummon = true;
			source = TargetEnum.LASTSUMMONER.getCode();
		}
		if (entity.getParams().size() > 1) {
			source = entity.getParams().get(1).getValue();
		}
		block.addVariable(context, "JA#COMBAT", true, "0", "1");
		block.getAction().addCondition(new Condition(ConditionEnum.Heard, true, source, num));
		if (!isSummon && entity.getParams().size() == 0)
			block.getAction().addCondition(ConditionService.getAllegiance(context, true, TargetEnum.LASTHEARD.getCode()));
		else if (isSummon) {
			block.getAction().addCondition(new Condition(ConditionEnum.Allegiance, TargetEnum.MYSELF.getCode(), Constant.Allegiance.ENEMY));
		}
		block.getAction().addLine(ActionEnum.Enemy);
	}

	public void actionRanged(Block block, Entity entity) throws Exception {
		ConditionService.addConditions(block, context, block.getConditions(), "!MAGICALWEAPONITEM,!RANGE(5,NEAREST)", TargetEnum.MYSELF.getCode());
		block.getAction().addLine(ActionEnum.EquipRanged);
		block.getAction().addLine(ActionEnum.Continue);
	}

	public void actionMelee(Block block, Entity entity) throws Exception {
		ConditionService.addConditions(block, context, block.getConditions(), "!MAGICALWEAPONITEM,RANGE(5,NEAREST)", TargetEnum.MYSELF.getCode());
		block.getAction().addLine(ActionEnum.EquipMostDamagingMelee);
		block.getAction().addLine(ActionEnum.Continue);
	}
	
	public void actionGlobal(Block block, Entity entity) throws Exception {
		block.getAction().addLine(ActionEnum.SetGlobal, entity.getParams().get(0).getValue(), entity.getParams().get(1).getValue());
	}

	public void actionIncrement(Block block, Entity entity) {
		block.getAction().addLine(ActionEnum.IncrementGlobal, entity.getParams().get(0).getValue(), entity.getParams().get(1).getValue());
	}
	
	public void actionTimer(Block block, Entity entity) throws Exception {
		block.addTimer(context, ConditionEnum.GlobalTimerNotExpired, false, entity.getParams().get(0).getValue(), entity.getParams().get(1).getValue());
	}

	public void actionRun(Block block, Entity entity) {
		String target = TargetEnum.LASTSEEN.getCode();
		if (entity.getParams().size() >= 2 && entity.getParams().get(1).getValue().equals(TargetEnum.NEARESTENEMY.getShortcut()))
			target = TargetEnum.NEARESTENEMY.getCode();
		block.getAction().addLine(ActionEnum.RunAwayFromNoInterrupt, target, entity.getParams().get(0).getValue());
	}
	
	public void actionMove(Block block, Entity entity) throws Exception {
		if (context.getScriptContext().isTrackingWithTeleport()) {
			Spell spell = Tools.getSpell(context, "PHASE_SPIDER_TELEPORT");
			block.getAction().addLine(spell.getCastAction(Constant.SpellAction.FORCESPELL, TargetEnum.LASTSEEN.getCode()));
		} else if (entity.getParams().size() == 0) {
			block.getAction().addLine(ActionEnum.MoveToObject, TargetEnum.LASTSEEN.getCode());
		} else {
			block.getAction().addLine(ActionEnum.ForceSpell, TargetEnum.LASTSEEN.getCode(), entity.getParams().get(0).getValue());
		}
	}
	
	public void actionCreateCreature(Block block, Entity entity) {
		block.getAction().addLine(String.format("CreateCreature('%s',[-1.-1],0)", entity.getParams().get(0).getValue()));
	}
	
	public void actionAttack(Block block, Entity entity, boolean backstab) throws Exception {
		//block.getConditions().add(new Condition(ConditionEnum.ActionListEmpty));
		if (backstab)
			block.getAction().addCondition(new Condition(ConditionEnum.StateCheck, true, TargetEnum.MYSELF.getCode(), StateEnum.STATE_INVISIBLE.toString()));
		if (entity.getParams().size() > 0 && !entity.getParams().get(0).getValue().equals("")) {
			if (entity.getParams().get(0).getValue().equals("MELEE"))
				block.getAction().addLine(ActionEnum.EquipMostDamagingMelee);
			else if (entity.getParams().get(0).getValue().equals("RANGED"))
				block.getAction().addLine(ActionEnum.EquipRanged);
			else if ("SLOT".equals(entity.getParams().get(0).getName())) {
				String slot = "";
				if (entity.getParams().get(0).getValue().equals("2"))
					slot = "1";
				else if (entity.getParams().get(0).getValue().equals("3"))
					slot = "2";
				if (entity.getParams().get(0).getValue().equals("4"))
					slot = "3";
				block.getAction().addLine(ActionEnum.SelectWeaponAbility, "SLOT_WEAPON" + slot, "0");		
			}
			for (int i = 1; i < entity.getParams().size(); i++) {
				String param = entity.getParams().get(i).getValue().toUpperCase();
				if (!param.equals("")) {
					throw new Exception("Unknown parameters on attack: " + block.getInput());
				}
			}
		}
		ConditionService.addConditions(block, context, block.getConditions(), "!MAGICWEAPONPROTECTION", TargetEnum.ENEMY.getCode());
		block.getConditions().add(new Condition(ConditionEnum.General, false, Constant.Affect.SUMMON, TargetEnum.ENEMY.getCode(), Constant.General.WEAPON)); // Wizard eye and Mordenkainen sword
		block.getConditions().add(new Condition(ConditionEnum.CheckStatGT, false, TargetEnum.ENEMY.getCode(), "0", StatEnum.SANCTUARY.toString()));
		block.getConditions().add(new Condition(ConditionEnum.See, true, TargetEnum.ENEMY.getCode()));
		if (backstab)
			block.getAction().addLine(ActionEnum.MoveToObjectNoInterrupt, TargetEnum.LASTSEEN.getCode());
		if (context.getScriptContext().getMonster() != null) {
			//block.getAction().addLine(ActionEnum.AttackReevaluate, TargetEnum.LASTSEEN.getCode(), "15");
			//block.getAction().getConditions().add(new Condition(ConditionEnum.OR, "2"));
			block.getConditions().add(new Condition(ConditionEnum.ActionListEmpty));
			//block.getAction().getConditions().add(new Condition(ConditionEnum.GlobalTimerNotExpired, false, Constant.Variable.ATTACK));
			//block.getAction().addLine(ActionEnum.SetGlobalTimer, Constant.Variable.ATTACK, "3");
			//block.getAction().addLine(ActionEnum.Attack, TargetEnum.LASTSEEN.getCode());
			//block.getAction().addLine(ActionEnum.AttackReevaluate, TargetEnum.LASTSEEN.getCode(), "30");
			block.getAction().addLine(ActionEnum.AttackOneRound, TargetEnum.LASTSEEN.getCode());
			//block.getConditions().add(new Condition(ConditionEnum.GlobalTimerNotExpired, false, Constant.Variable.ATTACK));
		} else {
			//block.getAction().addLine(ActionEnum.AttackOneRound, TargetEnum.LASTSEEN.getCode());
			//block.getAction().getConditions().add(new Condition(ConditionEnum.OR, "2"));
			block.getConditions().add(new Condition(ConditionEnum.ActionListEmpty));
			//block.getAction().getConditions().add(new Condition(ConditionEnum.GlobalTimerNotExpired, false, Constant.Variable.ATTACK));
			//block.getAction().addLine(ActionEnum.SetGlobalTimer, Constant.Variable.ATTACK, "3");
			//block.getAction().addLine(ActionEnum.Attack, TargetEnum.LASTSEEN.getCode());
			block.getAction().addLine(ActionEnum.AttackReevaluate, TargetEnum.LASTSEEN.getCode(), "30");
			//block.getConditions().add(new Condition(ConditionEnum.GlobalTimerNotExpired, false, Constant.Variable.ATTACK));
		}
		//block.getAction().addLine(ActionEnum.Attack, TargetEnum.LASTSEEN.getCode());
	}
	
	public void actionTurnUndead(Block block, Entity entity) throws Exception {
		block.getConditions().add(new Condition(ConditionEnum.ModalState, false, "TURNUNDEAD"));
		if (entity.getParams().size() == 2) {
			block.getConditions().add(new Condition(ConditionEnum.CheckStatGT, true, TargetEnum.MYSELF.getCode(), entity.getParams().get(1).getValue(), StatEnum.TURNUNDEADLEVEL.toString()));
			block.getConditions().add(new Condition(ConditionEnum.LevelLT, true, TargetEnum.ENEMY.getCode(), entity.getParams().get(0).getValue()));
		}
		block.getConditions().add(new Condition(ConditionEnum.General, true, TargetEnum.ENEMY.getCode(), Constant.General.UNDEAD));
		block.getConditions().add(new Condition(ConditionEnum.CheckStat, true, TargetEnum.ENEMY.getCode(), "0", StatEnum.HELD.toString()));
		block.getConditions().add(new Condition(ConditionEnum.Range, true, TargetEnum.ENEMY.getCode(), "10"));
		block.getConditions().add(new Condition(ConditionEnum.See, true, TargetEnum.ENEMY.getCode()));

		Target target = TargetService.getTarget(context, TargetEnum.NEARESTENEMYOFTYPE.getCode(), 3, context.getScriptContext().getTargetAllegiance(), Constant.General.UNDEAD, "0", "0", "0", Constant.Gender.SUMMONED);
		target.setRandom(false);
		block.addTarget(target);
		
		block.getAction().addLine(ActionEnum.SetInterrupt, "FALSE");
		block.addTimer(context, ConditionEnum.GlobalTimerNotExpired, false, "JA#TURN", "18");
		block.getAction().addLine(ActionEnum.DisplayStringHead, TargetEnum.MYSELF.getCode(), "4974");
		block.getAction().addLine(ActionEnum.Turn);
		block.getAction().addLine(ActionEnum.Wait, "6");
		block.getAction().addLine(ActionEnum.SetInterrupt, "TRUE");
	}
	
	public void offensiveSpells(Block block, String value) throws Exception {
		context.getScriptContext().setOffensiveSpells(value);
		block.getAction().addCondition(new Condition(ConditionEnum.Global, Constant.Variable.MELEE, "0"));
		for (String s : value.split(",")) {
			Spell spell = Tools.getSpell(context, s);
			block.getAction().addCondition(spell.getHaveSpellCondition(false));
		}
		block.getAction().addLine(ActionEnum.SetGlobal, Constant.Variable.MELEE, "1");
	}
	
	public void parseSpell(Block block, String value, String spellAction) throws Exception {
		Entity entity = Tools.getEntity(value);
		actionSpell(block, entity, spellAction);
	}
	
	public void actionSpell(Block block, Entity entity, String spellAction) throws Exception {
		//System.out.println("input:" + entity.getInput());
		try {
			Pattern p = Pattern.compile("^([^(]+)[(]([^),]+)[,]?([^)]*)[)]$");
			Matcher m = p.matcher(entity.getInput());
			if (m.find()) {
				String value = m.group(2) + "(" + m.group(3) + ")";
				Entity e = Tools.getEntity(value);
				//System.out.println(value);
				processSpell(block, e, spellAction);
			} else {
				processSpell(block, entity, spellAction);
			}
		} catch(Exception exc) {
			System.out.println("actionSpell " + entity.getInput() + " : " + exc.getMessage());
			throw new Exception(exc);
		}
	}

	public void processSpell(Block block, Entity entity, String spellAction) throws Exception {
		Spell spell = Tools.getSpell(context, entity.getName());
		block.setDefaultTarget(spell.getDefaultTarget());
		block.setComment(spell.getName());
		block.setSpellLevel(spell.getLevel());
		block.addCommonSpellCheck(context, spell.isTargetSelf(), spell.isTeleport());
		ConditionService.addConditions(block, context, block.getAction().getConditions(), spell.getSelfConditions(), TargetEnum.ALLY.getCode());
		ConditionService.addConditions(block, context, block.getConditions(), spell.getTargetConditions(), TargetEnum.ENEMY.getCode());
		if (spell.getDetectableStat() != null) {
			block.getConditions().add(spell.getDetectableCondition(TargetEnum.LASTSEEN.getCode()));
		}
		parseSpellType(block, spell.getType());
		parseDamageType(block, spell.getDamageType());
		block.addRoundTimer(context);
		block.getAction().addLine(spell.getCastAction(spellAction, TargetEnum.LASTSEEN.getCode()));
		if (block.isMemorized()) {
			//block.getAction().addCondition(0, spell.getHaveSpellCondition());
			block.getConditions().add(0, spell.getHaveSpellCondition());
		}
		block.setSpell(spell);
		block.addSpellProtectionConditions(context);
		if (spell.isTargetSelf()) {
			if (spell.getRadius() > 0) {
				block.getAction().addCondition(new Condition(ConditionEnum.Range, true, TargetEnum.NEARESTENEMY.getCode(), String.valueOf(spell.getRadius())));
			}
		}
		for (Param param : entity.getParams()) {
			if ("REMOVE".equalsIgnoreCase(param.getValue())) {
				if (spell.getIdentifier().isEmpty()) {
					block.getAction().addLine(ActionEnum.RemoveSpellRES, Tools.encloseString(spell.getResource()));
				} else {
					block.getAction().addLine(ActionEnum.RemoveSpell, spell.getIdentifier());
				}
			}
		}
		if (spell.getRadius() > 0 && spell.isCanHurtAllies()) {
			Action action = block.getAction().clone();
			String condition = "";
			if (context.getScriptContext().isWizard()) {
				if (spell.getLevel() == 5) {
					condition += "MAJORGLOBE,";
				} else if (spell.getLevel() == 4) {
					condition += "GLOBE,";
				} else if (spell.getLevel() <= 3) {    
					condition += "MINORGLOBE,";
				}
				if (spell.getSchool() == SchoolEnum.Invoker) {
					condition += "INVOCATIONIMMUNITY,";
				} else if (spell.getSchool() == SchoolEnum.Conjurer) {
					condition += "CONJURATIONIMMUNITY,";
				} else if (spell.getSchool() == SchoolEnum.Diviner) {
					condition += "DIVINATIONIMMUNITY,";
				} else if (spell.getSchool() == SchoolEnum.Enchanter) {
					condition += "ENCHANTMENTIMMUNITY,";
				} else if (spell.getSchool() == SchoolEnum.Necromancer) {
					condition += "NECROMANCYIMMUNITY,";
				} else if (spell.getSchool() == SchoolEnum.Transmuter) {
					condition += "ALTERATIONIMMUNITY,";
				}
			}
			if (spell.getDamageType().equalsIgnoreCase("FIRE")) {
				condition += "FIREIMMUNITY,";
			} else if (spell.getDamageType().equalsIgnoreCase("COLD")) {
				condition += "COLDIMMUNITY,";
			} else if (spell.getDamageType().equalsIgnoreCase("ACID")) {
				condition += "ACIDIMMUNITY,";
			} else if (spell.getDamageType().equalsIgnoreCase("ELECTRICITY")) {
				condition += "ELECTRICITYIMMUNITY,";
			} else if (spell.getDamageType().equalsIgnoreCase("POISON")) {
				condition += "POISONIMMUNITY,";
			} else if (spell.getDamageType().equalsIgnoreCase("MAGIC")) {
				condition += "MAGICDAMAGEIMMUNITY,";
			}
			if (spell.getType().equalsIgnoreCase("HOLD")) {
				if (context.getScriptContext().isPriest()) {
					condition += "FREEACTION,";
				}
			}
			if (condition.endsWith(",")) {
				condition = condition.substring(0, condition.length() - 1);
			}
			if (!condition.isEmpty()) {
				if (condition.split(",").length > 1) {
					condition = "OR(" + condition + ")";
				}
				ConditionService.addConditions(block, context, block.getAction().getConditions(), condition.toUpperCase(), TargetEnum.ALLY.getCode());
			}
			if (!spell.isTargetSelf() && context.getScriptContext().isRunAwayFromAOESpells()) {
				block.getActions().add(action);
				block.getAction().addLine(ActionEnum.RunAwayFromNoInterrupt, TargetEnum.LASTSEEN.getCode(),  "90");
				block.addRandomNum(context, true);
			} else {
				block.addRandomNum(context, true);
			}
		}
	}

	public void removeSpell(Block block, Entity entity) throws Exception {
		for(Param param : entity.getParams()) {
			Spell spell = Tools.getSpell(context, param.getValue());
			if (spell.getIdentifier().isEmpty()) {
				block.getAction().addLine(ActionEnum.RemoveSpellRES, Tools.encloseString(spell.getResource()));
			} else {
				block.getAction().addLine(ActionEnum.RemoveSpell, spell.getIdentifier());
			}
		}
	}
	
	public void parseItem(Block block, String value) throws Exception {
		Item item = Tools.getItem(context, value);
		block.setComment(item.getName());
		block.addRoundTimer(context);
		block.getAction().addCondition(0, item.getHasItemCondition(true, false));
		block.getAction().addLine(item.getUseAction(TargetEnum.LASTSEEN.getCode()));
		ConditionService.addConditions(block, context, block.getConditions(), item.getSelfConditions(), TargetEnum.MYSELF.getCode());
		ConditionService.addConditions(block, context, block.getConditions(), item.getTargetConditions(), TargetEnum.ENEMY.getCode());
		block.addRandomNum(context, false);
		block.addCommonSpellCheck(context, item.isTargetSelf(), false);
		if (!item.isOnlyBG2()) {
			block.addAction();
			block.addRoundTimer(context);
			block.getAction().addCondition(0, item.getHasItemCondition(true, true));
			block.getAction().addLine(item.getUseAction(TargetEnum.LASTSEEN.getCode(), true));
			block.addRandomNum(context, false);
		}
		parseSpellType(block, item.getType());
		parseDamageType(block, item.getDamageType());
		block.setItem(item);
		block.addSpellProtectionConditions(context);
	}
	
	public void actionAutoCast(Block block, Entity entity) throws Exception {
		Spell spell = Tools.getSpell(context, entity.getParams().get(0).getValue());
		block.setComment(spell.getName());
		block.setSpellLevel(spell.getLevel());
		block.getAction().addCondition(new Condition(ConditionEnum.Global, true, "JA#AUTOCAST", "0"));
		block.getAction().addCondition(0, spell.getHaveSpellCondition());
		block.getAction().addLine(spell.getCastAction(Constant.SpellAction.REALLYFORCESPELL, TargetEnum.LASTSEEN.getCode()));
		block.getAction().addLine(ActionEnum.Continue);
	}

	public void parseSpellType(Block block, String type) throws Exception {
		if (type == null || type.isEmpty())
			return;
		type = type.toUpperCase();
		if (type.equals("SLEEP") || type.equals("FEAR") || type.equals("HOLD") || type.equals("CHARM") || type.equals("STUN") || type.equals("CONFUSION") 
				|| type.equals("DEATH") || type.equals("PETRIFICATION") || type.equals("FAILURE")  || type.equals("SILENCE")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!DISABLE", TargetEnum.ENEMY.getCode());
		}
		if (type.equals("SLEEP")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!SLEEPIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (type.equals("FEAR")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!FEARIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (type.equals("HOLD")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!HOLDIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (type.equals("CHARM")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!CHARMIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (type.equals("STUN")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!STUNIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (type.equals("CONFUSION")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!CONFUSIONIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (type.equals("DEATH")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!DEATHIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (type.equals("FAILURE")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!SILENCE,!FAILURE", TargetEnum.ENEMY.getCode());
		} else if (type.equals("SILENCE")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!SILENCE,!FAILURE", TargetEnum.ENEMY.getCode());
		} else if (type.equals("BLIND")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!BLIND", TargetEnum.ENEMY.getCode());
		}
	}
	
	public void parseDamageType(Block block, String damageType) throws Exception {
		if (damageType == null || damageType.isEmpty())
			return;
		damageType = damageType.toUpperCase();
		if (damageType.equals("FIRE")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!FIREIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (damageType.equals("COLD")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!COLDIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (damageType.equals("ACID")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!ACIDIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (damageType.equals("ELECTRICITY")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!ELECTRICITYIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (damageType.equals("POISON")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!POISONIMMUNITY", TargetEnum.ENEMY.getCode());
		} else if (damageType.equals("MAGIC")) {
			ConditionService.addConditions(block, context, block.getConditions(), "!MAGICDAMAGEIMMUNITY", TargetEnum.ENEMY.getCode());
		}
	}

	public void actionSequencer(Block block, Entity entity, int sequencer) throws Exception {
		int spell_count = 2;
		String var = "";
		String strRef = "";
		if (sequencer == Constant.Sequencer.MINOR_SEQUENCER) {
			var = "JA#MINOR_SEQUENCER";
			strRef = "5013";
			block.setComment("Minor Sequencer");
		} else if (sequencer == Constant.Sequencer.SPELL_SEQUENCER) {
			spell_count = 3;
			var = "JA#SPELL_SEQUENCER";
			strRef = "25951";
			block.setComment("Spell Sequencer");
		} else {
			spell_count = 3;
			var = "JA#SPELL_TRIGGER";
			strRef = "26243";
			block.setComment("Spell Trigger");
		}
		
		int response = 0;
		for (int i = spell_count; i < entity.getParams().size(); i++) {
			if ("RESPONSE".equals(entity.getParams().get(i).getName())) {
				response = Integer.valueOf(entity.getParams().get(i).getValue());
			}
		}
			
		if (response == 0 && block.getAction().getResponses().isEmpty()) {
			block.getAction().addDefaultResponse();
		} else if (response > 0) {
			block.getAction().addResponse(response);
		}
		
		block.addVariable(context, var, true, "0", "1");
		block.addRoundTimer(context);
		block.getAction().addLine(ActionEnum.DisplayString, TargetEnum.MYSELF.getCode(), strRef);
		
		Spell highestLevelSpell = null;
		for (int i = 0; i < spell_count; i++) {
			Spell spell = Tools.getSpell(context, entity.getParams().get(i).getValue());
			block.getAction().addLine(spell.getCastAction(Constant.SpellAction.REALLYFORCESPELL, TargetEnum.LASTSEEN.getCode()));
			ConditionService.addConditions(block, context, block.getAction().getConditions(), spell.getSelfConditions(), TargetEnum.MYSELF.getCode());
			//ConditionService.addConditions(block, context, block.getConditions(), spell.getTargetConditions(), TargetEnum.ENEMY.getCode());
			if (highestLevelSpell == null || spell.getLevel() > highestLevelSpell.getLevel())
				highestLevelSpell = spell;
		}
		block.setSpell(highestLevelSpell);
		block.addSpellProtectionConditions(context);
		if (block.getAction().getResponses().size() == 1) {
			block.addCommonSpellCheck(context, highestLevelSpell.isTargetSelf(), false);
		}
	}
	
	public void actionContingency(Block block, Entity entity, int contingency) throws Exception {
		// require = seeEnemy, hit, poison, helpless, hp%lt(50), hp%lt(25), hp%lt(10)
		// Contingency target = myself
		// Chain Contingencies target: myself, nearestEnemy, hitter
		int spell_count = 1;
		String var = "";
		String strRef = "";
		if (contingency == Constant.Contingency.CONTINGENCY) {
			var = "JA#CONTINGENCY";
			strRef = "36936";
			block.setComment("Contingency");
			if (block.getMinCasterLevel() < 12) {
				block.setMinCasterLevel(12);
			}
		} else {
			spell_count = 3;
			var = "JA#CHAIN_CONTINGENCY";
			strRef = "84743";
			block.setComment("Chain Contingencies");
			if (block.getMinCasterLevel() < 18) {
				block.setMinCasterLevel(18);
			}
		}
		int response = 0;
		String target = TargetEnum.MYSELF.getCode();
		for (int i = spell_count; i < entity.getParams().size(); i++) {
			if (TargetEnum.NEARESTENEMY.getShortcut().equalsIgnoreCase(entity.getParams().get(i).getValue())) {
				target = TargetEnum.NEARESTENEMY.getCode();
			} else if (TargetEnum.LASTHITTER.getShortcut().equalsIgnoreCase(entity.getParams().get(i).getValue())) {
				target = TargetEnum.LASTHITTER.getCode();
			} else if ("RESPONSE".equals(entity.getParams().get(i).getName())) {
				response = Integer.valueOf(entity.getParams().get(i).getValue());
			}
		}
		if (response == 0 && block.getAction().getResponses().isEmpty()) {
			block.getAction().addDefaultResponse();
		} else if (response > 0) {
			block.getAction().addResponse(response);
		}
		block.addVariable(context, var, true, "0", "1");
		for (int i = 0; i < spell_count; i++) {
			Spell spell = Tools.getSpell(context, entity.getParams().get(i).getValue());
			block.getAction().addLine(spell.getCastAction(Constant.SpellAction.REALLYFORCESPELL, target, true));
		}
		block.getAction().addLine(ActionEnum.DisplayString, TargetEnum.MYSELF.getCode(), strRef);
		block.getAction().addCondition(new Condition(ConditionEnum.Allegiance, TargetEnum.MYSELF.getCode(), Constant.Allegiance.ENEMY));
	}
	
}
