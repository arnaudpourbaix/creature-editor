package com.pourbaix.infinity.factory;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.AttributeEnum;
import com.pourbaix.creature.editor.domain.AttributeValue;
import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.infinity.datatype.DimensionalArrayEnum;
import com.pourbaix.infinity.datatype.Flag;
import com.pourbaix.infinity.datatype.IdsEnum;
import com.pourbaix.infinity.datatype.ResourceRef;
import com.pourbaix.infinity.datatype.TextString;
import com.pourbaix.infinity.domain.DimensionalArrayFile;
import com.pourbaix.infinity.domain.DimensionalArrayFileException;
import com.pourbaix.infinity.domain.DimensionalArrayRow;
import com.pourbaix.infinity.domain.RawCreature;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.StringResourceException;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.service.GameService;
import com.pourbaix.infinity.util.DynamicArray;

@Service
public class CreatureFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CreatureFactory.class);

	@Resource
	private IdentifierFactory identifierFactory;

	@Resource
	private DimensionalArrayFileFactory dimensionalArrayFileFactory;

	@Resource
	private AttributeFactory attributeFactory;
	
	@Resource
	private GameService gameService; 
	
	private static final String MISSING_CREATURE_FILE = "MISSING_CREATURE_FILE";
	private static final String INVALID_CREATURE_FILE = "INVALID_CREATURE_FILE";
	private static final String INVALID_CREATURE_NAME = "INVALID_CREATURE_NAME";
	private static final String INVALID_CREATURE_TOOLTIP = "INVALID_CREATURE_TOOLTIP";

	private static final String[] FLAGS = { "No flags set", "Identified", "No corpse", "Permanent corpse", "Original class: Fighter", "Original class: Mage",
			"Original class: Cleric", "Original class: Thief", "Original class: Druid", "Original class: Ranger", "Fallen paladin", "Fallen ranger",
			"Export allowed", "Hide status", "Large creature", "Moving between areas", "Been in party", "Holding item", "Clear all flags", "", "", "", "", "",
			"", "Allegiance tracking", "General tracking", "Race tracking", "Class tracking", "Specifics tracking", "Gender tracking", "Alignment tracking",
			"Uninterruptible" };

	public Creature getCreature(String entryName, boolean onlyName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException(MISSING_CREATURE_FILE, entryName);
		}
		return getCreature(entry, onlyName);
	}

	public Creature getCreature(ResourceEntry entry, boolean onlyName) throws FactoryException {
		RawCreature creature = new RawCreature();
		creature.setResource(entry.getResourceName());
		byte buffer[];
		try {
			buffer = entry.getResourceData();
		} catch (IOException e) {
			throw new FactoryException(INVALID_CREATURE_FILE, entry.getResourceName());
		}
		try {
			creature.setName(StringResource.getInstance().getStringRef(buffer, 0x08));
		} catch (StringResourceException e) {
			throw new FactoryException(INVALID_CREATURE_NAME, entry.getResourceName());
		}
		try {
			creature.setTooltip(StringResource.getInstance().getStringRef(buffer, 0x0c));
		} catch (StringResourceException e) {
			throw new FactoryException(INVALID_CREATURE_TOOLTIP, entry.getResourceName());
		}
		if (onlyName) {
			return getCreature(creature, onlyName);
		}
		creature.setFlags(new Flag((long) DynamicArray.getInt(buffer, 0x10), 4, FLAGS));
		creature.setExperienceValue(DynamicArray.getInt(buffer, 0x14));
		creature.setPowerLevelOrExperience(DynamicArray.getInt(buffer, 0x18));
		creature.setGold(DynamicArray.getInt(buffer, 0x1c));
		creature.setStatus(identifierFactory.getFirstValueByKey(IdsEnum.State.getResource(), (long) DynamicArray.getInt(buffer, 0x20)));
		creature.setCurrentHitPoint(DynamicArray.getShort(buffer, 0x24));
		creature.setMaximumHitPoint(DynamicArray.getShort(buffer, 0x26));
		creature.setAnimation(DynamicArray.getInt(buffer, 0x28));
		creature.setAnimationLabel(identifierFactory.getFirstValueByKey(IdsEnum.Animation.getResource(), (long) creature.getAnimation()));
		creature.setEffectFlag(DynamicArray.getByte(buffer, 0x33));
		creature.setReputation((byte) DynamicArray.getUnsignedByte(buffer, 0x44));
		creature.setHideInShadows((byte) DynamicArray.getUnsignedByte(buffer, 0x45));
		creature.setNaturalAC(DynamicArray.getShort(buffer, 0x46));
		creature.setEffectiveAC(DynamicArray.getShort(buffer, 0x48));
		creature.setCrushingAC(DynamicArray.getShort(buffer, 0x4a));
		creature.setMissileAC(DynamicArray.getShort(buffer, 0x4c));
		creature.setPiercingAC(DynamicArray.getShort(buffer, 0x4e));
		creature.setSlashingAC(DynamicArray.getShort(buffer, 0x50));
		creature.setThac0(DynamicArray.getByte(buffer, 0x52));
		creature.setReputation((byte) DynamicArray.getUnsignedByte(buffer, 0x44));
		creature.setAttackNumber((byte) DynamicArray.getByte(buffer, 0x53));
		creature.setSaveDeath((byte) DynamicArray.getByte(buffer, 0x54));
		creature.setSaveWand((byte) DynamicArray.getByte(buffer, 0x55));
		creature.setSavePolymorph((byte) DynamicArray.getByte(buffer, 0x56));
		creature.setSaveBreath((byte) DynamicArray.getByte(buffer, 0x57));
		creature.setSaveSpell((byte) DynamicArray.getByte(buffer, 0x58));
		creature.setResistFire((byte) DynamicArray.getByte(buffer, 0x59));
		creature.setResistCold((byte) DynamicArray.getByte(buffer, 0x5a));
		creature.setResistElectricity((byte) DynamicArray.getByte(buffer, 0x5b));
		creature.setResistAcid((byte) DynamicArray.getByte(buffer, 0x5c));
		creature.setResistMagic((byte) DynamicArray.getByte(buffer, 0x5d));
		creature.setResistMagicFire((byte) DynamicArray.getByte(buffer, 0x5e));
		creature.setResistMagicCold((byte) DynamicArray.getByte(buffer, 0x5f));
		creature.setResistSlashing((byte) DynamicArray.getByte(buffer, 0x60));
		creature.setResistCrushing((byte) DynamicArray.getByte(buffer, 0x61));
		creature.setResistPiercing((byte) DynamicArray.getByte(buffer, 0x62));
		creature.setResistMissile((byte) DynamicArray.getByte(buffer, 0x63));
		creature.setDetectIllusion((byte) DynamicArray.getUnsignedByte(buffer, 0x64));
		creature.setSetTraps((byte) DynamicArray.getUnsignedByte(buffer, 0x65));
		creature.setLore((byte) DynamicArray.getByte(buffer, 0x66));
		creature.setLockpicking((byte) DynamicArray.getUnsignedByte(buffer, 0x67));
		creature.setStealth((byte) DynamicArray.getUnsignedByte(buffer, 0x68));
		creature.setDisarmTraps((byte) DynamicArray.getUnsignedByte(buffer, 0x69));
		creature.setPickPocket((byte) DynamicArray.getUnsignedByte(buffer, 0x6a));
		creature.setFatigue((byte) DynamicArray.getByte(buffer, 0x6b));
		creature.setIntoxication((byte) DynamicArray.getByte(buffer, 0x6c));
		creature.setLuck((byte) DynamicArray.getByte(buffer, 0x6d));
		creature.setLargeSwordProficiency((byte) DynamicArray.getByte(buffer, 0x6e));
		creature.setSmallSwordProficiency((byte) DynamicArray.getByte(buffer, 0x6f));
		creature.setBowProficiency((byte) DynamicArray.getByte(buffer, 0x70));
		creature.setSpearProficiency((byte) DynamicArray.getByte(buffer, 0x71));
		creature.setBluntProficiency((byte) DynamicArray.getByte(buffer, 0x72));
		creature.setSpikedProficiency((byte) DynamicArray.getByte(buffer, 0x73));
		creature.setAxeProficiency((byte) DynamicArray.getByte(buffer, 0x74));
		creature.setMissileProficiency((byte) DynamicArray.getByte(buffer, 0x75));
		creature.setTurnUndeadLevel((byte) DynamicArray.getByte(buffer, 0x82));
		creature.setTrackingSkill((byte) DynamicArray.getByte(buffer, 0x83));
		creature.setTrackingTarget(new TextString(buffer, 0x84, 32, "").toString());
		creature.setLevel1((byte) DynamicArray.getByte(buffer, 0x234));
		creature.setLevel2((byte) DynamicArray.getByte(buffer, 0x235));
		creature.setLevel3((byte) DynamicArray.getByte(buffer, 0x236));
		creature.setSex(DynamicArray.getUnsignedByte(buffer, 0x237));
		creature.setSexLabel(identifierFactory.getFirstValueByKey(IdsEnum.Sex.getResource(), (long) creature.getSex()));
		creature.setStrength((byte) DynamicArray.getByte(buffer, 0x238));
		creature.setStrengthBonus((byte) DynamicArray.getByte(buffer, 0x239));
		creature.setIntelligence((byte) DynamicArray.getByte(buffer, 0x23a));
		creature.setWisdom((byte) DynamicArray.getByte(buffer, 0x23b));
		creature.setDexterity((byte) DynamicArray.getByte(buffer, 0x23c));
		creature.setConstitution((byte) DynamicArray.getByte(buffer, 0x23d));
		creature.setCharisma((byte) DynamicArray.getByte(buffer, 0x23e));
		creature.setMorale((byte) DynamicArray.getByte(buffer, 0x23f));
		creature.setMoraleBreak((byte) DynamicArray.getByte(buffer, 0x240));
		creature.setRacialEnemy(DynamicArray.getUnsignedByte(buffer, 0x241));
		creature.setRacialEnemyLabel(identifierFactory.getFirstValueByKey(IdsEnum.Race.getResource(), (long) creature.getRacialEnemy()));
		creature.setMoraleRecoveryTime(DynamicArray.getShort(buffer, 0x242));
		creature.setScriptOverride(new ResourceRef(buffer, 0x248, "", "BCS").getResourceName());
		creature.setScriptClass(new ResourceRef(buffer, 0x250, "", "BCS").getResourceName());
		creature.setScriptRace(new ResourceRef(buffer, 0x258, "", "BCS").getResourceName());
		creature.setScriptGeneral(new ResourceRef(buffer, 0x260, "", "BCS").getResourceName());
		creature.setScriptDefault(new ResourceRef(buffer, 0x268, "", "BCS").getResourceName());
		creature.setAllegiance(DynamicArray.getUnsignedByte(buffer, 0x270));
		creature.setAllegianceLabel(identifierFactory.getFirstValueByKey(IdsEnum.Allegiance.getResource(), (long) creature.getAllegiance()));
		creature.setGeneral(DynamicArray.getUnsignedByte(buffer, 0x271));
		creature.setGeneralLabel(identifierFactory.getFirstValueByKey(IdsEnum.General.getResource(), (long) creature.getGeneral()));
		creature.setRace(DynamicArray.getUnsignedByte(buffer, 0x272));
		creature.setRaceLabel(identifierFactory.getFirstValueByKey(IdsEnum.Race.getResource(), (long) creature.getRace()));
		creature.setClasse(DynamicArray.getUnsignedByte(buffer, 0x273));
		creature.setClassLabel(identifierFactory.getFirstValueByKey(IdsEnum.Class.getResource(), (long) creature.getClasse()));
		creature.setSpecific(DynamicArray.getUnsignedByte(buffer, 0x274));
		creature.setSpecificLabel(identifierFactory.getFirstValueByKey(IdsEnum.Specific.getResource(), (long) creature.getSpecific()));
		creature.setGender(DynamicArray.getUnsignedByte(buffer, 0x275));
		creature.setGenderLabel(identifierFactory.getFirstValueByKey(IdsEnum.Gender.getResource(), (long) creature.getGender()));
		creature.setAlignment(DynamicArray.getUnsignedByte(buffer, 0x27b));
		creature.setAlignmentLabel(identifierFactory.getFirstValueByKey(IdsEnum.Alignment.getResource(), (long) creature.getAlignment()));
		creature.setGlobalIdentifier(DynamicArray.getShort(buffer, 0x27c));
		creature.setLocalIdentifier(DynamicArray.getShort(buffer, 0x27e));
		creature.setDeathVariable(new TextString(buffer, 0x280, 32, "").toString());
		creature.setDialogFile(new ResourceRef(buffer, 0x2cc, "", "DLG").getResourceName());

		parseKit(creature, buffer);

		return getCreature(creature, onlyName);
	}

	private Creature getCreature(RawCreature rawCreature, boolean onlyName) {
		Creature creature = new Creature();
		creature.setGame(gameService.getGame());
		creature.setResource(rawCreature.getResource().replace(".CRE", ""));
		addAttribute(creature, rawCreature, AttributeEnum.NAME, rawCreature.getName());
		addAttribute(creature, rawCreature, AttributeEnum.TOOLTIP, rawCreature.getTooltip());
		if (onlyName) {
			return creature;
		}
		addAttribute(creature, rawCreature, AttributeEnum.FLAGS, rawCreature.getFlags().getValue());
		addAttribute(creature, rawCreature, AttributeEnum.XP_VALUE, rawCreature.getExperienceValue());
		addAttribute(creature, rawCreature, AttributeEnum.XP, rawCreature.getPowerLevelOrExperience());
		addAttribute(creature, rawCreature, AttributeEnum.GOLD, rawCreature.getGold());
		addAttribute(creature, rawCreature, AttributeEnum.STATUS, rawCreature.getStatus());
		addAttribute(creature, rawCreature, AttributeEnum.CURRENT_HP, rawCreature.getCurrentHitPoint());
		addAttribute(creature, rawCreature, AttributeEnum.MAXIMUM_HP, rawCreature.getMaximumHitPoint());
		addAttribute(creature, rawCreature, AttributeEnum.ANIMATION, rawCreature.getAnimation());
		addAttribute(creature, rawCreature, AttributeEnum.REPUTATION, rawCreature.getReputation());
		addAttribute(creature, rawCreature, AttributeEnum.HIDE_IN_SHADOWS, rawCreature.getHideInShadows());
		addAttribute(creature, rawCreature, AttributeEnum.NATURAL_ARMOR_CLASS, rawCreature.getNaturalAC());
		addAttribute(creature, rawCreature, AttributeEnum.EFFECTIVE_ARMOR_CLASS, rawCreature.getEffectiveAC());
		addAttribute(creature, rawCreature, AttributeEnum.CRUSHING_ARMOR_CLASS, rawCreature.getCrushingAC());
		addAttribute(creature, rawCreature, AttributeEnum.MISSILE_ARMOR_CLASS, rawCreature.getMissileAC());
		addAttribute(creature, rawCreature, AttributeEnum.PIERCING_ARMOR_CLASS, rawCreature.getPiercingAC());
		addAttribute(creature, rawCreature, AttributeEnum.SLASHING_ARMOR_CLASS, rawCreature.getSlashingAC());
		addAttribute(creature, rawCreature, AttributeEnum.THAC0, rawCreature.getThac0());
		addAttribute(creature, rawCreature, AttributeEnum.NUMBER_OF_ATTACKS, rawCreature.getAttackNumber());
		addAttribute(creature, rawCreature, AttributeEnum.SAVE_VERSUS_DEATH, rawCreature.getSaveDeath());
		addAttribute(creature, rawCreature, AttributeEnum.SAVE_VERSUS_WANDS, rawCreature.getSaveWand());
		addAttribute(creature, rawCreature, AttributeEnum.SAVE_VERSUS_POLYMORPH, rawCreature.getSavePolymorph());
		addAttribute(creature, rawCreature, AttributeEnum.SAVE_VERSUS_BREATH, rawCreature.getSaveBreath());
		addAttribute(creature, rawCreature, AttributeEnum.SAVE_VERSUS_SPELLS, rawCreature.getSaveSpell());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_FIRE, rawCreature.getResistFire());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_COLD, rawCreature.getResistCold());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_ELECTRICITY, rawCreature.getResistElectricity());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_ACID, rawCreature.getResistAcid());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_MAGIC, rawCreature.getResistMagic());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_MAGIC_FIRE, rawCreature.getResistMagicFire());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_MAGIC_COLD, rawCreature.getResistMagicCold());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_SLASHING, rawCreature.getResistSlashing());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_CRUSHING, rawCreature.getResistCrushing());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_PIERCING, rawCreature.getResistPiercing());
		addAttribute(creature, rawCreature, AttributeEnum.RESIST_MISSILE, rawCreature.getResistMissile());
		addAttribute(creature, rawCreature, AttributeEnum.DETECT_ILLUSION, rawCreature.getDetectIllusion());
		addAttribute(creature, rawCreature, AttributeEnum.SET_TRAPS, rawCreature.getSetTraps());
		addAttribute(creature, rawCreature, AttributeEnum.LORE, rawCreature.getLore());
		addAttribute(creature, rawCreature, AttributeEnum.LOCKPICKING, rawCreature.getLockpicking());
		addAttribute(creature, rawCreature, AttributeEnum.STEALTH, rawCreature.getStealth());
		addAttribute(creature, rawCreature, AttributeEnum.FIND_DISARM_TRAPS, rawCreature.getDisarmTraps());
		addAttribute(creature, rawCreature, AttributeEnum.PICK_POCKETS, rawCreature.getPickPocket());
		addAttribute(creature, rawCreature, AttributeEnum.FATIGUE, rawCreature.getFatigue());
		addAttribute(creature, rawCreature, AttributeEnum.INTOXICATION, rawCreature.getIntoxication());
		addAttribute(creature, rawCreature, AttributeEnum.LUCK, rawCreature.getLuck());
		addAttribute(creature, rawCreature, AttributeEnum.TURN_UNDEAD_LEVEL, rawCreature.getTurnUndeadLevel());
		addAttribute(creature, rawCreature, AttributeEnum.TRACKING_SKILL, rawCreature.getTrackingSkill());
		addAttribute(creature, rawCreature, AttributeEnum.TRACKING_TARGET, rawCreature.getTrackingTarget());
		addAttribute(creature, rawCreature, AttributeEnum.LEVEL1, rawCreature.getLevel1());
		addAttribute(creature, rawCreature, AttributeEnum.LEVEL2, rawCreature.getLevel2());
		addAttribute(creature, rawCreature, AttributeEnum.LEVEL3, rawCreature.getLevel3());
		addAttribute(creature, rawCreature, AttributeEnum.SEX, rawCreature.getSex());
		addAttribute(creature, rawCreature, AttributeEnum.STRENGTH, rawCreature.getStrength());
		addAttribute(creature, rawCreature, AttributeEnum.EXCEPTIONNAL_STRENGTH, rawCreature.getStrengthBonus());
		addAttribute(creature, rawCreature, AttributeEnum.INTELLIGENCE, rawCreature.getIntelligence());
		addAttribute(creature, rawCreature, AttributeEnum.WISDOM, rawCreature.getWisdom());
		addAttribute(creature, rawCreature, AttributeEnum.DEXTERITY, rawCreature.getDexterity());
		addAttribute(creature, rawCreature, AttributeEnum.CONSTITUTION, rawCreature.getConstitution());
		addAttribute(creature, rawCreature, AttributeEnum.CHARISMA, rawCreature.getCharisma());
		addAttribute(creature, rawCreature, AttributeEnum.MORALE, rawCreature.getMorale());
		addAttribute(creature, rawCreature, AttributeEnum.MORALE_BREAK, rawCreature.getMoraleBreak());
		addAttribute(creature, rawCreature, AttributeEnum.RACIAL_ENEMY, rawCreature.getRacialEnemy());
		addAttribute(creature, rawCreature, AttributeEnum.MORALE_RECOVERY_TIME, rawCreature.getMoraleRecoveryTime());
		addAttribute(creature, rawCreature, AttributeEnum.KIT, rawCreature.getKit());
		addAttribute(creature, rawCreature, AttributeEnum.OVERRIDE_SCRIPT, rawCreature.getScriptOverride());
		addAttribute(creature, rawCreature, AttributeEnum.CLASS_SCRIPT, rawCreature.getScriptClass());
		addAttribute(creature, rawCreature, AttributeEnum.RACE_SCRIPT, rawCreature.getScriptRace());
		addAttribute(creature, rawCreature, AttributeEnum.GENERAL_SCRIPT, rawCreature.getScriptGeneral());
		addAttribute(creature, rawCreature, AttributeEnum.DEFAULT_SCRIPT, rawCreature.getScriptDefault());
		addAttribute(creature, rawCreature, AttributeEnum.ALLEGIANCE, rawCreature.getAllegiance());
		addAttribute(creature, rawCreature, AttributeEnum.GENERAL, rawCreature.getGeneral());
		addAttribute(creature, rawCreature, AttributeEnum.RACE, rawCreature.getRace());
		addAttribute(creature, rawCreature, AttributeEnum.CLASS, rawCreature.getClasse());
		addAttribute(creature, rawCreature, AttributeEnum.SPECIFIC, rawCreature.getSpecific());
		addAttribute(creature, rawCreature, AttributeEnum.GENDER, rawCreature.getGender());
		addAttribute(creature, rawCreature, AttributeEnum.ALIGNMENT, rawCreature.getAlignment());
		addAttribute(creature, rawCreature, AttributeEnum.GLOBAL_IDENTIFIER, rawCreature.getGlobalIdentifier());
		addAttribute(creature, rawCreature, AttributeEnum.LOCAL_IDENTIFIER, rawCreature.getLocalIdentifier());
		addAttribute(creature, rawCreature, AttributeEnum.DEATH_VARIABLE, rawCreature.getDeathVariable());
		addAttribute(creature, rawCreature, AttributeEnum.DIALOG_FILE, rawCreature.getDialogFile());
		addAttribute(creature, rawCreature, AttributeEnum.EFFECT_FLAG, rawCreature.getEffectFlag());
		return creature;
	}

	private void addAttribute(Creature creature, RawCreature rawCreature, AttributeEnum attribute, String value) {
		AttributeValue attributeValue = attributeFactory.getAttributeValue(creature, attribute, value, true);
		creature.getAttributeValues().add(attributeValue);
	}

	private void addAttribute(Creature creature, RawCreature rawCreature, AttributeEnum attribute, long value) {
		AttributeValue attributeValue = attributeFactory.getAttributeValue(creature, attribute, value, true);
		creature.getAttributeValues().add(attributeValue);
	}
	
	private void parseKit(RawCreature creature, byte buffer[]) throws FactoryException {
		int offset = 0x244;
		long value;
		if (buffer[offset + 3] == 0x40) {
			value = (long) buffer[offset + 2];
		} else {
			value = (long) (DynamicArray.getUnsignedShort(buffer, offset + 2) + 0x10000 * DynamicArray.getUnsignedShort(buffer, offset));
			if (value < 0) {
				value += 4294967296L;
			}
		}
		if (value == 0) {
			return;
		}
		try {
			DimensionalArrayFile kit2da = dimensionalArrayFileFactory.getDimensionalArray(DimensionalArrayEnum.Kit);
			DimensionalArrayRow row = kit2da.findByColumn("unusable", value);
			if (row != null) {
				creature.setKit(DynamicArray.getInt(buffer, offset));
				creature.setKitLabel(row.getColumns().get(1));
			} else {
				logger.error("no kit found for " + creature.getResource());
			}

		} catch (DimensionalArrayFileException e) {
			throw new FactoryException(e.getCode(), e.getParam());
		}
	}
}
