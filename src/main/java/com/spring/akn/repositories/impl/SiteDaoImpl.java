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
		String sql = "SELECT s_id, s_name, s_url FROM tbsite;";
		return getJdbcTemplate().query(sql , new SiteRowMapper() );
	}

	@Override
	public SiteDTO findSiteById(int id) {
		String sql = "SELECT s_id, s_name, s_url FROM tbsite WHERE s_id = ?;";
		return getJdbcTemplate().query(sql , new Object[]{id} , new SiteResultSetExtractor());
	}

	@Override
	public boolean isDeleteSiteById(int id) {
		String sql = "DELETE FROM tbsite WHERE s_id = ? ;";
		int result = getJdbcTemplate().update(sql , new Object[]{id});
		if (result > 0)
			return true;
		return false;
	}

	@Override
	public boolean isInsertSite(SiteDTO siteDTO) {
		String sql = "INSERT INTO tbsite(s_name, s_url) VALUES(?,?)";
		int result = getJdbcTemplate().update(sql , new Object[]{siteDTO.getName(), siteDTO.getUrl()});
		if (result > 0)
			return true;
		return false;
	}

	@Override
	public boolean isUpdateSite(SiteDTO siteDTO) {
		String sql = "UPDATE tbsite SET s_name=?, s_url=? WHERE s_id = ? ;";
		int result = getJdbcTemplate().update(sql , new Object[]{siteDTO.getName(), siteDTO.getUrl(), siteDTO.getId()});
		if (result > 0)
			return true;
		return false;
	}

	@Override
	public int countSiteRecord() {		
		String sql = "SELECT COUNT(*) FROM tbsite;";
		return getJdbcTemplate().queryForObject(sql , Integer.class);
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
	            
	            
				if ( getLogoName(s_id) == null){
		            //for random file name
		            randowFileName=uuid+"."+extension;
				}else{
					randowFileName = getLogoName(s_id);
				}
	            
	            
 	            String filename =randowFileName;
				byte[] bytes = file.getBytes();

				// creating the directory to store file
				String savePath = request.getSession().getServletContext().getRealPath("/resources/images/");
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
	public boolean isUpdateLogo(MultipartFile file, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isDeleteLogo(MultipartFile file, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}
	
/*	public void insertLogoName(String name, int s_id){
		String sql = "INSERT INTO tbsite(s_logo) VALUES(?) ;";
		getJdbcTemplate().update(sql , new Object[]{name});
	}*/
	
	public int editLogoName(int s_id, String name){
		String sql = "UPDATE tbsite SET s_logo = ? WHERE s_id = ?;";
		return getJdbcTemplate().update(sql , new Object[]{name , s_id});
	}
	
	public String getLogoName(int s_id){
		String sql = "SELECT s_logo FROM tbsite WHERE (s_id = ?);";
		return getJdbcTemplate().queryForObject(sql, new Object[]{ s_id }, String.class);
	}
	
	
	public static final class SiteRowMapper implements RowMapper<SiteDTO>{
		@Override
		public SiteDTO mapRow(ResultSet rs, int num) throws SQLException {
			SiteDTO dto= new SiteDTO();
			dto.setId(rs.getInt("s_id"));
			dto.setName(rs.getString("s_name"));
			dto.setUrl(rs.getString("s_url"));
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
				return dto;
			}
			return null;
		}
		
	}


}
