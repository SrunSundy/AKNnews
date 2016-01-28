package com.spring.akn.repositories;

import java.util.List;

import com.spring.akn.entities.scrap.StructureDTO;

public interface StructureDAO {
	
	public int addStructure(StructureDTO structure);
	
	public int deleteStructure(int id);
	
	public int updateStructure(StructureDTO structure);
	
	public List<StructureDTO> getStructures();
	
	public StructureDTO getStructure(int id);
}
