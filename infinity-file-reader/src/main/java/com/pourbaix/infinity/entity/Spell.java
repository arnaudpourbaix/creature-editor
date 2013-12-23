package com.pourbaix.infinity.entity;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.pourbaix.infinity.datatype.Flag;
import com.pourbaix.infinity.datatype.SchoolEnum;
import com.pourbaix.infinity.datatype.SpellSecondaryTypeEnum;
import com.pourbaix.infinity.datatype.SpellTypeEnum;
import com.pourbaix.infinity.util.Constant;

public class Spell {
	private String resource;
	private String identifier;
	private String name;
	private byte level;
	private String description;
	private SpellTypeEnum type;
	private SchoolEnum school;
	private SpellSecondaryTypeEnum secondaryType;
	private Flag flags;
	private Flag exclusionFlags;
	private List<Ability> abilities;
	private List<Effect> globalEffects;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(resource);
		sb.append(", name: ").append(name);
		sb.append(", level: ").append(level);
		sb.append(", identifier: ").append(identifier);
		sb.append(", type: ").append(type.getLabel());
		sb.append(", school: ").append(school.getLabel());
		sb.append(", secondary type: ").append(secondaryType.getLabel());
		sb.append(", flags: ").append(flags.toString());
		sb.append(", exclusionFlags: ").append(exclusionFlags.toString());
		if (!CollectionUtils.isEmpty(abilities)) {
			for (Ability ability : abilities) {
				if (ability.getEffects() != null) {
					for (Effect effect : ability.getEffects()) {
						sb.append(Constant.CARRIAGE_RETURN).append(effect.toString());
					}
				}
			}
		}
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

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
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

	public Flag getFlags() {
		return flags;
	}

	public void setFlags(Flag flags) {
		this.flags = flags;
	}

	public Flag getExclusionFlags() {
		return exclusionFlags;
	}

	public void setExclusionFlags(Flag exclusionFlags) {
		this.exclusionFlags = exclusionFlags;
	}

	public List<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(List<Ability> abilities) {
		this.abilities = abilities;
	}

	public List<Effect> getGlobalEffects() {
		return globalEffects;
	}

	public void setGlobalEffects(List<Effect> globalEffects) {
		this.globalEffects = globalEffects;
	}

}
