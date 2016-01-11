package com.spring.akn.entities;

public class SaveListDTO {

	
	private int newsid;
	private int userid;
	public int getNewsid() {
		return newsid;
	}
	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	@Override
	public String toString() {
		return "SaveListDTO [newsid=" + newsid + ", userid=" + userid + "]";
	}
	
	
}
