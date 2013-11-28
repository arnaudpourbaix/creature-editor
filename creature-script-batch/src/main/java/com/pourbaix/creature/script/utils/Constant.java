package com.pourbaix.creature.script.utils;

public interface Constant {

	public static final String CARRIAGE_RETURN = "\r\n";
	public static final String TABULATION = "\t";
	public static final String GENERATED_INSTRUCTION_END_CHAR = ";";

	public static final String SCRIPT_EXT = ".TXT";

	public interface Comment {
		public static final String SINGLE_LINE = "//";
		public static final String MULTI_LINE_START = "/*";
		public static final String MULTI_LINE_END = "*/";
	}
	
	public interface BlockKeyword {
		public static final String IF = "IF";
		public static final String THEN = "THEN";
		public static final String RESPONSE = "RESPONSE";
		public static final String END = "END";
	}

	public interface ConfigKeyword {
		public static final String CONFIG = "CONFIG";
	}

	public interface TriggerParam {
		public static final String OBJECT = "O:Object";
		public static final String OR_COUNT = "I:OrCount";
		public static final String DELAY = "I:Delay";
		public static final String NUM = "I:Num";
		public static final String NUMBER = "I:Number";
		public static final String AMOUNT = "I:Amount";
		public static final String AREA_TYPE = "I:NumberAREATYPE";
		public static final String AREA = "S:Area";
		public static final String STYLE = "I:StyleAStyles";
		public static final String TYPE_DAMAGE = "I:DameTypeDamages";
		public static final String SHOUT = "I:IDSHOUTIDS";
		public static final String ALIGNMENT = "I:AlignmentAlign";
		public static final String ALLEGIENCE = "I:AllegienceEA";
		public static final String CLASS = "I:ClassClass";
		public static final String GENDER = "I:SexGender";
		public static final String KIT = "I:KitKIT";
		public static final String GENERAL = "I:GeneralGeneral";
		public static final String RACE = "I:RaceRace";
		public static final String SPECIFIC = "I:SpecificsSpecific";
		public static final String LEVEL = "I:Level";
		public static final String STATE_MODAL = "I:StateMODAL";
		public static final String STAT = "I:StatNumStats";
		public static final String STATE = "I:StateState";
		public static final String VALUE = "I:Value";
		public static final String HIT_POINTS = "I:Hit Points";
		public static final String RANGE = "I:Range";
		public static final String NAME = "S:Name";
		public static final String NAME1 = "S:Name1";
		public static final String NAME2 = "S:Name2";
		public static final String RESOURCE = "S:ResRef";
		public static final String IDENTIFICATEUR_SPELL = "I:SpellSpell";
		public static final String SPELL = "S:Spell";
		public static final String SLOT = "I:Slot";
	}

	public interface Action {
		public static final String RES = "RES";
	}

	public interface ActionParam {
		public static final String TARGET = "O:Target";
		public static final String SPELL = "I:SpellSpell";
		public static final String RESOURCE = "S:RES";
	}

	interface Global {
		public static final String ROUND = "ROUND";
		public static final String DISABLE_SPELLCASTING = "DISABLE_SPELLCASTING";
		public static final String MELEE = "MELEE";
		public static final String ATTACK = "ATTACK";
		public static final String SHAPESHIFT = "SHAPESHIFT";
	}

}
