package com.pourbaix.creature.editor.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("LIST")
public class ParameterListValue extends Parameter implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VALUE", length = 500)
	private String value;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parameter")
	private Set<ParameterValue> parameterValues = new HashSet<ParameterValue>(0);

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Set<ParameterValue> getParameterValues() {
		return this.parameterValues;
	}

	public void setParameterValues(Set<ParameterValue> parameterValues) {
		this.parameterValues = parameterValues;
	}

}
