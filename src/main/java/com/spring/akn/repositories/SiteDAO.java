package com.spring.akn.repositories;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.spring.akn.entities.SiteDTO;

public interface SiteDAO {
	// list all source
	public List<SiteDTO> listSite();
	// get one record of source
	public SiteDTO findSiteById(int id);
	// delete source by id
	public boolean isDeleteSiteById(int id);
	// insert object of source 
	public boolean isInsertSite(SiteDTO siteDTO);
	// update object of source 
	public boolean isUpdateSite(SiteDTO siteDTO);
	// count all record in source
	public int countSiteRecord();
	// upload logo of source
	public boolean isUploadLogo(MultipartFile file, HttpServletRequest request, int s_id);
	// change logo name
	public int editLogoName(int s_id, String name);
	// get logo name filter by id
	public String getLogoName(int s_id);
	// update base path url of image or link
	public boolean updateSiteBasePath(int id, String basePath);
	// check exist source in news or not
	public SiteDTO checkExistSite(int id);
}
