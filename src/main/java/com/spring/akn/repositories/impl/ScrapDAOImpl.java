package com.spring.akn.repositories.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SiteDTO;
import com.spring.akn.entities.scrap.StructureDTO;
import com.spring.akn.entities.scrap.TestScrapDTO;
import com.spring.akn.repositories.ScrapDAO;

@Repository
public class ScrapDAOImpl implements ScrapDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int[] scrapAllSites() {
		
		ArrayList<NewsDTO> news = new ArrayList<NewsDTO>();
		
		ArrayList<StructureDTO> selectors = (ArrayList<StructureDTO>) this.getAllSelectors();
		
		for(StructureDTO selector:selectors){
			System.out.println("=>> Scraping URL : " + selector.getUrl());
			news.addAll(scrapNews(selector));
			System.out.println("=>> Completed..!");
		}
		System.out.println("=>> Scraping DONE..!");
		
		int []insertRow =  scrapNewsToDatabase(news);
		
		System.out.println("=>> Insert Into Database Successfully..!");
		
		return insertRow;
	}
	
	private int[] scrapNewsToDatabase(final ArrayList<NewsDTO> news){
		
		String sql = "INSERT INTO news.tbnews(news_title, news_description, news_img, news_url, category_id, source_id) " +
				     "SELECT ?, ?, (SELECT DISTINCT s_basepath || ? FROM news.tbsite LEFT JOIN news.tbnews ON news.tbsite.s_id=news.tbnews.source_id WHERE s_id=?), " + 
				     "(SELECT DISTINCT s_prefix_link || ? FROM news.tbsite LEFT JOIN news.tbnews ON news.tbsite.s_id=news.tbnews.source_id WHERE s_id=?), ?, ? " +
				     "WHERE NOT EXISTS(SELECT news_url FROM news.tbnews WHERE news_url=(SELECT DISTINCT s_prefix_link || ? FROM news.tbsite LEFT JOIN news.tbnews ON news.tbsite.s_id=news.tbnews.source_id WHERE s_id=?))";
		
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
						NewsDTO n = news.get(i);
						ps.setString(1, n.getTitle());
						ps.setString(2, n.getDescription());
						ps.setString(3, n.getImage());
						ps.setInt(4, n.getSite().getId());
						ps.setString(5, n.getUrl());
						ps.setInt(6, n.getSite().getId());
						ps.setInt(7, n.getCategory().getId());
						ps.setInt(8, n.getSite().getId());
						ps.setString(9, n.getUrl());
						ps.setInt(10, n.getSite().getId());
			}
			
			@Override
			public int getBatchSize() {
				return news.size();
			}
		});
	}
	
	private List<StructureDTO> getAllSelectors(){
		
		String sql = "SELECT sd.url, link_selector, rows_selector, image_selector, title_selector, site_id, category_id, s.s_prefix_img " +  
					 "FROM news.tbsite_detail sd INNER JOIN news.tbsite s ON sd.site_id=s.s_id " + 
					 "INNER JOIN news.tbstructure st ON st.id=s.s_id " + 
					 "WHERE sd.status=true";
		
		return jdbcTemplate.query(sql, new RowMapper<StructureDTO>() {

			@Override
			public StructureDTO mapRow(ResultSet rs, int rowNumber) throws SQLException {
				StructureDTO selectors = new StructureDTO();
				
				selectors.setUrl(rs.getString("url"));
				selectors.setCategoryId(rs.getInt("category_id"));
				selectors.setSiteId(rs.getInt("site_id"));
				selectors.setRowsSelector(rs.getString("rows_selector"));
				selectors.setLinkSelector(rs.getString("link_selector"));
				selectors.setTitleSelector(rs.getString("title_selector"));
				selectors.setImageSelector(rs.getString("image_selector"));
				selectors.setPrefixImg(rs.getString("s_prefix_img"));
				return selectors;
			}
		});
		
	}
	
	private ArrayList<NewsDTO> scrapNews(StructureDTO selector){
		
		ArrayList<NewsDTO> news = new ArrayList<NewsDTO>();
		
		try {
			Document doc = Jsoup.connect(selector.getUrl()).timeout(30000).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").get();
			
			Elements elements = doc.select(selector.getRowsSelector());
			for(Element e:elements){
				
				String link = e.select(selector.getLinkSelector()).attr("href");
				
				if(link=="" || link==null)
					continue;

				String title = e.select(selector.getTitleSelector()).text();
				
				String image = e.select(selector.getImageSelector()).attr(selector.getPrefixImg()+"src");
				
				
				NewsDTO n = new NewsDTO();
				n.setUrl(link);
				n.setImage(image);
				n.setTitle(title);
				
				CategoryDTO category = new CategoryDTO();
				category.setId(selector.getCategoryId());
				
				SiteDTO site = new SiteDTO();
				site.setId(selector.getSiteId());
				
				n.setCategory(category);
				n.setSite(site);
				
				news.add(n);
			}
			
		} catch (IOException e) {
			System.err.println("IOException Occurr + " + e.toString());
			
		} catch (Exception e){
			System.err.println("Exception Occurr + " + e.toString());
		}
		
		return news;
	}

	@Override
	public NewsDTO scrapNews(int id, int user_id) {
		
		String url = this.getURLNotAKN(id);
		System.out.println("Scraping URL : "+url);
		if(url != null)
			return readOtherNews(url, user_id);
		
		return readAKNNews(id, user_id);
	
	}
	
	private String getURLNotAKN(int id){
		try{
			String sql = "SELECT n.news_url " + 
						 "FROM news.tbnews n INNER JOIN news.tbsite s ON n.source_id=s.s_id " + 
						 "WHERE n.news_id=? AND s.s_url NOT LIKE '%news.khmeracademy.org%'";
			
			return jdbcTemplate.queryForObject(sql , new Object[]{id}, String.class);		
			
		}catch(IncorrectResultSizeDataAccessException ex){
			return null;
		}
	}
	
	private StructureDTO getSelector(String url, int user_id){
		
		String sql = "SELECT (CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS issaved, " +
					 "content_selector, n.news_id, n.news_date, n.news_title, n.news_content, n.news_hit, n.news_img, news.tbsite.s_logo " +
				     "FROM news.tbsite INNER JOIN news.tbstructure ON news.tbstructure.id=news.tbsite.s_id " + 
					 "INNER JOIN news.tbnews n ON n.source_id=news.tbsite.s_id " +
					 "WHERE ? LIKE '%'|| s_url ||'%' AND n.news_url=?";
		try{
			return jdbcTemplate.queryForObject(sql, new Object[]{user_id, url, url}, new RowMapper<StructureDTO>() {

				@Override
				public StructureDTO mapRow(ResultSet rs, int rowNumber) throws SQLException {
					StructureDTO structure = new StructureDTO();
					
					structure.setId(rs.getInt("news_id"));
					structure.setSaved(rs.getBoolean("issaved"));
					structure.setContentSelector(rs.getString("content_selector"));
					structure.setTitleSelector(rs.getString("news_title"));
					structure.setHit(rs.getInt("news_hit"));
					structure.setContent(rs.getString("news_content"));
					structure.setImageSelector(rs.getString("news_img"));
					structure.setLogo(rs.getString("s_logo"));
					structure.setDate(rs.getTimestamp("news_date"));
					
					return structure;
				}
			});

		}catch (IncorrectResultSizeDataAccessException ex){
			return null;
		}
	}
	
	private NewsDTO readAKNNews(int id, int user_id){
		
		String sql = "SELECT (CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=?) THEN TRUE ELSE FALSE END) AS issaved, " +
					 "n.news_id, n.news_title, n.news_content, n.news_hit, n.news_img, n.news_date " + 
					 "FROM news.tbnews n WHERE n.news_id=? ";
		try{
			return jdbcTemplate.queryForObject(sql, new Object[]{user_id, id}, new RowMapper<NewsDTO>() {

				@Override
				public NewsDTO mapRow(ResultSet rs, int rowNumber) throws SQLException {
					NewsDTO news = new NewsDTO();
					news.setId(rs.getInt("news_id"));
					news.setContent(rs.getString("news_content"));
					news.setTitle(rs.getString("news_title"));
					news.setHit(rs.getInt("news_hit"));
					news.setSaved(rs.getBoolean("issaved"));
					news.setImage(rs.getString("news_img"));
					news.setDate(rs.getTimestamp("news_date"));
					return news;
				}
			});

		}catch (IncorrectResultSizeDataAccessException ex){
			return null;
		}
		
	}
	
	private NewsDTO readOtherNews(String url, int user_id){
		
		StructureDTO st = this.getSelector(url, user_id);
		NewsDTO news = new NewsDTO();
		
		try{
			Document doc = Jsoup.connect(url).timeout(30000).get();
			
			Elements elements = doc.select(st.getContentSelector());
			
			String content = "";
			
			for(Element element : elements){
				content += element.text() + "\n";
			}
			news.setId(st.getId());
			news.setContent(content);
			news.setTitle(st.getTitleSelector());
			news.setHit(st.getHit());
			news.setSaved(st.isSaved());
			news.setImage(st.getImageSelector());
			news.setDate(st.getDate());
			
			SiteDTO site = new SiteDTO();
			site.setLogo(st.getLogo());
			news.setSite(site);
			
		} catch (IOException e) {
			System.err.println("IOException Occurr + " + e.toString());
			
		} catch (Exception e){
			System.err.println("Exception Occurr + " + e.toString());
		}
		
		return news;
	}

	@Override
	public int[] scrapSite(int site_id) {
		
		ArrayList<NewsDTO> news = new ArrayList<NewsDTO>();
		
		ArrayList<StructureDTO> selectors = (ArrayList<StructureDTO>) this.getAllSelectors(site_id);
		
		System.out.println("Size : "+selectors.size());
		
		for(StructureDTO selector:selectors){
			System.out.println("=>> Scraping URL : " + selector.getUrl());
			news.addAll(scrapNews(selector));
			System.out.println("=>> Completed..!");
		}
		System.out.println("=>> Scraping DONE..!");
		
		int []insertRow =  scrapNewsToDatabase(news);
		
		System.out.println("=>> Insert Into Database Successfully..!");
		
		return insertRow;
		
	}
	
	private List<StructureDTO> getAllSelectors(int site_id){
		
		String sql = "SELECT sd.url, link_selector, rows_selector, image_selector, title_selector, site_id, category_id, s.s_prefix_img " +  
					 "FROM news.tbsite_detail sd INNER JOIN news.tbsite s ON sd.site_id=s.s_id " + 
					 "INNER JOIN news.tbstructure st ON st.id=s.s_id " + 
					 "WHERE sd.status=true AND site_id=?";
		
		return jdbcTemplate.query(sql, new Object[]{site_id}, new RowMapper<StructureDTO>() {

			@Override
			public StructureDTO mapRow(ResultSet rs, int rowNumber) throws SQLException {
				StructureDTO selectors = new StructureDTO();
				
				selectors.setUrl(rs.getString("url"));
				selectors.setCategoryId(rs.getInt("category_id"));
				selectors.setSiteId(rs.getInt("site_id"));
				selectors.setRowsSelector(rs.getString("rows_selector"));
				selectors.setLinkSelector(rs.getString("link_selector"));
				selectors.setTitleSelector(rs.getString("title_selector"));
				selectors.setImageSelector(rs.getString("image_selector"));
				selectors.setPrefixImg(rs.getString("s_prefix_img"));
				return selectors;
			}
		});
	}

	@Override
	public ArrayList<TestScrapDTO> testScrap(StructureDTO selector) {
		return this.testScrapURL(selector);
	}
	
	private ArrayList<TestScrapDTO> testScrapURL(StructureDTO selector){
		
		ArrayList<TestScrapDTO> news = new ArrayList<TestScrapDTO>();
		
		try {
			Document doc = Jsoup.connect(selector.getUrl()).timeout(30000).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").get();
			
			Elements elements = doc.select(selector.getRowsSelector());
			for(Element e:elements){
				
				String link = e.select(selector.getLinkSelector()).attr("href");
				
				if(link=="" || link==null)
					continue;

				String title = e.select(selector.getTitleSelector()).text();
				
				String image = e.select(selector.getImageSelector()).attr(selector.getPrefixImg()+"src");
				
				
				System.out.println(title+" "+ image + " " + link);
				
				TestScrapDTO n = new TestScrapDTO();
				n.setLink(link);
				n.setImage(image);
				n.setTitle(title);
		
				news.add(n);
			}
			
		} catch (IOException e) {
			System.err.println("IOException Occurr + " + e.toString());
			
		} catch (Exception e){
			System.err.println("Exception Occurr + " + e.toString());
		}
		
		return news;
	}

	@Override
	public String testScrapContent(StructureDTO selector){
		
		String content = "";
		try{
			Document doc = Jsoup.connect(selector.getUrl()).timeout(30000).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").get();
					
			Elements elements = doc.select(selector.getContentSelector());
			
			for(Element element : elements){
				content += element.text() + "\n";
			}
						
		} catch (IOException e) {
			System.err.println("IOException Occurr + " + e.toString());
			
		} catch (Exception e){
			System.err.println("Exception Occurr + " + e.toString());
		}
		return content;
	}
	
}
