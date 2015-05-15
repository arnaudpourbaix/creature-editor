package com.pourbaix.creature.editor.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("INT")
public class ParameterIntegerValue extends Parameter implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VALUE", columnDefinition = "VARCHAR")
	private Integer value;

	@Column(name = "MIN_VALUE")
	private Integer minValue;

	@Column(name = "MAX_VALUE")
	private Integer maxValue;

	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getMinValue() {
		return this.minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	public Integer getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

}
