package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;

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

import com.devsuperior.dscatalog.Factory;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.entities.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;

		page = new PageImpl<>(List.of(Factory.createProduct()));

		// Simulando FindAll
		Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		// Simulando Save
		Mockito.when(productRepository.save((Product) ArgumentMatchers.any())).thenReturn(Factory.createProduct());

		// Simulando FindById
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(Factory.createProduct()));
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

		// Simulando Delete
		Mockito.doNothing().when(productRepository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

	}

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			productService.delete(existingId);
		});

		Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowEmptyResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(nonExistingId);
		});

		Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldThrowEmptyDatabaseExceptionWhenDependentId() {

		Assertions.assertThrows(DatabaseException.class, () -> {
			productService.delete(dependentId);
		});

		Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
	}

	@Test
	public void findByIdShouldReturnObjectProductDTOWhenIdExists() {			
		Assertions.assertDoesNotThrow(() ->{
        	productService.findById(existingId);
		});
	}
	
	@Test
	public void findByIdShouldThrowEmptyResourceNotFoundExceptionWhenIdDoesNotExists() {			
		Assertions.assertThrows(ResourceNotFoundException.class, () ->{
        	productService.findById(nonExistingId);
		});
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = productService.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
		
	}
}
