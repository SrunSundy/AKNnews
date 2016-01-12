package com.spring.akn.entities;

public class SiteDTO {
	private int id;
	private String name;
	private String url;
	private String logo;
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	@Override
	public String toString() {
		return "SiteDTO [id=" + id + ", name=" + name + ", url=" + url + ", logo=" + logo + "]";
	}
	
	
}
