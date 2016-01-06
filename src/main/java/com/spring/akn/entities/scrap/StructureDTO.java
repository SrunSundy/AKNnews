package com.spring.akn.entities.scrap;

public class StructureDTO {
	private int id;
	private String url;
	private int siteId;
	private int categoryId;
	private String rowsSelector;
	private String imageSelector;
	private String linkSelector;
	private String titleSelector;
	private String descriptionSelector;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRowsSelector() {
		return rowsSelector;
	}
	public void setRowsSelector(String rowsSelector) {
		this.rowsSelector = rowsSelector;
	}
	public String getImageSelector() {
		return imageSelector;
	}
	public void setImageSelector(String imageSelector) {
		this.imageSelector = imageSelector;
	}
	public String getLinkSelector() {
		return linkSelector;
	}
	public void setLinkSelector(String linkSelector) {
		this.linkSelector = linkSelector;
	}
	public String getTitleSelector() {
		return titleSelector;
	}
	public void setTitleSelector(String titleSelector) {
		this.titleSelector = titleSelector;
	}
	public String getDescriptionSelector() {
		return descriptionSelector;
	}
	public void setDescriptionSelector(String descriptionSelector) {
		this.descriptionSelector = descriptionSelector;
	}
	@Override
	public String toString() {
		return "Structure [id=" + id + ", rowsSelector=" + rowsSelector + ", imageSelector=" + imageSelector
				+ ", linkSelector=" + linkSelector + ", titleSelector=" + titleSelector + ", descriptionSelector="
				+ descriptionSelector + "]";
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
}
