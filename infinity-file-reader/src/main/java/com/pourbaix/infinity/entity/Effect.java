package com.pourbaix.infinity.entity;

import com.pourbaix.infinity.datatype.Flag;
import com.pourbaix.infinity.datatype.ResistanceEnum;
import com.pourbaix.infinity.datatype.TargetTypeEnum;
import com.pourbaix.infinity.datatype.TimingEnum;

public class Effect {

	private short opcode;
	private int param1;
	private int param2;
	private byte power;
	private TargetTypeEnum target;
	private TimingEnum timing;
	private ResistanceEnum resistance;
	private int duration;
	private byte probability1;
	private byte probability2;
	private String resource;
	private short diceThrown;
	private short diceSides;
	private Flag savingThrowType;
	private short savingThrowBonus;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("target: ").append(target.getLabel());
		sb.append(", param1: ").append(param1);
		sb.append(", param2: ").append(param2);
		sb.append(", power: ").append(power);
		sb.append(", timing: ").append(timing.getLabel());
		sb.append(", resistance: ").append(resistance.getLabel());
		sb.append(", duration: ").append(duration);
		sb.append(", probability 1: ").append(probability1);
		sb.append(", probability 2: ").append(probability2);
		sb.append(", resource: ").append(resource);
		sb.append(", diceThrown: ").append(diceThrown);
		sb.append(", diceSides: ").append(diceSides);
		sb.append(", savingThrowType: ").append(savingThrowType);
		sb.append(", savingThrowBonus: ").append(savingThrowBonus);
		return sb.toString();
	}

	public TargetTypeEnum getTarget() {
		return target;
	}

	public void setTarget(TargetTypeEnum target) {
		this.target = target;
	}

	public TimingEnum getTiming() {
		return timing;
	}

	public void setTiming(TimingEnum timing) {
		this.timing = timing;
	}

	public ResistanceEnum getResistance() {
		return resistance;
	}

	public void setResistance(ResistanceEnum resistance) {
		this.resistance = resistance;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public byte getProbability1() {
		return probability1;
	}

	public void setProbability1(byte probability1) {
		this.probability1 = probability1;
	}

	public byte getProbability2() {
		return probability2;
	}

	public void setProbability2(byte probability2) {
		this.probability2 = probability2;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public short getDiceThrown() {
		return diceThrown;
	}

	public void setDiceThrown(short diceThrown) {
		this.diceThrown = diceThrown;
	}

	public short getDiceSides() {
		return diceSides;
	}

	public void setDiceSides(short diceSides) {
		this.diceSides = diceSides;
	}

	public Flag getSavingThrowType() {
		return savingThrowType;
	}

	public void setSavingThrowType(Flag savingThrowType) {
		this.savingThrowType = savingThrowType;
	}

	public short getSavingThrowBonus() {
		return savingThrowBonus;
	}

	public void setSavingThrowBonus(short savingThrowBonus) {
		this.savingThrowBonus = savingThrowBonus;
	}

	public byte getPower() {
		return power;
	}

	public void setPower(byte power) {
		this.power = power;
	}

	public int getParam1() {
		return param1;
	}

	public void setParam1(int param1) {
		this.param1 = param1;
	}

	public int getParam2() {
		return param2;
	}

	public void setParam2(int param2) {
		this.param2 = param2;
	}

}
