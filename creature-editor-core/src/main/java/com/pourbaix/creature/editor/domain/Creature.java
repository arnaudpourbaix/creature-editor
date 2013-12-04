package com.pourbaix.creature.editor.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Creature generated by hbm2java
 */
@Entity
@Table(name = "CREATURE", schema = "PUBLIC", catalog = "PUBLIC")
public class Creature implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GAME_ID")
	private Game game;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "creature")
	private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>(0);
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CREATURE_CATEGORY", schema = "PUBLIC", catalog = "PUBLIC", joinColumns = { @JoinColumn(name = "CREATURE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID", nullable = false, updatable = false) })
	private Set<Category> categories = new HashSet<Category>(0);

	public Creature() {
	}

	public Creature(Game game, Set<AttributeValue> attributeValues, Set<Category> categories) {
		this.game = game;
		this.attributeValues = attributeValues;
		this.categories = categories;
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
