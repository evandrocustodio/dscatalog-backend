package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.Factory;
import com.devsuperior.dscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {

	private Long existingId; 
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@Autowired
	private ProductRepository productRepository; 	
	
	@BeforeEach
	void setUp() throws Exception{		
		existingId =1L;
		nonExistingId =1000L;
		countTotalProducts= 25L;
	}
	
	@Test
	public void saveShoudPersistWithAutoincrementWhenIdIsNull() {		
		Product product = Factory.createProduct();
		product.setId(null);		
		product = this.productRepository.save(product);		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(product.getId(), countTotalProducts+1);	
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		this.productRepository.deleteById(existingId);		
		Optional<Product> product = this.productRepository.findById(existingId);		
		Assertions.assertFalse(product.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {			
			this.productRepository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnOneOptionalObjectWhenIdExists() {
		Optional<Product> product = this.productRepository.findById(existingId);		
		Assertions.assertTrue(product.isPresent());
	}
	
	@Test
	public void findByIdShouldNotReturnOneOptionalObjectWhenIdDoesNotExists() {
		Optional<Product> product = this.productRepository.findById(nonExistingId);		
		Assertions.assertFalse(product.isPresent());
	}
	
}
