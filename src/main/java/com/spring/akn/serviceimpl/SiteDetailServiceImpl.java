package com.spring.akn.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.repositories.SiteDetailDAO;
import com.spring.akn.services.SiteDetailServices;

@Service
public class SiteDetailServiceImpl implements SiteDetailServices{
	@Autowired
	private SiteDetailDAO siteDetailDAO;

	@Override
	public boolean isInsertSiteDetail(int s_id, int c_id, String url) {
		return siteDetailDAO.isInsertSiteDetail(s_id, c_id, url);
	}

	@Override
	public boolean isUpdateSiteDetail(int s_id, int c_id, String url) {
		return siteDetailDAO.isUpdateSiteDetail(s_id, c_id, url);
	}
	
	
}
