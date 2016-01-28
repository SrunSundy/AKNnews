package com.spring.akn.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.scrap.StructureDTO;
import com.spring.akn.repositories.StructureDAO;
import com.spring.akn.services.StructureService;

@Service
public class StructureServiceImpl implements StructureService{
	
	@Autowired
	StructureDAO structureDAO;
	
	@Override
	public boolean addStructure(StructureDTO structure) {
		return structureDAO.addStructure(structure)==1?true:false;
	}

	@Override
	public boolean deleteStructure(int id) {
		return structureDAO.deleteStructure(id)==1?true:false;
	}

	@Override
	public boolean updateStructure(StructureDTO structure) {
		return structureDAO.updateStructure(structure)==1?true:false;
	}

	@Override
	public List<StructureDTO> getStructures() {
		return structureDAO.getStructures();
	}

	@Override
	public StructureDTO getStructure(int id) {
		return structureDAO.getStructure(id);
	}

}
