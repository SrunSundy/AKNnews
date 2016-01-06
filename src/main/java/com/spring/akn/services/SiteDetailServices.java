package com.spring.akn.services;

public interface SiteDetailServices {
	public boolean isInsertSiteDetail(int s_id,int c_id, String url);
	public boolean isUpdateSiteDetail(int s_id,int c_id, String url);
	public boolean isDeleteSiteDetail(int s_id,int c_id );
}
