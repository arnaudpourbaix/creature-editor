package com.pourbaix.infinity.entity;

public class Spell {
	private String resource;
	private String identifier;
	private String name;
	private int level;
	private String description;
	private SpellTypeEnum type;
	private SchoolEnum school;
	private SpellSecondaryTypeEnum secondaryType;
	private short flags;
	private short exclusionFlags;

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("resource: ").append(resource);
		sb.append(", name: ").append(name);
		sb.append(", level: ").append(level);
		sb.append(", identifier: ").append(identifier);
		sb.append(", type: ").append(type.getLabel());
		sb.append(", school: ").append(school.getLabel());
		sb.append(", secondary type: ").append(secondaryType.getLabel());
		return sb.toString();
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SpellTypeEnum getType() {
		return type;
	}

	public void setType(SpellTypeEnum type) {
		this.type = type;
	}

	public SchoolEnum getSchool() {
		return school;
	}

	public void setSchool(SchoolEnum school) {
		this.school = school;
	}

	public SpellSecondaryTypeEnum getSecondaryType() {
		return secondaryType;
	}

	public void setSecondaryType(SpellSecondaryTypeEnum secondaryType) {
		this.secondaryType = secondaryType;
	}

	public short getFlags() {
		return flags;
	}

	public void setFlags(short flags) {
		this.flags = flags;
	}

	public short getExclusionFlags() {
		return exclusionFlags;
	}

	public void setExclusionFlags(short exclusionFlags) {
		this.exclusionFlags = exclusionFlags;
	}

}
