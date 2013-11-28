package com.pourbaix.script.creature.action;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum ActionEnum {
	ActionOverride("O:Actor, A:Action"), Activate("O:Object"), AddAreaFlags("I:TypeAREAFLAG"), AddAreaType("I:TypeAREATYPE"), AddExperiencePartyGlobal(
			"S:Name, S:Area"), AddFamiliar(""), AddGlobals("S:Name, S:Name2"), AddJournalEntry("I:Entry, I:TypeJourType"), AddKit("I:KitKIT"), AddMapNote(
			"P:Position, I:StringRef"), AddSpecialAbility("S:ResRef"), AddSuperKit("I:KitKIT"), AddWayPoint("P:WayPoint"), AddXP2DA("S:Column"), AddXPObject(
			"O:Object, I:XP"), AddexperienceParty("I:XP"), Ally(""), AmbientActivate("O:Object, I:StateBOOLEAN"), AnkhegEmerge(""), AnkhegHide(""), ApplyDamage(
			"O:Object, I:Amount, I:TypeDMGTYPE"), ApplyDamagePercent("O:Object, I:Amount, I:TypeDMGTYPE"), ApplySpell("O:Target, I:SpellSpell"), ApplySpellRES(
			"S:RES, O:Target"), AttachTransitionToDoor("S:GLOBAL, O:Object"), Attack("O:Target"), AttackNoSound("O:Target"), AttackOneRound(
			"O:Target"), AttackReevaluate("O:Target, I:ReevaluationPeriod"), BackStab("O:Target"), BanterBlockFlag("I:StateBoolean"), BanterBlockTime(
			"I:Time"), BashDoor("0:Object"), BattleSong(""), Berserk(""), BreakInstants(""), CallLightning("O:Target"), Calm("O:Object"), ChangeAIScript(
			"S:ScriptFile, I:LevelScrlev"), ChangeAIType("O:Object"), ChangeAlignment("O:Object, I:ValueAlign"), ChangeAnimation("S:ResRef"), ChangeAnimationNoEffect(
			"S:ResRef"), ChangeClass("O:Object, I:ValueClass"), ChangeEnemyAlly("O:Object, I:ValueEA"), ChangeGender("O:Object, I:ValueGender"), ChangeGeneral(
			"O:Object, I:ValueGeneral"), ChangeRace("O:Object, I:ValueRace"), ChangeSpecifics("O:Object, I:ValueSpecific"), ChangeTileState(
			"O:Tile, I:StateBoolean"), ClearActions("O:Object"), ClearAllActions(""), ClickLButtonObject("O:Target, I:ScrollSpeedScroll"), ClickLButtonPoint(
			"P:Target, I:ScrollSpeedScroll"), ClickRButtonObject("O:Target, I:ScrollSpeedScroll"), ClickRButtonPoint("P:Target, I:ScrollSpeedScroll"), CloseDoor(
			"O:Object"), ContainerEnable("O:Object, I:BoolBOOLEAN"), Continue(""), CopyGroundPilesTo("S:ResRef, P:Location"), CreateCreature(
			"S:NewObject, P:Location, I:Face"), CreateCreatureAtLocation("S:GLOBAL, S:Area, S:ResRef"), CreateCreatureCopyPoint(
			"S:ResRef, O:Object, P:Dest"), CreateCreatureDoor("S:NewObject, P:Location, I:Face"), CreateCreatureImpassable(
			"S:NewObject, P:Location, I:Face"), CreateCreatureImpassableAllowOverlap("S:NewObject, P:Location, I:Face"), CreateCreatureObject(
			"S:ResRef, O:Object, I:Usage1, I:Usage2, I:Usage3"), CreateCreatureObjectCopy("S:ResRef, O:Object, I:Usage1, I:Usage2, I:Usage3"), CreateCreatureObjectDoor(
			"S:ResRef, O:Object, I:Usage1, I:Usage2, I:Usage3"), CreateCreatureObjectOffScreen("S:ResRef, O:Object, I:Usage1, I:Usage2, I:Usage3"), CreateCreatureObjectOffset(
			"S:ResRef, O:Object, P:Offset"), CreateCreatureOffScreen("S:ResRef, I:Face"), CreateItem("S:ResRef, I:Usage1, I:Usage2, I:Usage3"), CreateItemGlobal(
			"S:Global, S:Area, S:ResRef"), CreateItemNumGlobal("S:Global, S:Area, S:ResRef"), CreateVisualEffect("S:Object, P:Location"), CreateVisualEffectObject(
			"S:DialogFile, O:Target"), CutAllowScripts("I:BOOLBOOLEAN"), CutSceneId("O:Object"), DayNight("I:TimeOfDayTime"), Deactivate("O:Object"), DeathMatchPositionArea(
			"S:Areaname, P:Dest, I:Player"), DeathMatchPositionGlobal("S:Areaname, P:Dest, I:Player"), DeathMatchPositionLocal(
			"S:Areaname, P:Dest, I:Player"), DemoEnd(""), DestroyAllDestructableEquipment(""), DestroyAllEquipment(""), DestroyGold("I:Gold"), DestroyItem(
			"S:ResRef"), DestroySelf(""), DetectSecretDoor("O:Object"), Dialog("O:Object"), DialogForceInterrupt("O:Object"), DialogInterrupt(
			"I:StateBoolean"), Dialogue("O:Object"), DialogueForceInterrupt("O:Object"), DialogueInterrupt("I:StateBoolean"), DisplayString(
			"O:Object, I:StrRef"), DisplayStringHead("O:Object, I:StrRef"), DisplayStringHeadDead("O:Object, I:StrRef"), DisplayStringHeadOwner(
			"S:Item, I:STRREF"), DisplayStringNoName("O:Object, I:StrRef"), DisplayStringNoNameHead("O:Object, I:StrRef"), DisplayStringWait(
			"O:Object, I:StrRef"), DoubleClickLButtonObject("O:Target, I:ScrollSpeedScroll"), DoubleClickLButtonPoint("P:Target, I:ScrollSpeedScroll"), DoubleClickRButtonObject(
			"O:Target, I:ScrollSpeedScroll"), DoubleClickRButtonPoint("P:Target, I:ScrollSpeedScroll"), DropInventory(""), DropItem(
			"S:Object, P:Location"), EndCredits(""), EndCutSceneMode(""), Enemy(""), EquipItem("S:Object"), EquipMostDamagingMelee(""), EquipRanged(
			""), EraseJournalEntry("I:STRREF"), EscapeArea(""), EscapeAreaDestroy("I:Delay"), EscapeAreaMove("S:Area, I:X, I:Y, I:Face"), EscapeAreaNoSee(
			""), EscapeAreaObject("O:Object"), EscapeAreaObjectMove("S:ResRef, O:Object, I:X, I:Y, I:Face"), ExitPocketPlane(""), Explore(""), Face(
			"I:Direction"), FaceObject("O:Object"), FadeFromColor("P:Point, I:Blue"), FadeToColor("P:Point, I:Blue"), FakeEffectExpiryCheck(
			"O:Object, I:Ticks"), FillSlot("I:SlotSLOTS"), FindTraps(""), FlyToPoint("P:Point, I:time"), Follow("P:Point"), FollowObjectFormation(
			"O:Object, I:Formation, I:Position"), FollowPath(""), ForceSpell("O:Target, I:SpellSpell"), ForceSpellPoint("P:Target, I:SpellSpell"), ForceSpellPointRES(
			"S:RES, P:Target"), ForceSpellPointRange("P:Target, I:SpellSpell"), ForceSpellPointRangeRES("S:RES, P:Target"), ForceSpellRES(
			"S:RES, O:Target"), ForceSpellRange("O:Target, I:SpellSpell"), ForceSpellRangeRES("S:RES, O:Target"), Formation("O:Leader, P:Offset"), GetItem(
			"S:Object, O:Target"), GiveGoldForce("I:Amount"), GiveItem("S:Object, O:Target"), GiveItemCreate(
			"S:ResRef, O:Object, I:Usage1, I:Usage2, I:Usage3"), GiveOrder("O:Object, I:Order"), GivePartyAllEquipment(""), GivePartyGold("I:Amount"), GivePartyGoldGlobal(
			"S:Name, S:Area"), GlobalShout("I:IDSHOUTIDS"), GoToStartScreen(""), GroupAttack("O:Target"), Help(""), Hide(""), HideAreaOnMap(
			"S:ResRef"), HideGUI(""), IncMoraleAI("I:MoraleMoraleAI"), IncrementChapter("S:RESREF"), IncrementGlobal("S:Name, S:Area, I:Value"), Interact(
			"O:Object"), JoinParty(""), JumpToPoint("P:Target"), Kill("O:Object"), LayHands("O:Target"), Leader("P:Point"), LeaveArea(
			"S:Area, P:Point, I:Face"), LeaveAreaLUA("S:Area, S:Parchment, P:Point, I:Face"), LeaveAreaLUAEntry("S:Area, S:Entry, P:Point, I:Face"), LeaveAreaLUAPanic(
			"S:Area, S:Parchment, P:Point, I:Face"), LeaveAreaLUAPanicEntry("S:Area, S:Entry, P:Point, I:Face"), LeaveAreaName("I:Target"), LeaveParty(
			""), Lock("O:Object"), LockScroll(""), MakeGlobal(""), MakeUnselectable("I:Time"), MoraleDec("O:Target, I:Morale"), MoraleInc(
			"O:Target, I:Morale"), MoraleSet("O:Target, I:Morale"), MoveBetweenAreas("S:Area, P:Location, I:Face"), MoveBetweenAreasEffect(
			"S:Area, S:Graphic, P:Location, I:Face"), MoveCursorPoint("P:Target, I:ScrollSpeedScroll"), MoveGlobal("S:Area, O:Object, P:Point"), MoveGlobalObject(
			"O:Object, O:Target"), MoveGlobalObjectOffScreen("O:Object, O:Target"), MoveGlobalsTo("S:FromArea, S:ToArea, P:Location"), MoveToCenterOfScreen(
			"I:NotInterruptableFor"), MoveToExpansion(""), MoveToObject("O:Target"), MoveToObjectFollow("O:Object"), MoveToObjectNoInterrupt(
			"O:Object"), MoveToOffset("P:Offset"), MoveToPoint("P:Point"), MoveToPointNoInterrupt("P:Point"), MoveToPointNoRecticle("P:Point"), MoveToSavedLocation(
			"S:GLOBAL, S:Area"), MoveToSavedLocationn("S:GLOBAL, S:Area"), MoveViewObject("O:Target, I:ScrollSpeedScroll"), MoveViewPoint(
			"P:Target, I:ScrollSpeedScroll"), MultiPlayerSync(""), NIDSpecial1(""), NoAction(""), OpenDoor("O:Object"), Panic(""), PauseGame(""), PickLock(
			"O:Object"), PickPockets("O:Target"), PickUpItem("S:ResRef"), PlayDead("I:Time"), PlayDeadInterruptable("I:Time"), PlayDeadInterruptible(
			"I:Time"), PlaySong("I:Song"), PlaySound("S:Sound"), PlayerDialog("O:Target"), PlayerDialogue("O:Target"), Polymorph(
			"I:AnimationTypeAnimate"), PolymorphCopy("O:Object"), PolymorphCopyBase("O:Object"), ProtectObject("O:Target, I:Range"), ProtectPoint(
			"P:Target, I:Range"), RandomFly(""), RandomTurn(""), RandomWalk(""), RandomWalkContinuous(""), RealSetGlobalTimer(
			"S:Name, S:Area, I:TimeGTimes"), ReallyForceSpell("O:Target, I:SpellSpell"), ReallyForceSpellDead("O:Target, I:SpellSpell"), ReallyForceSpellDeadRES(
			"S:RES, O:Target"), ReallyForceSpellPoint("P:Target, I:SpellSpell"), ReallyForceSpellPointRES("S:RES, P:Target"), ReallyForceSpellRES(
			"S:RES, O:TargetI:SpellSpell"), Recoil(""), RegainPaladinHood(""), RegainRangerHood(""), RemoveAreaFlags("I:TypeAREAFLAG"), RemoveAreaType(
			"I:TypeAREATYPE"), RemoveFamiliar(""), RemoveMapNote("P:Position, I:STRREF"), RemovePaladinHood(""), RemoveRangerHood(""), RemoveSpell(
			"I:SpellSpell"), RemoveSpellRES("S:RES"), RemoveTraps("O:Trap"), ReputationInc("I:Reputation"), ReputationSet("I:Reputation"), Rest(""), RestNoSpells(
			""), RestParty(""), RestorePartyLocations(""), RevealAreaOnMap("S:ResRef"), RunAwayFrom("O:Creature, I:Time"), RunAwayFromNoInterrupt(
			"O:Creature, I:Time"), SG("S:Name, I:Num"), SaveGame("I:Slot"), SaveLocation("S:Area, S:Global, P:Point"), SaveObjectLocation(
			"S:Area, S:Global, O:Object"), ScreenShake("P:Point, I:Duration"), SelectWeaponAbility("I:WeaponNumSlots, I:AbilityNum"), SendTrigger(
			"O:Target, I:TriggerNum"), SetAreaRestFlag("I:CanRest"), SetBeenInPartyFlags(""), SetCursorState("I:BOOLBOOLEAN"), SetCutSceneLite(
			"I:BOOLBOOLEAN"), SetDialog("S:DialogFile"), SetDialogue("S:DialogFile"), SetEncounterProbability("S:FromArea, S:ToArea, I:Probability"), SetGabber(
			"O:Object"), SetGlobal("S:Name, S:Area, I:Value"), SetGlobalTimer("S:Name, S:Area, I:TimeGTimes"), SetHomeLocation("P:Point"), SetInterrupt(
			"I:StateBoolean"), SetLeavePartyDialogFile(""), SetLeavePartyDialogueFile(""), SetMasterArea("S:Name"), SetMoraleAI("I:MoraleMoraleAI"), SetName(
			"I:STRREF"), SetNumTimesTalkedTo("I:Num"), SetPlayerSound("O:Object, I:STRREF, I:SlotNumSNDSLOT"), SetQuestDone("I:STRREF"), SetRestEncounterProbabilityDay(
			"I:Prob"), SetRestEncounterProbabilityNight("I:Prob"), SetSequence("I:SequenceSEQ"), SetToken("S:Token, I:STRREF"), SetTokenGlobal(
			"S:GLOBAL, S:Area, S:Token"), SetTokenObject("S:Token, O:Object"), SetupWish("I:Column, I:Count"), SetupWishObject("O:Creature, I:Count"), Shout(
			"I:IDSHOUTIDS"), SmallWait("I:Time"), SoundActivate("O:Object, I:StateBOOLEAN"), SpawnPtActivate("O:Object"), SpawnPtDeactivate(
			"O:Object"), SpawnPtSpawn("O:Object"), Spell("O:Target, I:SpellSpell"), SpellNoDec("O:Target, I:SpellSpell"), SpellPoint(
			"P:Target, I:SpellSpell"), SpellPointNoDec("P:Target, I:SpellSpell"), SpellPointRES("S:RES, P:Target"), SpellRES("S:RES, O:Target"), StartCombatCounter(
			""), StartCutScene("S:CutScene"), StartCutSceneMode(""), StartDialog("S:DialogFile, O:Target"), StartDialogInterrupt(
			"S:DialogFile, O:Target"), StartDialogNoName("S:DialogFile, O:Target"), StartDialogNoSet("O:Object"), StartDialogNoSetInterrupt(
			"O:Object"), StartDialogOverride("S:DialogFile, O:Target"), StartDialogOverrideInterrupt("S:DialogFile, O:Target"), StartDialogue(
			"S:DialogFile, O:Target"), StartDialogueInterrupt("S:DialogFile, O:Target"), StartDialogueNoSet("O:Object"), StartDialogueNoSetInterrupt(
			"O:Object"), StartMovie("S:ResRef"), StartMusic("I:Slot, I:FlagsMFLAGS"), StartRainNow(""), StartStore("S:Store, O:Target"), StartTimer(
			"I:ID, I:Time"), StateOverrideFlag("I:StateBoolean"), StateOverrideTime("I:Time"), StaticPalette("S:Palette, O:Object"), StaticSequence(
			"O:Object, I:Sequence"), StaticStart("O:Object"), StaticStop("O:Object"), StorePartyLocations(""), Swing(""), SwingOnce(""), TakeItemListParty(
			"S:ResRef"), TakeItemListPartyNum("S:ResRef, I:Num"), TakeItemReplace("S:Give, S:Take, O:Object"), TakePartyGold("I:Amount"), TakePartyItem(
			"S:Item"), TakePartyItemAll("S:Item"), TakePartyItemNum("S:ResRef, I:Num"), TakePartyItemRange("S:Item"), TextScreen("S:TextList"), TriggerActivation(
			"O:Object, I:StateBoolean"), TriggerWalkTo("O:Object"), Turn(""), UndoExplore(""), UnhideGUI(""), Unlock("O:Object"), UnlockScroll(""), UseContainer(
			""), UseDoor(""), UseItem("S:Object, O:Target"), UseItemPoint("S:Item, P:Target, I:Ability"), UseItemPointSlot("P:Target, I:Slot"), UseItemSlot(
			"O:Target, I:Slot"), UseItemSlotAbility("O:Target, I:Slot, I:Ability"), VEquip("I:item"), VerbalConstant("O:Object, I:ConstantsoundOff"), VerbalConstantHead(
			"O:Object, I:ConstantsoundOff"), Wait("I:Time"), WaitWait("I:Time"), Weather("I:WeatherWeather");

	private String params;

	private ActionEnum(final String params) {
		this.params = params;
	}

	public String getParams() {
		return params;
	}

	public void setParams(final String params) {
		this.params = params;
	}

	public static ActionEnum fromString(final String text) throws GeneratorException {
		if (text != null) {
			for (ActionEnum value : ActionEnum.values()) {
				if (text.trim().equalsIgnoreCase(value.toString())) {
					return value;
				}
			}
		}
		throw new GeneratorException("Unknown action: " + text);
	}

}
