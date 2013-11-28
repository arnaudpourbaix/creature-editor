package old;
public interface Constant {
	
	public static final String SCRIPT_EXT = ".TXT";

	public static final String CR = "\r\n";
	public static final String TAB = "  ";
	
	public static final Integer[] RANDOMNUM = { 0, 500, 333, 250, 200, 167, 143, 125, 111, 100, 91, 83, 77, 71, 67, 62, 59 };
	
	public static final Integer[] RANDOM_RESPONSE = { 100, 90, 80, 70, 60, 50, 40, 30 };
	
	public static final String[] PLAYERS = { "Player1", "Player2", "Player3", "Player4", "Player5", "Player6" };

	public static final String[] CASTERS = { "MAGE_ALL", "CLERIC_ALL", "DRUID_ALL", "BARD_ALL" };
	
	public static final String[] FIGHTERS = { "FIGHTER_ALL", "PALADIN_ALL", "RANGER_ALL" };
	
	interface SpellAction {
		public static final String SPELL = "Spell";
		public static final String FORCESPELL = "ForceSpell";
		public static final String REALLYFORCESPELL = "ReallyForceSpell";
		public static final String APPLYSPELL = "ApplySpell";
	}

	interface ItemAction {
		public static final String ITEM = "UseItem";
	}
	
	public static final String[] NEAREST = { "NearestEnemyOf(Myself)", "SecondNearestEnemyOf(Myself)", 
		"ThirdNearestEnemyOf(Myself)", "FourthNearestEnemyOf(Myself)", "FifthNearestEnemyOf(Myself)", 
		"SixthNearestEnemyOf(Myself)", "SeventhNearestEnemyOf(Myself)", "EighthNearestEnemyOf(Myself)",
		"NinthNearestEnemyOf(Myself)", "TenthNearestEnemyOf(Myself)" };
	
	public static final String[] NEARESTOFTYPE = { "NearestEnemyOfType(<TYPE>)", "SecondNearestEnemyOfType(<TYPE>)", 
		"ThirdNearestEnemyOfType(<TYPE>)", "FourthNearestEnemyOfType(<TYPE>)", "FifthNearestEnemyOfType(<TYPE>)", 
		"SixthNearestEnemyOfType(<TYPE>)", "SeventhNearestEnemyOfType(<TYPE>)", "EigthNearestEnemyOfType(<TYPE>)",
		"NinthNearestEnemyOfType(<TYPE>)", "TenthNearestEnemyOfType(<TYPE>)" };
	
	interface Condition {
		public static final String GLOBAL = "GLOBAL";
		public static final String TIMER = "TIMER";
		public static final String EXPIREDTIMER = "EXPIREDTIMER";
		public static final String GENERAL = "GENERAL";
		public static final String CLASS = "CLASS";
		public static final String KIT = "KIT";
		public static final String RANDOM = "RANDOM";
		public static final String NUM = "NUM";
		public static final String RANGE = "RANGE";
		public static final String HPLT = "HPLT";
		public static final String HPGT = "HPGT";
		public static final String HPPERCENTLT = "HP%LT";
		public static final String HPPERCENTGT = "HP%GT";
		public static final String LEVELLT = "LEVELLT";
		public static final String LEVELGT = "LEVELGT";
		public static final String STAT = "STAT";
		public static final String STATGT = "STATGT";
		public static final String STATLT = "STATLT";
		public static final String STATE = "STATE";
		public static final String EA = "EA";
		public static final String ALIGN = "ALIGN";
		public static final String DETECT = "DETECT";
		public static final String SEE = "SEE";
		public static final String HAVESPELL = "SPELL";
	}

