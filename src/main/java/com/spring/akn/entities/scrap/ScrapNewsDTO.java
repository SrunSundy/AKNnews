package com.spring.akn.entities.scrap;

public class ScrapNewsDTO {
	private String link;
	private String image;
	private String title;
	private String description;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "ScrapNewsDTO [link=" + link + ", image=" + image + ", title=" + title + ", description=" + description
				+ "]";
	}
	
}
