package com.pourbaix.creature.editor.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * AttributeValue generated by hbm2java
 */
@Entity
@Table(name = "ATTRIBUTE_VALUE", schema = "PUBLIC", catalog = "PUBLIC")
public class AttributeValue implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "STATE")
	@Enumerated(EnumType.STRING)
	private StateEnum state;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTRIBUTE_ID")
	private Attribute attribute;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATURE_ID")
	private Creature creature;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GAME_EDITION_ID")
	private GameEdition gameEdition;

	@Column(name = "STRING_VALUE", length = 50)
	private String stringValue;

	@Column(name = "NUMBER_VALUE")
	private Long numberValue;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(attribute.getShortLabel()).append(": ");
		if (attribute.getType() == AttributeTypeEnum.string || attribute.getType() == AttributeTypeEnum.stringref) {
			sb.append(stringValue);
		} else if (attribute.getType() == AttributeTypeEnum.number) {
			sb.append(numberValue);
		}
		return sb.toString();
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Attribute getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public Creature getCreature() {
		return this.creature;
	}

	public void setCreature(Creature creature) {
		this.creature = creature;
	}

	public GameEdition getGameEdition() {
		return this.gameEdition;
	}

	public void setGameEdition(GameEdition gameEdition) {
		this.gameEdition = gameEdition;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Long getNumberValue() {
		return numberValue;
	}

	public void setNumberValue(Long numberValue) {
		this.numberValue = numberValue;
	}

	public StateEnum getState() {
		return state;
	}

	public void setState(StateEnum state) {
		this.state = state;
	}
	
}
