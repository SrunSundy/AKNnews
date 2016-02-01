package com.spring.akn.entities;

import java.sql.Timestamp;

public class SavedNewsDTO {

	private int saveid;
	private int userid;
	private Timestamp saveddate;
	private int newsid;
	public SavedNewsDTO(){
		
	}
	
	public int getSaveid() {
		return saveid;
	}
	public void setSaveid(int saveid) {
		this.saveid = saveid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public Timestamp getSaveddate() {
		return saveddate;
	}
	public void setSaveddate(Timestamp saveddate) {
		this.saveddate = saveddate;
	}
	public int getNewsid() {
		return newsid;
	}
	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}
	@Override
	public String toString() {
		return "SavedNewsDTO [saveid=" + saveid + ", userid=" + userid + ", saveddate=" + saveddate + ", newsid="
				+ newsid + "]";
	}
	
	
}
