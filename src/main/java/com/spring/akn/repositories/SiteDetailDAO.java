package com.spring.akn.repositories;

public interface SiteDetailDAO {
	public boolean isInsertSiteDetail(int s_id,int c_id, String url);
	public boolean isUpdateSiteDetail(int s_id,int c_id, String url);
	public boolean isDeleteSiteDetail(int s_id,int c_id );
	public boolean isToggleStatusSiteDetail(int s_id, int c_id);
	
	
}
