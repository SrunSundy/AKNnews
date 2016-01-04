package com.spring.akn.entities.user;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority{

	private static final long serialVersionUID = -5731466746267180124L;

	private int id;
	private String name;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAuthority() {
		return this.getName();
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}
	
}
