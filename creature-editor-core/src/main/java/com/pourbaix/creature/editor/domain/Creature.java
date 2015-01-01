package com.pourbaix.creature.editor.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Creature generated by hbm2java
 */
@Entity
@Table(name = "CREATURE", schema = "PUBLIC", catalog = "PUBLIC")
@NamedEntityGraphs({ 
	@NamedEntityGraph(name = "Creature.lists", attributeNodes = @NamedAttributeNode("attributeValues")),
	@NamedEntityGraph(name = "Creature.edit", attributeNodes = @NamedAttributeNode("attributeValues"))
})
public class Creature implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Column(name = "RESOURCE", unique = true, nullable = false)
	private String resource;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GAME_ID")
	private Game game;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MOD_ID", nullable = false)
	private Mod mod;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "creature", cascade = CascadeType.ALL)
	private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>(0);

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CREATURE_CATEGORY", schema = "PUBLIC", catalog = "PUBLIC", joinColumns = { @JoinColumn(name = "CREATURE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID", nullable = false, updatable = false) })
	private Set<Category> categories = new HashSet<Category>(0);

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	public Mod getMod() {
		return mod;
	}

	public void setMod(Mod mod) {
		this.mod = mod;
	}

	public Set<AttributeValue> getAttributeValues() {
		return this.attributeValues;
	}

	public void setAttributeValues(Set<AttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}

	public Set<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

}
