package com.spring.akn.entities;

public class CategoryDTO {
	private int id;
	private String name;
	private boolean isMenu;
	
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
	public boolean isMenu() {
		return isMenu;
	}
	public void setMenu(boolean isMenu) {
		this.isMenu = isMenu;
	}
	
	@Override
	public String toString() {
		return "CategoryDTO [id=" + id + ", name=" + name + ", isMenu=" + isMenu + "]";
	}
}
