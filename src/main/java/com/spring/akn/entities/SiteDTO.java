package com.spring.akn.entities;

public class SiteDTO {
	private int id;
	private String name;
	private String url;
	private String logo;
	private String basepath;
	private String prefixImg;
	private String prefixLink;
	
	public int getId() {
		return id;
	}

	public String getBasepath() {
		return basepath;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
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
		return "SiteDTO [id=" + id + ", name=" + name + ", url=" + url + ", logo=" + logo + ", basepath=" + basepath
				+ "]";
	}

	public String getPrefixImg() {
		return prefixImg;
	}

	public void setPrefixImg(String prefixImg) {
		this.prefixImg = prefixImg;
	}

	public String getPrefixLink() {
		return prefixLink;
	}

	public void setPrefixLink(String prefixLink) {
		this.prefixLink = prefixLink;
	}


}
