package com.spring.akn.entities.scrap;

public class DetailStructureDTO {
	private int id;
	private String url;
	private String titleSelector;
	private String contentSelector;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitleSelector() {
		return titleSelector;
	}
	public void setTitleSelector(String titleSelector) {
		this.titleSelector = titleSelector;
	}
	public String getContentSelector() {
		return contentSelector;
	}
	public void setContentSelector(String contentSelector) {
		this.contentSelector = contentSelector;
	}
	@Override
	public String toString() {
		return "DetailStructure [id=" + id + ", titleSelector=" + titleSelector + ", contentSelector=" + contentSelector
				+ "]";
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
