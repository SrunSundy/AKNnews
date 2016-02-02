package com.spring.akn.repositories;

import java.util.List;

import com.spring.akn.entities.SiteDetailDTO;

public interface SiteDetailDAO {
	public boolean isInsertSiteDetail(SiteDetailDTO siteDetailDTO);
	public boolean isUpdateSiteDetail(SiteDetailDTO siteDetailDTO);
	public boolean isDeleteSiteDetail(int s_id );
	public boolean isToggleStatusSiteDetail(int cid);
	public List<SiteDetailDTO> listSiteDetail();
	public List<SiteDetailDTO> listSiteDetailPage(int limit ,int offet , int s_id, int c_id );
	public SiteDetailDTO findSiteAndCategoryById(int cid);
	public int countRecord();
}
