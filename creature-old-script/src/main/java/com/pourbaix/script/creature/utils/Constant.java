package com.pourbaix.script.creature.utils;

public interface Constant {

	public static final String CR = "\r\n";
	public static final String TAB = "\t";

	public static final String CONFIG_FILE = "config.ini";
	public static final String XLS_CONFIG_FILE = "general.xls";
	public static final String SCRIPT_EXT = ".TXT";

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

	public interface Keyword {
		public static final String IF = "IF";
		public static final String THEN = "THEN";
		public static final String RESPONSE = "RESPONSE";
		public static final String END = "END";
	}

	interface Global {
		public static final String ROUND = "ROUND";
		public static final String DISABLE_SPELLCASTING = "DISABLE_SPELLCASTING";
		public static final String MELEE = "MELEE";
		public static final String ATTACK = "ATTACK";
		public static final String SHAPESHIFT = "SHAPESHIFT";
	}

}
