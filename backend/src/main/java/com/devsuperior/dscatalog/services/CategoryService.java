package com.devsuperior.dscatalog.services;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
    
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable){
		Page<Category>list= categoryRepository.findAll(pageable);
		
		return list.map(x -> new CategoryDTO(x));
		
		
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = categoryRepository.findById(id);
		
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entidade não encontrada"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		com.devsuperior.dscatalog.entities.Category entity =  new com.devsuperior.dscatalog.entities.Category();
		entity.setName(dto.getName());
		entity = categoryRepository.save(entity);
		return new CategoryDTO(entity);
		
		
		
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = categoryRepository.getOne(id);
			entity.setName(dto.getName());
			entity = categoryRepository.save(entity);
			return new CategoryDTO(entity);	
			
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException ("Id not found "+ id);
		}
		
	}

	public void update(Long id) {
		try {
			categoryRepository.deleteById(id);
		} 
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found "+ id);
			
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
			
		}
		
		
	}
}
