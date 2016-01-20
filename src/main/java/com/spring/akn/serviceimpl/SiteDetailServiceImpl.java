package com.spring.akn.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.SiteDetailDTO;
import com.spring.akn.repositories.SiteDetailDAO;
import com.spring.akn.services.SiteDetailServices;

@Service
public class SiteDetailServiceImpl implements SiteDetailServices{
	@Autowired
	private SiteDetailDAO siteDetailDAO;

	@Override
	public boolean isInsertSiteDetail(SiteDetailDTO siteDetailDTO) {
		return siteDetailDAO.isInsertSiteDetail(siteDetailDTO);
	}

	@Override
	public boolean isUpdateSiteDetail(SiteDetailDTO siteDetailDTO) {
		return siteDetailDAO.isUpdateSiteDetail( siteDetailDTO);
	}

	@Override
	public boolean isDeleteSiteDetail(int s_id, int c_id) {
		return siteDetailDAO.isDeleteSiteDetail(s_id, c_id);
	}

	@Override
	public boolean isToggleStatusSiteDetail(int s_id, int c_id) {
		return siteDetailDAO.isToggleStatusSiteDetail(s_id, c_id);
	}

	@Override
	public List<SiteDetailDTO> listSiteDetail() {
		return siteDetailDAO.listSiteDetail();
	}
	
	
}
