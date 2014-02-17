package com.pourbaix.creature.editor.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("STRING")
public class ParameterStringValue extends Parameter implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VALUE", length = 500)
	private String value;

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
