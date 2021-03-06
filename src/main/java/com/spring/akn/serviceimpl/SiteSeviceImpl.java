package com.spring.akn.serviceimpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.akn.entities.SiteDTO;
import com.spring.akn.repositories.SiteDAO;
import com.spring.akn.services.SiteServices;

@Service
public class SiteSeviceImpl implements SiteServices{
	
	@Autowired
	private SiteDAO siteDAO;
	@Override
	public List<SiteDTO> listSite() {
		return siteDAO.listSite();
	}

	@Override
	public SiteDTO findSiteById(int id) {
		return siteDAO.findSiteById(id);
	}

	@Override
	public boolean isDeleteSiteById(int id) {
		return siteDAO.isDeleteSiteById(id);
	}

	@Override
	public boolean isInsertSite(SiteDTO siteDTO) {
		return siteDAO.isInsertSite(siteDTO);
	}

	@Override
	public boolean isUpdateSite(SiteDTO siteDTO) {
		return siteDAO.isUpdateSite(siteDTO);
	}

	@Override
	public int countSiteRecord() {
		return siteDAO.countSiteRecord();
	}

	@Override
	public boolean isUploadLogo(MultipartFile file, HttpServletRequest request, int s_id) {
		return siteDAO.isUploadLogo(file, request, s_id);
	}

	@Override
	public int editLogoName(int s_id, String name) {
		return siteDAO.editLogoName(s_id, name);
	}

	@Override
	public String getLogoName(int s_id) {
		return siteDAO.getLogoName(s_id);
	}

	@Override
	public String getLogoPath(HttpServletRequest request, int s_id) {				
		return siteDAO.getLogoName(s_id);
	}

	@Override
	public boolean updateSiteBasePath(int id, String basePath) {
		return siteDAO.updateSiteBasePath(id, basePath);
	}

	@Override
	public SiteDTO checkExistSite(int id) {
		return siteDAO.checkExistSite(id);
	}

	
	


}
