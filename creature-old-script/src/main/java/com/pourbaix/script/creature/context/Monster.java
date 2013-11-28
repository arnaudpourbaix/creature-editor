package com.pourbaix.script.creature.context;

public class Monster {
	private String race;
	private String classe;
	private boolean isSmart;
	private boolean isSummon;
	private boolean canTrack;
	private boolean canAttack;
	private boolean canRandomWalk;
	private boolean canHideInShadows;
	private boolean canFightWithMeleeAndRange;
	private boolean canTeleport;
	
	public Monster() {
		isSummon = false;
		isSmart = false;
		canFightWithMeleeAndRange = false;
		canRandomWalk = false;
		canTeleport = false;
		canTrack = true;
		canAttack = true;
	}
	
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race = race;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public boolean isSmart() {
		return isSmart;
	}
	public void setSmart(boolean isSmart) {
		this.isSmart = isSmart;
	}
	public boolean isCanFightWithMeleeAndRange() {
		return canFightWithMeleeAndRange;
	}
	public void setCanFightWithMeleeAndRange(boolean canFightWithMeleeAndRange) {
		this.canFightWithMeleeAndRange = canFightWithMeleeAndRange;
	}

	public boolean isSummon() {
		return isSummon;
	}

	public void setSummon(boolean isSummon) {
		this.isSummon = isSummon;
	}

	public boolean isCanRandomWalk() {
		return canRandomWalk;
	}

	public void setCanRandomWalk(boolean canRandomWalk) {
		this.canRandomWalk = canRandomWalk;
	}

	public boolean isCanHideInShadows() {
		return canHideInShadows;
	}

	public void setCanHideInShadows(boolean canHideInShadows) {
		this.canHideInShadows = canHideInShadows;
	}

	public boolean isCanTeleport() {
		return canTeleport;
	}

	public void setCanTeleport(boolean canTeleport) {
		this.canTeleport = canTeleport;
	}

	public boolean isCanTrack() {
		return canTrack;
	}

	public void setCanTrack(boolean canTrack) {
		this.canTrack = canTrack;
	}

	public boolean isCanAttack() {
		return canAttack;
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}
	
}
