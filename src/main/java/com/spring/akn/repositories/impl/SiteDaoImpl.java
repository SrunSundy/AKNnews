package com.spring.akn.repositories.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.akn.entities.SiteDTO;
import com.spring.akn.repositories.SiteDAO;

@Repository
public class SiteDaoImpl implements SiteDAO {
	
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<SiteDTO> listSite() {
		String sql = "SELECT s_id, s_name, s_url, s_logo,s_basepath, s_prefix_img, s_prefix_link FROM news.tbsite ORDER BY s_id ASC;";
		return getJdbcTemplate().query(sql , new SiteRowMapper() );
	}

	@Override
	public SiteDTO findSiteById(int id) {
		String sql = "SELECT s_id, s_name, s_url , s_logo, s_basepath, s_prefix_img, s_prefix_link FROM news.tbsite WHERE s_id = ?;";
		return getJdbcTemplate().query(sql , new Object[]{id} , new SiteResultSetExtractor());
	}

	@Override
	public boolean isDeleteSiteById(int id) {
		String sql = "DELETE FROM news.tbsite WHERE s_id = ? AND (SELECT source_id FROM news.tbnews WHERE news.tbnews.source_id = ?) ISNULL ;";
		int result = getJdbcTemplate().update(sql , new Object[]{id,id});
		if (result > 0)
			return true;
		return false;
	}

	@Override
	public boolean isInsertSite(SiteDTO siteDTO) {
		String sql = "INSERT INTO news.tbsite(s_name, s_url, s_basepath, s_prefix_img, s_prefix_link) VALUES(?,?,?,?,?)";
		int result = getJdbcTemplate().update(sql , new Object[]{siteDTO.getName(), siteDTO.getUrl(),siteDTO.getBasepath(), siteDTO.getPrefixImg(), siteDTO.getPrefixLink()});
		if (result > 0)
			return true;
		return false;
	}

	@Override
	public boolean isUpdateSite(SiteDTO siteDTO) {
		String sql = "UPDATE news.tbsite SET s_name=?, s_url=? ,s_basepath=?, s_prefix_img=?, s_prefix_link=? WHERE s_id = ?";
		int result = getJdbcTemplate().update(sql , new Object[]{siteDTO.getName(), siteDTO.getUrl(), siteDTO.getBasepath(), siteDTO.getPrefixImg(), siteDTO.getPrefixLink(), siteDTO.getId()});
		if (result > 0)
			return true;
		return false;
	}

	@Override
	public int countSiteRecord() {		
		String sql = "SELECT COUNT(*) FROM news.tbsite;";
		return getJdbcTemplate().queryForObject(sql , Integer.class);
	}
	
	@Override
	public boolean updateSiteBasePath(int id,String basePath) {
		String sql = "UPDATE news.tbsite SET s_basepath= ? WHERE s_id = ?";
		int result = getJdbcTemplate().update(sql, new Object[]{basePath, id});
		if (result > 0)
			return true;
		return false;
	}
	
	public boolean isUUID(String filename){
		//String filename = "4a6978a5-ec94-484f-88c1-51f3ee52221e.png"; //define it!
		if ( filename == null)
			return false;
		String[] fn = filename.split("\\.");
		System.out.println(fn[0]);		
		if (fn[0].matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
			return true;
		}
		return false;
	}
	@Override
	public boolean isUploadLogo(MultipartFile file, HttpServletRequest request, int s_id) {
		if(!file.isEmpty()){
			try{
				

				UUID uuid = UUID.randomUUID();
	            String originalFilename = file.getOriginalFilename(); 
	            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")+1);

	            //for random file name
	            String randowFileName = null;
	            
	            
				if (!isUUID(getLogoName(s_id)) ){
		            //for random file name
		            randowFileName=uuid+"."+extension;
				}else{
					randowFileName = getLogoName(s_id);
				}
	            
	            
 	            String filename =randowFileName;
				byte[] bytes = file.getBytes();

				// creating the directory to store file
				String savePath = request.getSession().getServletContext().getRealPath("/resources/images/logo/");
				System.out.println(savePath);
				File path = new File(savePath);
				if(!path.exists()){
					path.mkdir();
				}

				// creating the file on server
				File serverFile = new File(savePath + File.separator + filename );
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				
				System.out.println(serverFile.getAbsolutePath());
				System.out.println("You are successfully uploaded file " + filename);
				
				//insert img name in table
				if ( editLogoName(s_id, filename) > 0 ) {
					System.out.println("Insert logo success!");
					return true;
				}
				else 
					System.out.println("Insert logo fail!");
				
			}catch(Exception e){
				System.out.println("You are failed to upload  => " + e.getMessage());
			}
		}else{
			System.err.println("File not found");
		}
		
		return false;
	}
	@Override
	public int editLogoName(int s_id, String name){
		String sql = "UPDATE news.tbsite SET s_logo = ? WHERE s_id = ?;";
		return getJdbcTemplate().update(sql , new Object[]{name , s_id});
	}
	@Override
	public String getLogoName(int s_id){
		String sql = "SELECT s_logo FROM news.tbsite WHERE (s_id = ?);";
		return getJdbcTemplate().queryForObject(sql, new Object[]{ s_id }, String.class);
	}
	
	
	public static final class SiteRowMapper implements RowMapper<SiteDTO>{
		@Override
		public SiteDTO mapRow(ResultSet rs, int num) throws SQLException {
			SiteDTO dto= new SiteDTO();
			dto.setId(rs.getInt("s_id"));
			dto.setName(rs.getString("s_name"));
			dto.setUrl(rs.getString("s_url"));
			dto.setLogo(rs.getString("s_logo"));
			dto.setBasepath(rs.getString("s_basepath"));
			dto.setPrefixImg(rs.getString("s_prefix_img"));
			dto.setPrefixLink(rs.getString("s_prefix_link"));
			return dto;
		}		
	}
	
	public static final class SiteResultSetExtractor implements ResultSetExtractor<SiteDTO>{
		@Override
		public SiteDTO extractData(ResultSet rs) throws SQLException, DataAccessException {
			while(rs.next()){
				SiteDTO dto = new SiteDTO();
				dto.setId(rs.getInt("s_id"));
				dto.setName(rs.getString("s_name"));
				dto.setUrl(rs.getString("s_url"));
				dto.setLogo(rs.getString("s_logo"));
				dto.setBasepath(rs.getString("s_basepath"));
				dto.setPrefixImg(rs.getString("s_prefix_img"));
				dto.setPrefixLink(rs.getString("s_prefix_link"));
				return dto;
			}
			return null;
		}
	}

	public static final class SiteResultSetExtractorExist implements ResultSetExtractor<SiteDTO>{
		@Override
		public SiteDTO extractData(ResultSet rs) throws SQLException, DataAccessException {
			while(rs.next()){
				SiteDTO dto = new SiteDTO();
				dto.setId(rs.getInt("s_id"));
				return dto;
			}
			return null;
		}
	}
	
	
	@Override
	public SiteDTO checkExistSite(int id) {
		String sql = "SELECT DISTINCT news.tbnews.source_id as s_id FROM news.tbnews WHERE source_id = ?";
		return getJdbcTemplate().query(sql , new Object[]{id} , new SiteResultSetExtractorExist());
	}




}
