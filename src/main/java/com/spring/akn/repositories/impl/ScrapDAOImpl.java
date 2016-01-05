package com.spring.akn.repositories.impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.scrap.ScrapNewsDTO;
import com.spring.akn.entities.scrap.StructureDTO;
import com.spring.akn.repositories.ScrapDAO;

@Repository
public class ScrapDAOImpl implements ScrapDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public ArrayList<ScrapNewsDTO> scrapAllSites() {
		
		ArrayList<ScrapNewsDTO> news = new ArrayList<ScrapNewsDTO>();
		
		ArrayList<StructureDTO> selectors = (ArrayList<StructureDTO>) this.getAllSelectors();
		
		for(StructureDTO selector:selectors){
			news.addAll(scrapNews(selector));
		}
		
		return news;
	}

	
	private List<StructureDTO> getAllSelectors(){
		
		String sql = "SELECT sd.url, rows_selector,image_selector,title_selector,desc_selector "  
					 + "FROM tbsite_detail sd INNER JOIN tbsite s ON sd.site_id=s.s_id " 
					 + "INNER JOIN tbstructure st ON st.id=s.s_id";
		
		return jdbcTemplate.query(sql, new RowMapper<StructureDTO>() {

			@Override
			public StructureDTO mapRow(ResultSet rs, int rowNumber) throws SQLException {
				StructureDTO selectors = new StructureDTO();
				
				selectors.setUrl(rs.getString("url"));
				selectors.setRowsSelector(rs.getString("rows_selector"));
				selectors.setLinkSelector(rs.getString("link_selector"));
				selectors.setTitleSelector(rs.getString("title_selector"));
				selectors.setImageSelector(rs.getString("image_selector"));
				selectors.setDescriptionSelector(rs.getString("desc_selector"));
				
				return selectors;
			}
		});
		
	}
	
	private ArrayList<ScrapNewsDTO> scrapNews(StructureDTO selector){
		
		ArrayList<ScrapNewsDTO> news = new ArrayList<ScrapNewsDTO>();
		
		try {
			Document doc = Jsoup.connect(selector.getUrl()).timeout(30000).get();
			
			Elements elements = doc.select(selector.getRowsSelector());
			for(Element e:elements){
				
				String title = e.select(selector.getTitleSelector()).text();
				String description = e.select(selector.getDescriptionSelector()).text();
				
				String image = e.select(selector.getImageSelector()).attr("src");
				String link = e.select(selector.getLinkSelector()).attr("href");
				
				news.add(new ScrapNewsDTO(link, image, title, description));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return news;
	}
	
}
