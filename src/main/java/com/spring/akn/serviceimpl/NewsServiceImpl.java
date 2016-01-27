package com.spring.akn.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SearchNewsDTO;
import com.spring.akn.entities.frmApiDoc.FrmSaveListAdd;
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
	public int insertNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return newsrepository.insertNews(news);
	}

	@Override
	public int deleteNews(int newsid) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*@Override
	public int updateNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return newsrepository.updateNews(news);
	}*/

	@Override
	public int toggleNews(int newsid) {
		// TODO Auto-generated method stub
		return newsrepository.toggleNews(newsid);
	}

	@Override
	public List<NewsDTO> searchNews(SearchNewsDTO search) {
		// TODO Auto-generated method stub
		return newsrepository.searchNews(search);
	}

	@Override
	public int saveNews(FrmSaveListAdd savenews) {
		// TODO Auto-generated method stub
		return newsrepository.saveNews(savenews);
	}

	@Override
	public int deleteSavedNews(int newsid,int userid) {
		// TODO Auto-generated method stub
		return newsrepository.deleteSavedNews(newsid,userid);
	}

	@Override
	public List<NewsDTO> listSavedNews(int userid,int row ,int page) {
		// TODO Auto-generated method stub
		return newsrepository.listSavedNews(userid, row, page);
	}

	@Override
	public List<NewsDTO> getPopularNews(int userid,int day,int row) {
		// TODO Auto-generated method stub
		return newsrepository.getPopularNews(userid,day,row);
	}

	@Override
	public int getNewsTotalPage(String key,int row,int categoryid,int siteid,int userid)  {
		// TODO Auto-generated method stub
		return newsrepository.getNewsTotalPage(key,row, categoryid, siteid,userid);
	}

	@Override
	public int getNewsTotalRecords(String key, int categoryid, int siteid,int userid) {
		// TODO Auto-generated method stub
		return newsrepository.getNewsTotalRecords(key, categoryid, siteid,userid);
	}

	@Override
	public int updateView(int newsid) {
		// TODO Auto-generated method stub
		return newsrepository.updateView(newsid);
	}

	/*@Override
	public NewsDTO listAData(int newsid) {
		// TODO Auto-generated method stub
		return newsrepository.listAData(newsid);
	}*/

	@Override
	public String getNewsTitle(int newsid) {
		// TODO Auto-generated method stub
		return newsrepository.getNewsTitle(newsid);
	}

	@Override
	public String getNewsDescription(int newsid) {
		// TODO Auto-generated method stub
		return newsrepository.getNewsDescription(newsid);
	}

	@Override
	public String getNewsThumbnail(int newsid) {
		// TODO Auto-generated method stub
		return newsrepository.getNewsThumbnail(newsid);
	}

	@Override
	public String getNewsContent(int newsid) {
		// TODO Auto-generated method stub
		return newsrepository.getNewsContent(newsid);
	}

	@Override
	public int updateNewsTitle(NewsDTO news) {
		// TODO Auto-generated method stub
		return newsrepository.updateNewsTitle(news);
	}

	@Override
	public int updateNewsCategory(NewsDTO news) {
		// TODO Auto-generated method stub
		return newsrepository.updateNewsCategory(news);
	}

	@Override
	public int updateNewsDescription(NewsDTO news) {
		// TODO Auto-generated method stub
		return newsrepository.updateNewsDescription(news);
	}

	/*@Override
	public int updateNewsThumbnail(NewsDTO news) {
		// TODO Auto-generated method stub
		return newsrepository.updateNewsThumbnail(news);
	}*/

	@Override
	public int updateNewsContent(NewsDTO news) {
		// TODO Auto-generated method stub
		return newsrepository.updateNewsContent(news);
	}

	@Override
	public List<NewsDTO> listNewsStatistic(int categoryid, int siteid, int day, int row) {
		// TODO Auto-generated method stub
		return newsrepository.listNewsStatistic(categoryid, siteid, day, row);
	}


	

}
