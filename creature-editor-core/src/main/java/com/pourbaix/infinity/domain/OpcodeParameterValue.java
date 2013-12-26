package com.pourbaix.infinity.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * OpcodeParameterValue generated by hbm2java
 */
@Entity
@Immutable
@Table(name = "OPCODE_PARAMETER_VALUE", schema = "PUBLIC", catalog = "PUBLIC")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class OpcodeParameterValue implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARAMETER_ID")
	@Immutable
	private OpcodeParameter parameter;

	@Column(name = "VALUE", nullable = false)
	private int value;

	@Column(name = "LABEL", length = 100)
	private String label;

	public OpcodeParameterValue() {
	}

	public OpcodeParameterValue(int value) {
		this.value = value;
	}

	public OpcodeParameterValue(OpcodeParameter parameter, int value, String label) {
		this.parameter = parameter;
		this.value = value;
		this.label = label;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OpcodeParameter getParameter() {
		return this.parameter;
	}

	public void setParameter(OpcodeParameter parameter) {
		this.parameter = parameter;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
