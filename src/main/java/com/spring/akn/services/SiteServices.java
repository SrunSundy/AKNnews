package com.spring.akn.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.spring.akn.entities.SiteDTO;

public interface SiteServices {
	public List<SiteDTO> listSite();
	public SiteDTO findSiteById(int id);
	public boolean isDeleteSiteById(int id);
	public boolean isInsertSite(SiteDTO siteDTO);
	public boolean isUpdateSite(SiteDTO siteDTO);
	public int countSiteRecord();	
	public boolean isUploadLogo(MultipartFile file, HttpServletRequest request, int s_id);
	public int editLogoName(int s_id, String name);
	public String getLogoName(int s_id);
	public String getLogoPath(HttpServletRequest request, int s_id);

}
