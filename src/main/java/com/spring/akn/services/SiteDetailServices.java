package com.spring.akn.services;

import java.util.List;

import com.spring.akn.entities.SiteDetailDTO;

public interface SiteDetailServices {
	// insert object for scrap url
	public boolean isInsertSiteDetail(SiteDetailDTO siteDetailDTO);
	// update object for scrap url
	public boolean isUpdateSiteDetail(SiteDetailDTO siteDetailDTO);
	// delete scrap url by id
	public boolean isDeleteSiteDetail(int s_id );
	// change status of scrap url if status true can scrap else not
	public boolean isToggleStatusSiteDetail(int cid);
	// list all site url
	public List<SiteDetailDTO> listSiteDetail();
	// list all site url page by page
	public List<SiteDetailDTO> listSiteDetailPage(int limit ,int offet , int s_id, int c_id );
	// check exit site and category for scrap url
	public SiteDetailDTO findSiteAndCategoryById(int cid);
	// count all record for scrap url
	public int countRecord();
}
