package com.spring.akn.entities;

public class SearchNewsDTO {
	
	private String key;
	private int page;
	
	private int cid;
	private int sid;
	private int uid;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "SearchNewsDTO [key=" + key + ", page=" + page + ", cid=" + cid + ", sid=" + sid + ", uid=" + uid + "]";
	}
	
	
	
}
