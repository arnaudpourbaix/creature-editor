package com.pourbaix.creature.editor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * State generated by hbm2java
 */
@Entity
@Table(name = "STATE", schema = "PUBLIC", catalog = "PUBLIC")
public class State implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Column(name = "NAME", nullable = false, length = 10)
	private String name;

	@Column(name = "LABEL", nullable = false, length = 50)
	private String label;

	public State() {
	}

	public State(String name, String label) {
		this.name = name;
		this.label = label;
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

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
