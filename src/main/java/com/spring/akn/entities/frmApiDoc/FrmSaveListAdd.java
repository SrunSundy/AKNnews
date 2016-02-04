package com.spring.akn.entities.frmApiDoc;

public class FrmSaveListAdd {
	private int newsid;
	private int userid;
	public int getNewsid() {
		return newsid;
	}
	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}

	@Override
	public String toString() {
		return "SaveListDTO [newsid=" + newsid + ", userid=" + userid + "]";
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
}
