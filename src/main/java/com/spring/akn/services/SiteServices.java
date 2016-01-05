package com.spring.akn.services;

import java.util.List;

import com.spring.akn.entities.SiteDTO;

public interface SiteServices {
	public List<SiteDTO> listSite();
	public SiteDTO findSiteById(int id);
	public boolean isDeleteSiteById(int id);
	public boolean isInsertSite(SiteDTO siteDTO);
	public boolean isUpdateSite(SiteDTO siteDTO);
	public int countSiteRecord();	
}
