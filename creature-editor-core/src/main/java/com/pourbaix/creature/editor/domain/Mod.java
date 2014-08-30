package com.pourbaix.creature.editor.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Mod generated by hbm2java
 */
@Entity
@Table(name = "MOD", schema = "PUBLIC", catalog = "PUBLIC")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Mod implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Column(name = "NAME", nullable = false, length = 100)
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mod", cascade = CascadeType.REMOVE)
	private Set<Spell> spells = new HashSet<Spell>(0);

	public Mod() {
	}

	public Mod(String name) {
		this.name = name;
	}

	public Mod(String name, Set<Spell> spells) {
		this.name = name;
		this.spells = spells;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Spell> getSpells() {
		return this.spells;
	}

	public void setSpells(Set<Spell> spells) {
		this.spells = spells;
	}

}
