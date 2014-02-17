package com.pourbaix.creature.editor.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BOOLEAN")
public class ParameterBooleanValue extends Parameter implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VALUE")
	private Boolean value;

	public Boolean getValue() {
		return this.value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

}
