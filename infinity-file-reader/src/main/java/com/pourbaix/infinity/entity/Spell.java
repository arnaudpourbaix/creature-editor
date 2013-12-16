package com.pourbaix.infinity.entity;

public class Spell {
	private String resource;
	private String name;
	private String description;
	private short flags;
	private short type;
	private short exclusionFlags;
	private byte school;
	private short level;

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("name: ").append(name);
		sb.append(", description: ").append(description);
		sb.append(", type: ").append(SpellTypeEnum.values()[type]);
		sb.append(", school: ").append(SchoolEnum.values()[school]);
		return sb.toString();
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public short getFlags() {
		return flags;
	}

	public void setFlags(short flags) {
		this.flags = flags;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public short getExclusionFlags() {
		return exclusionFlags;
	}

	public void setExclusionFlags(short exclusionFlags) {
		this.exclusionFlags = exclusionFlags;
	}

	public byte getSchool() {
		return school;
	}

	public void setSchool(byte school) {
		this.school = school;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

}
