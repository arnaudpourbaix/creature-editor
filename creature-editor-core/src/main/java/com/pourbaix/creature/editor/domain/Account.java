package com.pourbaix.creature.editor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Account generated by hbm2java
 */
@Entity
@Table(name = "ACCOUNT", schema = "PUBLIC", catalog = "PUBLIC")
public class Account implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private int id;

	@Column(name = "EMAIL", nullable = false, length = 100)
	private String email;

	@Column(name = "PASSWORD", nullable = false, length = 200)
	private String password;

	@Column(name = "ROLE", nullable = false, length = 50)
	private String role;

	public Account() {
	}

	public Account(int id, String email, String password, String role) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
