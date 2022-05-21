package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
    
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<com.devsuperior.dscatalog.entities.Category>list= categoryRepository.findAll();
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
		
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<com.devsuperior.dscatalog.entities.Category> obj = categoryRepository.findById(id);
		com.devsuperior.dscatalog.entities.Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entidade não encontrada"));
		return new CategoryDTO(entity);
	}
}
