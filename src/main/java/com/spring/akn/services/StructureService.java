package com.spring.akn.services;

import java.util.List;

import com.spring.akn.entities.scrap.StructureDTO;

public interface StructureService {
	
	public boolean addStructure(StructureDTO structure);
	
	public boolean deleteStructure(int id);
	
	public boolean updateStructure(StructureDTO structure);
	
	public List<StructureDTO> getStructures();
	
	public StructureDTO getStructure(int id);
}
