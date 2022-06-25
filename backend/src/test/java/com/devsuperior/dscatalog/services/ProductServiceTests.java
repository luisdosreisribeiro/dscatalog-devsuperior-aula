package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;	
	
	@Mock
	private CategoryRepository categoryRepository;
	

	
	private long nonExistingId;
	private long existingId;
	private long dependentId;
	private PageImpl<Product>page;
	private Product product;
	private Category category;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId  = 1L;
		nonExistingId = 2;
		dependentId = 3L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		category = Factory.cretateCategory();
		
		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);	
		
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(productRepository.getOne(existingId)).thenReturn(product);
		Mockito.when(productRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.doNothing().when(productRepository).deleteById(existingId);		
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDependentId() {				
		Assertions.assertThrows(DatabaseException.class,()->{
			productService.delete(dependentId);
		});
		
		Mockito.verify(productRepository).deleteById(dependentId);
	}
	
	
	
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {				
		Assertions.assertDoesNotThrow(()->{
			productService.delete(existingId);
		});
		
		Mockito.verify(productRepository).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {				
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			productService.delete(nonExistingId);
		});
		
		Mockito.verify(productRepository).deleteById(nonExistingId);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0,10);
		
		Page<ProductDTO> result = productService.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		//Mockito.verify(productRepository,Mockito.times(1)).findAll(pageable);
		Mockito.verify(productRepository).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = productService.findById(existingId);
		
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesExist() {
			
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			productService.findById(nonExistingId);
		});
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO productDTO = Factory.createProductDTO();
		
		ProductDTO result =  productService.update(existingId,productDTO );
		
		Assertions.assertNotNull(productDTO);
	}
	
	@Test
	public void updateIdShouldThrowResourceNotFoundExceptionWhenIdDoesExist() {
		ProductDTO productDTO = Factory.createProductDTO();
		
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			productService.update(nonExistingId, productDTO);
		});
		
	}

}