	interface Allegiance {
		public static final String ALLY = "ALLY";
		public static final String CONTROLLED = "CONTROLLED";
		public static final String CHARMED = "CHARMED";
		public static final String GOODBUTRED = "GOODBUTRED";
		public static final String GOODBUTBLUE = "GOODBUTBLUE";
		public static final String GOODCUTOFF = "GOODCUTOFF";
		public static final String NOTGOOD = "NOTGOOD";
		public static final String ANYTHING = "ANYTHING";
		public static final String NEUTRAL = "NEUTRAL";
		public static final String NOTEVIL = "NOTEVIL";
		public static final String EVILCUTOFF = "EVILCUTOFF";
		public static final String EVILBUTGREEN = "EVILBUTGREEN";
		public static final String EVILBUTBLUE = "EVILBUTBLUE";
		public static final String ENEMY = "ENEMY";
	}
	
	interface ConditionType {
		public static final String ACTION = "ACTION";
		public static final String TARGET = "TARGET";
		public static final String ALL = "ALL";
	}
	
	interface General {
		public static final String HUMANOID = "HUMANOID";
		public static final String UNDEAD = "UNDEAD";
		public static final String ANIMAL = "ANIMAL";
		//public static final String GENERAL_ITEM = "GENERAL_ITEM";
		public static final String WEAPON = "WEAPON";
	}
	
	interface Race {
		public static final String ELF = "ELF";
		public static final String SKELETON = "SKELETON";
		public static final String ELEMENTAL = "ELEMENTAL";
		public static final String SLIME = "SLIME";
		public static final String GOLEM = "GOLEM";
	}

	interface Gender {
		public static final String MALE = "MALE";
		public static final String FEMALE = "FEMALE";
		public static final String OTHER = "OTHER";
		public static final String NIETHER = "NIETHER";
		public static final String BOTH = "BOTH";
		public static final String SUMMONED = "SUMMONED";
		public static final String SUMMONED_DEMON = "SUMMONED_DEMON";
		public static final String ILLUSIONARY = "ILLUSIONARY";
	}
	
	interface Class {
		public static final String MAGE_ALL = "MAGE_ALL";
		public static final String CLERIC_ALL = "CLERIC_ALL";
		public static final String DRUID_ALL = "DRUID_ALL";
		public static final String BARD_ALL = "BARD_ALL";
		
		public static final String FIGHTER_ALL = "FIGHTER_ALL";
		public static final String PALADIN_ALL = "PALADIN_ALL";
		public static final String RANGER_ALL = "RANGER_ALL";
		
		public static final String MAGE = "MAGE";
		public static final String FIGHTER = "FIGHTER";
		public static final String CLERIC = "CLERIC";
		public static final String THIEF = "THIEF";
		public static final String BARD = "BARD";
		public static final String PALADIN = "PALADIN";
		public static final String DRUID = "DRUID";
		public static final String RANGER = "RANGER";
		public static final String SORCERER = "SORCERER";
		public static final String MONK = "MONK";
		public static final String OTYUGH = "OTYUGH";
		public static final String GOLEM_IRON = "GOLEM_IRON";
		public static final String MUSTARD_JELLY = "MUSTARD_JELLY";
		public static final String GENIE_EFREETI = "GENIE_EFREETI";
		public static final String ELEMENTAL_FIRE = "ELEMENTAL_FIRE";
	}

	interface Kit {
		public static final String INQUISITOR = "INQUISITOR";
		public static final String CAVALIER = "CAVALIER";
	}

	interface Affect {
		public static final int ALL = 1;
		public static final int SUMMON = 2;
		public static final int PLAYER = 3;
	}

	interface Sequencer {
		public static final int MINOR_SEQUENCER = 1;
		public static final int SPELL_SEQUENCER = 2;
		public static final int SPELL_TRIGGER = 3;
	}

	interface Contingency {
		public static final int CONTINGENCY = 1;
		public static final int CHAIN_CONTINGENCIES = 2;
	}
	
	interface Variable {
		public static final String ROUND = "JA#ROUND";
		public static final String DISABLE_SPELLCASTING = "JA#DISABLE_SPELLCASTING";
		public static final String MELEE = "JA#MELEE";
		public static final String ATTACK = "JA#ATTACK";
		public static final String SHAPESHIFT = "JA#SHAPESHIFT";
	}
	
}
