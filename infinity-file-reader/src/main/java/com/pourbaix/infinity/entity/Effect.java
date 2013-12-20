package com.pourbaix.infinity.entity;

public class Effect {

	private short opcode;
	private TargetTypeEnum target;
	private byte timing;
	private byte resistance;
	private int duration;
	private byte probability1;
	private byte probability2;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("target: ").append(target.getLabel());
		return sb.toString();
	}

	public TargetTypeEnum getTarget() {
		return target;
	}

	public void setTarget(TargetTypeEnum target) {
		this.target = target;
	}

}
