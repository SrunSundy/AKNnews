package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.scrap.StructureDTO;
import com.spring.akn.repositories.StructureDAO;

@Repository
public class StructureDAOImpl implements StructureDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int addStructure(StructureDTO structure) {
		return jdbcTemplate.update("INSERT INTO tbStructure(id, rows_selector, image_selector, link_selector, title_selector, content_selector) VALUES(?,?,?,?,?,?)",
				structure.getSiteId(),structure.getRowsSelector(), structure.getImageSelector(), structure.getLinkSelector(), structure.getTitleSelector(), structure.getContentSelector());
	}

	@Override
	public int deleteStructure(int id) {
		return jdbcTemplate.update("DELETE FROM tbStructure WHERE id=?",id);
	}

	@Override
	public int updateStructure(StructureDTO structure) {
		return jdbcTemplate.update("UPDATE tbStructure SET rows_selector=?, image_selector=?, link_selector=?, title_selector=?, content_selector=? WHERE id=?", 
				structure.getRowsSelector(), structure.getImageSelector(), structure.getLinkSelector(), structure.getTitleSelector(),structure.getContentSelector(), structure.getId());
	}

	@Override
	public List<StructureDTO> getStructures() {
		return jdbcTemplate.query("SELECT id, rows_selector, image_selector, link_selector, title_selector, content_selector FROM tbStructure", new RowMapper<StructureDTO>() {

			@Override
			public StructureDTO mapRow(ResultSet rs, int rowNumber) throws SQLException {
				StructureDTO structure = new StructureDTO();
				structure.setId(rs.getInt("id"));
				structure.setRowsSelector(rs.getString("rows_selector"));
				structure.setImageSelector(rs.getString("image_selector"));
				structure.setLinkSelector(rs.getString("link_selector"));
				structure.setTitleSelector(rs.getString("title_selector"));
				structure.setContentSelector(rs.getString("content_selector"));
				return structure;
			}
		});
	}

	@Override
	public StructureDTO getStructure(int id) {
		try {
			return jdbcTemplate.queryForObject("SELECT id, rows_selector, image_selector, link_selector, title_selector, content_selector FROM tbStructure WHERE id=?", 
					new Object[]{id}, new RowMapper<StructureDTO>() {

				@Override
				public StructureDTO mapRow(ResultSet rs, int rowNumber) throws SQLException {
					StructureDTO structure = new StructureDTO();
					structure.setId(rs.getInt("id"));
					structure.setRowsSelector(rs.getString("rows_selector"));
					structure.setImageSelector(rs.getString("image_selector"));
					structure.setLinkSelector(rs.getString("link_selector"));
					structure.setTitleSelector(rs.getString("title_selector"));
					structure.setContentSelector(rs.getString("content_selector"));
					return structure;
				}
			});
		} catch (IncorrectResultSizeDataAccessException e) {
				System.out.println("ERROR : " + e.toString());
				return null;
		}
	}

}	
