package com.pourbaix.creature.editor.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Spell generated by hbm2java
 */
@Entity
@NamedQuery(name = "Spell.findAll", query = "SELECT s FROM Spell s JOIN FETCH s.mod ORDER BY s.resource desc")
@Table(name = "SPELL", schema = "PUBLIC", catalog = "PUBLIC")
public class Spell implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOD_ID", nullable = false)
	private Mod mod;

	@Column(name = "RESOURCE", nullable = false, length = 8)
	private String resource;

	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	@Column(name = "IDENTIFIER", length = 50)
	private String identifier;

	@Column(name = "DESCRIPTION", length = 3000)
	private String description;

	@Column(name = "LEVEL", nullable = false)
	private int level;

	@Column(name = "RANGE")
	private Integer range;

	@Column(name = "SCHOOL", length = 15)
	private String school;

	@Column(name = "HURT_ALLY", nullable = false)
	private boolean hurtAlly;

	public Spell() {
	}

	public Spell(Mod mod, String resource, String name, int level, boolean hurtAlly) {
		this.mod = mod;
		this.resource = resource;
		this.name = name;
		this.level = level;
		this.hurtAlly = hurtAlly;
	}

	public Spell(Mod mod, String resource, String name, String identifier, String description, int level, Integer range, String school, boolean hurtAlly) {
		this.mod = mod;
		this.resource = resource;
		this.name = name;
		this.identifier = identifier;
		this.description = description;
		this.level = level;
		this.range = range;
		this.school = school;
		this.hurtAlly = hurtAlly;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Mod getMod() {
		return this.mod;
	}

	public void setMod(Mod mod) {
		this.mod = mod;
	}

	public String getResource() {
		return this.resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Integer getRange() {
		return this.range;
	}

	public void setRange(Integer range) {
		this.range = range;
	}

	public String getSchool() {
		return this.school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public boolean isHurtAlly() {
		return this.hurtAlly;
	}

	public void setHurtAlly(boolean hurtAlly) {
		this.hurtAlly = hurtAlly;
	}

}
