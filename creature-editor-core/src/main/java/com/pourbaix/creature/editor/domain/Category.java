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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Category generated by hbm2java
 */
@Entity
@Table(name = "CATEGORY", schema = "PUBLIC", catalog = "PUBLIC")
@NamedEntityGraph(name = "Category.eager", attributeNodes = @NamedAttributeNode("parent"))
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID")
	private Category parent;

	@Column(name = "NAME", nullable = false, length = 250)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CREATURE_CATEGORY", schema = "PUBLIC", catalog = "PUBLIC", joinColumns = { @JoinColumn(name = "CATEGORY_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "CREATURE_ID", nullable = false, updatable = false) })
	private Set<Creature> creatures = new HashSet<Creature>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.REMOVE)
	private Set<Category> children = new HashSet<Category>(0);

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(Category parent, String name, Set<Creature> creatures, Set<Category> children) {
		this.parent = parent;
		this.name = name;
		this.creatures = creatures;
		this.children = children;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getParent() {
		return this.parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Creature> getCreatures() {
		return this.creatures;
	}

	public void setCreatures(Set<Creature> creatures) {
		this.creatures = creatures;
	}

	public Set<Category> getChildren() {
		return this.children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

}
