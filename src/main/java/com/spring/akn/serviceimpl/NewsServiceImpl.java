package com.spring.akn.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.repositories.NewsRepositories;
import com.spring.akn.services.NewsService;

@Service
public class NewsServiceImpl implements NewsService{

	@Autowired
	NewsRepositories newsrepository;
	@Override
	public List<NewsDTO> listNewsDatas(int page, int categoryid, int siteid, int userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewsDTO listNewsData(int newsid, int userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteNews(int news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<NewsDTO> searchNews(String key, int page, int categoryid, int siteid, int userid) {
		// TODO Auto-generated method stub
		return null;
	}

}
