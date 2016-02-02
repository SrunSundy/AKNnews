package com.spring.akn.services;

import java.util.List;

import com.spring.akn.entities.SiteDetailDTO;

public interface SiteDetailServices {
	public boolean isInsertSiteDetail(SiteDetailDTO siteDetailDTO);
	public boolean isUpdateSiteDetail(SiteDetailDTO siteDetailDTO);
	public boolean isDeleteSiteDetail(int s_id,int c_id );
	public boolean isToggleStatusSiteDetail(int s_id, int c_id);
	public List<SiteDetailDTO> listSiteDetail();
	public SiteDetailDTO findSiteAndCategoryById(int s_id, int c_id);
	public List<SiteDetailDTO> listSiteDetailPage(int limit, int offet ,int s_id, int c_id);
	public int countRecord();
}
