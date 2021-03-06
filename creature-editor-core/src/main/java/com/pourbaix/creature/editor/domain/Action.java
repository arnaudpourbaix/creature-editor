package com.pourbaix.creature.editor.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Action generated by hbm2java
 */
@Entity
@NamedQuery(name = "Action.findByName", query = "SELECT a FROM Action a WHERE LOWER(a.name) = LOWER(?1)")
@Table(name = "ACTION", schema = "PUBLIC", catalog = "PUBLIC")
public class Action implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NAME", unique = true, nullable = false, length = 50)
	private String name;

	@Column(name = "TOBEX", nullable = false)
	private boolean tobex;

	@Column(name = "PARAMS", length = 100)
	private String params;

	@Column(name = "LABEL", length = 1500)
	private String label;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "action")
	private Set<ActionKeyword> actionKeywords = new HashSet<ActionKeyword>(0);

	public Action() {
	}

	public Action(String name, boolean tobex) {
		this.name = name;
		this.tobex = tobex;
	}

	public Action(String name, boolean tobex, String params, String label) {
		this.name = name;
		this.tobex = tobex;
		this.params = params;
		this.label = label;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTobex() {
		return this.tobex;
	}

	public void setTobex(boolean tobex) {
		this.tobex = tobex;
	}

	public String getParams() {
		return this.params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Set<ActionKeyword> getActionKeywords() {
		return actionKeywords;
	}

	public void setActionKeywords(Set<ActionKeyword> actionKeywords) {
		this.actionKeywords = actionKeywords;
	}

}
