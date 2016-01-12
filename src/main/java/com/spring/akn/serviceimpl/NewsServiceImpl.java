package com.spring.akn.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SaveListDTO;
import com.spring.akn.entities.SearchNewsDTO;
import com.spring.akn.repositories.NewsRepositories;
import com.spring.akn.services.NewsService;

@Service
public class NewsServiceImpl implements NewsService{

	@Autowired
	NewsRepositories newsrepository;
	@Override
	public List<NewsDTO> listNewsDatas(int page,int row, int categoryid, int siteid, int userid) {
		// TODO Auto-generated method stub
		return newsrepository.listNewsDatas(page,row, categoryid, siteid, userid);
	}

	/*@Override
	public NewsDTO listNewsData(int newsid, int userid) {
		// TODO Auto-generated method stub
		return newsrepository.listNewsData(newsid, userid);
	}*/


	@Override
	public List<NewsDTO> searchNews(SearchNewsDTO search) {
		// TODO Auto-generated method stub
		return newsrepository.searchNews(search);
	}

	@Override
	public int saveNews(SaveListDTO savenews) {
		// TODO Auto-generated method stub
		return newsrepository.saveNews(savenews);
	}

	@Override
	public int deleteSavedNews(int newsid,int userid) {
		// TODO Auto-generated method stub
		return newsrepository.deleteSavedNews(newsid,userid);
	}

	@Override
	public List<NewsDTO> listSavedNews(int userid) {
		// TODO Auto-generated method stub
		return newsrepository.listSavedNews(userid);
	}

	@Override
	public List<NewsDTO> getPopularNews(int userid) {
		// TODO Auto-generated method stub
		return newsrepository.getPopularNews(userid);
	}

	@Override
	public int getNewsTotalPage(String key,int row,int categoryid,int siteid)  {
		// TODO Auto-generated method stub
		return newsrepository.getNewsTotalPage(key,row, categoryid, siteid);
	}

	@Override
	public int getNewsTotalRecords(String key, int categoryid, int siteid) {
		// TODO Auto-generated method stub
		return newsrepository.getNewsTotalRecords(key, categoryid, siteid);
	}

	@Override
	public int insertNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteNews(int newsid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int toggleNews(int newsid) {
		// TODO Auto-generated method stub
		return 0;
	}

}
