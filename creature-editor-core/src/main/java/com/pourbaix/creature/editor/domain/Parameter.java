package com.pourbaix.creature.editor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Parameter generated by hbm2java
 */
@Entity
@Table(name = "PARAMETER", schema = "PUBLIC", catalog = "PUBLIC")
public class Parameter implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NAME", unique = true, nullable = false, length = 50)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TYPE_ID")
	private ParameterType parameterType;

	@Column(name = "DATATYPE", length = 10)
	private String datatype;

	@Column(name = "VALUE", length = 500)
	private String value;

	@Column(name = "DESCRIPTION", length = 1000)
	private String description;

	public Parameter() {
	}

	public Parameter(String name) {
		this.name = name;
	}

	public Parameter(String name, ParameterType parameterType, String datatype, String value, String description) {
		this.name = name;
		this.parameterType = parameterType;
		this.datatype = datatype;
		this.value = value;
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ParameterType getParameterType() {
		return this.parameterType;
	}

	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}

	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}