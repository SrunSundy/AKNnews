package com.spring.akn.entities;

public class SiteDetailDTO {
	private int c_id;
	private int s_id;
	private String url;
	private boolean status;
	public int getC_id() {
		return c_id;
	}
	public void setC_id(int c_id) {
		this.c_id = c_id;
	}
	public int getS_id() {
		return s_id;
	}
	public void setS_id(int s_id) {
		this.s_id = s_id;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "SiteDetailDTO [c_id=" + c_id + ", s_id=" + s_id + ", url=" + url + ", status=" + status + "]";
	}
	
	
}
