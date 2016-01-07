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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SiteDTO;
import com.spring.akn.entities.scrap.StructureDTO;
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
		
		String sql = "INSERT INTO tbnews(news_title, news_description, news_img, news_url, category_id, source_id) "+
			  "SELECT ?, ?, ?, ?, ?, ? " +
			  "WHERE NOT EXISTS(SELECT news_url FROM tbnews WHERE news_url=?)";
		
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
						NewsDTO n = news.get(i);
						ps.setString(1, n.getTitle());
						ps.setString(2, n.getDescription());
						ps.setString(3, n.getImage());
						ps.setString(4, n.getUrl());
						ps.setInt(5, n.getCategory().getId());
						ps.setInt(6, n.getSite().getId());
						ps.setString(7, n.getUrl());
			}
			
			@Override
			public int getBatchSize() {
				return news.size();
			}
		});
	}
	
	private List<StructureDTO> getAllSelectors(){
		
		String sql = "SELECT sd.url, link_selector, rows_selector, image_selector, title_selector, desc_selector, site_id, category_id " +  
					 "FROM tbsite_detail sd INNER JOIN tbsite s ON sd.site_id=s.s_id " + 
					 "INNER JOIN tbstructure st ON st.id=s.s_id " + 
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
				selectors.setDescriptionSelector(rs.getString("desc_selector"));
				
				return selectors;
			}
		});
		
	}
	
	private ArrayList<NewsDTO> scrapNews(StructureDTO selector){
		
		ArrayList<NewsDTO> news = new ArrayList<NewsDTO>();
		
		try {
			Document doc = Jsoup.connect(selector.getUrl()).timeout(30000).get();
			
			Elements elements = doc.select(selector.getRowsSelector());
			for(Element e:elements){
				
				String title = e.select(selector.getTitleSelector()).text();
				String description = e.select(selector.getDescriptionSelector()).text();
				
				String image = e.select(selector.getImageSelector()).attr("src");
				String link = e.select(selector.getLinkSelector()).attr("href");
				
				NewsDTO n = new NewsDTO();
				n.setUrl(link);
				n.setImage(image);
				n.setTitle(title);
				n.setDescription(description);
				
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
	public NewsDTO scrapNews(String url, int user_id) {
		
		if(!url.contains("news.khmeracademy.org"))
			return readOtherNews(url, user_id);
		
		return readAKNNews(url, user_id);
	
	}
	
	private StructureDTO getSelector(String url, int user_id){
		
		String sql = "SELECT (CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS issaved, " +
					 "content_selector, n.news_title, n.news_content, n.news_hit " +
				     "FROM tbsite INNER JOIN tbstructure ON tbstructure.id=tbsite.s_id " + 
					 "INNER JOIN tbnews n ON n.source_id=tbsite.s_id " +
					 "WHERE ? LIKE '%'|| s_url ||'%' AND n.news_url=?";
		try{
			return jdbcTemplate.queryForObject(sql, new Object[]{user_id, url, url}, new RowMapper<StructureDTO>() {

				@Override
				public StructureDTO mapRow(ResultSet rs, int rowNumber) throws SQLException {
					StructureDTO structure = new StructureDTO();
					
					structure.setSaved(rs.getBoolean("issaved"));
					structure.setContentSelector(rs.getString("content_selector"));
					structure.setTitleSelector(rs.getString("news_title"));
					structure.setHit(rs.getInt("news_hit"));
					structure.setContent(rs.getString("news_content"));
					
					return structure;
				}
			});

		}catch (Exception ex){
			return null;
		}
	}
	
	private NewsDTO readAKNNews(String url, int user_id){
		StructureDTO st = this.getSelector(url, user_id);
		
		NewsDTO news = new NewsDTO();
		news.setContent(st.getContent());
		news.setTitle(st.getTitleSelector());
		news.setHit(st.getHit());
		news.setSaved(st.isSaved());
		return news;
	}
	
	private NewsDTO readOtherNews(String url, int user_id){
		
		StructureDTO st = this.getSelector(url, user_id);
		NewsDTO news = new NewsDTO();
		
		try{
			Document doc = Jsoup.connect(url).timeout(30000).get();
			
			String content = doc.select(st.getContentSelector()).text();
			
			news.setContent(content);
			news.setTitle(st.getTitleSelector());
			news.setHit(st.getHit());
			news.setSaved(st.isSaved());
			
		} catch (IOException e) {
			System.err.println("IOException Occurr + " + e.toString());
			
		} catch (Exception e){
			System.err.println("Exception Occurr + " + e.toString());
		}
		
		return news;
	}
	
}
