package com.spring.akn.entities;

import java.sql.Timestamp;
import java.util.Date;

public class NewsDTO {
	private int id;
	private String title;
	private String description;
	private String image;
	private Timestamp date;
	private int hit;
	private String url;
	private boolean status;
	private String content;
	private boolean isSaved;

	private SiteDTO site;
	private CategoryDTO category;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}	
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isSaved() {
		return isSaved;
	}
	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public SiteDTO getSite() {
		return site;
	}
	public void setSite(SiteDTO site) {
		this.site = site;
	}
	public CategoryDTO getCategory() {
		return category;
	}
	public void setCategory(CategoryDTO category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "NewsDTO [id=" + id + ", title=" + title + ", description=" + description + ", image=" + image
				+ ", date=" + date + ", hit=" + hit + ", url=" + url + ", status=" + status + ", content=" + content
				+ ", isSaved=" + isSaved + ", site=" + site + ", category=" + category + "]";
	}
	

	
	
}
