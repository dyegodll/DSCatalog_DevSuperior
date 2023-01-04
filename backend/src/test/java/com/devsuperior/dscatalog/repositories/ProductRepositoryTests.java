package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.ProductFactory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long inexistingId;
	private Long countTotalProduct;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		inexistingId = 100L;
		countTotalProduct = 25L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenInexistentId() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
			repository.deleteById(inexistingId);
		});
	}

	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product p = ProductFactory.createProduct();
		p.setId(null);
		
		p = repository.save(p);
		
		Assertions.assertNotNull(p.getId());
		Assertions.assertEquals(countTotalProduct + 1, p.getId());
	}
	
	@Test
	public void findByIdShouldReturnsOptionalProductNonEmptyWhenIdExists() {
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnsOptionalProductEmptyWhenIdDoesNotExists() {
		Optional<Product> result = repository.findById(inexistingId);
		Assertions.assertTrue(result.isEmpty());
	}
}
