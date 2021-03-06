package com.pourbaix.infinity.domain;


public enum OpcodeEnum {
	AC_VS_DAMAGE_TYPE_MODIFIER(0), ATTACKS_PER_ROUND_MODIFIER(1), CURE_SLEEP(2), BERSERK(3), CURE_BERSERK(4), CHARM_CREATURE(5), CHARISMA_MODIFIER(6), CONSTITUTION_MODIFIER(
			10), CURE_POISON(11), DAMAGE(12), INSTANT_DEATH(13), DEXTERITY_MODIFIER(15), HASTE(16), CURRENT_HP_MODIFIER(17), MAXIMUM_HP_MODIFIER(18), INTELLIGENCE_MODIFIER(
			19), INVISIBILITY(20), RESET_MORALE(23), PANIC(24), POISON(25), REMOVE_CURSE(26), ACID_RESISTANCE_MODIFIER(27), COLD_RESISTANCE_MODIFIER(28), ELECTRICITY_RESISTANCE_MODIFIER(
			29), FIRE_RESISTANCE_MODIFIER(30), MAGIC_DAMAGE_RESISTANCE_MODIFIER(31), RAISE_DEAD(32), SAVE_VS_DEATH_MODIFIER(33), SAVE_VS_WAND_MODIFIER(34), SAVE_VS_POLYMORPH_MODIFIER(
			35), SAVE_VS_BREATH_MODIFIER(36), SAVE_VS_SPELL_MODIFIER(37), SILENCE(38), SLEEP(39), SLOW(40), STONE_TO_FLESH(43), STRENGTH_MODIFIER(44), STUN(45), CURE_STUN(
			46), REMOVE_INVISIBILITY(47), CURE_SILENCE(48), WISDOM_BONUS(49), ANIMATION_CHANGE(53), BASE_THAC0_BONUS(54), SLAY(55), DISPEL_EFFECTS(58), MOVE_SILENTLY_BONUS(
			59), CASTING_FAILURE(60), INFRAVISION(63), REMOVE_INFRAVISION(64), BLUR(65), SUMMON_CREATURE(67), UNSUMMON_CREATURE(68), NONDETECTION(69), REMOVE_NONDETECTION(
			70), SET_IDS_STATE(72), ATTACK_DAMAGE_BONUS(73), BLINDNESS(74), CURE_BLINDNESS(75), FEEBLEMINDEDNESS(76), CURE_FEEBLEMINDEDNESS(77), DISEASE(78), CURE_DISEASE(
			79), DEAFNESS(80), CURE_DEAFNESS(81), SET_AI_SCRIPT(82), IMMUNITY_TO_PROJECTILE(83), MAGICAL_FIRE_RESISTANCE_BONUS(84), MAGICAL_COLD_RESISTANCE_BONUS(
			85), SLASHING_RESISTANCE_BONUS(86), CRUSHING_RESISTANCE_BONUS(87), PIERCING_RESISTANCE_BONUS(88), MISSILE_RESISTANCE_BONUS(89), OPEN_LOCKS_BONUS(90), FIND_TRAPS_BONUS(
			91), PICK_POCKETS_BONUS(92), FATIGUE_BONUS(93), CHANGE_LEVEL(96), EXCEPTIONAL_STRENGTH_BONUS(97), REGENERATION(98), PROTECTION_FROM_CREATURE_TYPE(
			100), IMMUNITY_TO_EFFECT(101), IMMUNITY_TO_SPELL_LEVEL(102), MORALE_BREAK(106), PARALYZE(109), CREATE_WEAPON(111), REMOVE_ITEM(112), DETECT_ALIGNMENT(
			115), DETECT_INVISIBLE(116), CLAIRVOYANCE(117), SHOW_CREATURES(118), MIRROR_IMAGE(119), IMMUNITY_TO_WEAPONS(120), CREATE_INVENTORY_ITEM(122), REMOVE_INVENTORY_ITEM(
			123), TELEPORT(124), UNLOCK(125), MOVEMENT_RATE_BONUS(126), SUMMON_MONSTERS(127), CONFUSION(128), AID(129), BLESS(130), CHANT(131), DRAW_UPON_HOLY_MIGHT(
			132), LUCK(133), PETRIFICATION(134), POLYMORPH(135), FORCE_VISIBLE(136), BAD_CHANT(137), DISPLAY_PORTRAIT_ICON(142), DISABLE_BUTTON(144), DISABLE_SPELLCASTING(
			145), CAST_SPELL(146), CAST_SPELL_AT_POINT(148), FIND_TRAPS(150), REPLACE_SELF(151), SANCTUARY(153), ENTANGLE_OVERLAY(154), MINOR_GLOBE_OVERLAY(155), WEB_EFFECT(
			157), GREASE_OVERLAY(158), MIRROR_IMAGE_EFFECT(159), REMOVE_SANCTUARY(160), REMOVE_FEAR(161), REMOVE_PARALYSIS(162), FREE_ACTION(163), PAUSE_CASTER(
			165), MAGIC_RESISTANCE_BONUS(166), MISSILE_THAC0_BONUS(167), REMOVE_CREATURE(168), PREVENT_PORTRAIT_ICON(169), GIVE_INNATE_ABILITY(171), REMOVE_SPELL(
			172), POISON_RESISTANCE_BONUS(173), HOLD_CREATURE(175), MOVEMENT_RATE_BONUS_2(176), USE_EFF_FILE(177), THAC0_VS_TYPE_BONUS(178), DAMAGE_VS_TYPE_BONUS(
			179), DISALLOW_ITEM(180), DISALLOW_ITEM_TYPE(181), NO_COLLISION_DETECTION(184), HOLD_CREATURE_2(185), MOVE_CREATURE(186), CASTING_LEVEL_BONUS(191), INVISIBILITY_DETECTION(
			193), IGNORE_DIALOGUE_PAUSE(194), DRAIN_HP_ON_DEATH(195), FAMILIAR(196), PHYSICAL_MIRROR(197), REFLECT_SPECIFIED_EFFECT(198), REFLECT_SPELL_LEVEL(
			199), SPELL_TURNING(200), SPELL_DEFLECTION(201), REFLECT_SPELL_SCHOOL(202), REFLECT_SPELL_TYPE(203), PROTECTION_FROM_SPELL_SCHOOL(204), PROTECTION_FROM_SPELL_TYPE(
			205), PROTECTION_FROM_SPELL(206), REFLECT_SPECIFIED_SPELL(207), MINIMUM_HP(208), POWER_WORD_KILL(209), POWER_WORD_STUN(210), IMPRISONMENT(211), FREEDOM(
			212), MAZE(213), SELECT_SPELL(214), PLAY_VISUAL_EFFECT(215), LEVEL_DRAIN(216), POWER_WORD_SLEEP(217), STONESKIN_EFFECT(218), ATTACK_ROLL_PENALTY(
			219), REMOVE_SPELL_SCHOOL_PROTECTIONS(220), REMOVE_SPELL_TYPE_PROTECTIONS(221), TELEPORT_FIELD(222), SPELL_SCHOOL_DEFLECTION(223), RESTORATION(224), DETECT_MAGIC(
			225), SPELL_TYPE_DEFLECTION(226), SPELL_SCHOOL_TURNING(227), SPELL_TYPE_TURNING(228), REMOVE_PROTECTION_BY_SCHOOL(229), REMOVE_PROTECTION_BY_TYPE(
			230), TIME_STOP(231), CAST_SPELL_ON_CONDITION(232), MODIFY_PROFICIENCIES(233), CREATE_CONTINGENCY(234), WING_BUFFET(235), PROJECT_IMAGE(236), SET_IMAGE_TYPE(
			237), DISINTEGRATE(238), FARSIGHT(239), REMOVE_PORTRAIT_ICON(240), CONTROL_CREATURE(241), CURE_CONFUSION(242), DRAIN_ITEM_CHARGES(243), DRAIN_WIZARD_SPELLS(
			244), CHECK_FOR_BERSERK(245), BERSERK_EFFECT(246), ATTACK_NEAREST_CREATURE(247), MELEE_HIT_EFFECT(248), RANGED_HIT_EFFECT(249), MAXIMUM_DAMAGE_EACH_HIT(
			250), CHANGE_BARD_SONG(251), SET_TRAP(252), SET_AUTOMAP_NOTE(253), REMOVE_AUTOMAP_NOTE(254), CREATE_ITEM(255), SPELL_SEQUENCER(256), CREATE_SPELL_SEQUENCER(
			257), ACTIVATE_SPELL_SEQUENCER(258), SPELL_TRAP(259), WONDROUS_RECALL(261), VISUAL_RANGE_BONUS(262), BACKSTAB_BONUS(263), DROP_ITEM(264), REMOVE_PROTECTION_FROM_SPELL(
			266), DISABLE_DISPLAY_STRING(267), CLEAR_FOG_OF_WAR(268), UNPAUSE_CASTER(270), ZONE_OF_SWEET_AIR(273), PHASE(274), HIDE_IN_SHADOWS_BONUS(275), DETECT_ILLUSIONS_BONUS(
			276), SET_TRAPS_BONUS(277), THAC0_BONUS(278), ENABLE_BUTTON(279), WILD_MAGIC(280), WILD_SURGE_BONUS(281), MODIFY_SCRIPT_STATE(282), USE_EFF_FILE_AS_CURSE(
			283), MELEE_THAC0_BONUS(284), MELEE_WEAPON_DAMAGE_BONUS(285), MISSILE_WEAPON_DAMAGE_BONUS(286), FIST_THAC0_BONUS(288), FIST_DAMAGE_BONUS(289), IMMUNITY_TO_BACKSTAB(
			292), IMMUNITY_TO_SPECIFIC_ANIMATION(296), IMMUNITY_TO_TURN_UNDEAD(297), CRITICAL_HIT_MODIFIER(301), BACKSTAB_EVERY_HIT(303), MASS_RAISE_DEAD(304), OFF_HAND_THAC0_MODIFIER(
			305), MAIN_HAND_THAC0_MODIFIER(306), IMMUNITY_TO_TIME_STOP(310), WISH(311), GOLEM_STONESKIN(314), REMOVE_ANIMATION(315), REST(316), HASTE_2(317), RESTRICT_ITEM(
			319), REMOVE_EFFECTS_BY_RESOURCE(321);

	private int id;

	private OpcodeEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
