package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	//Classe a ser testada (teste Unitário)
	@InjectMocks
	ProductService service;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page; //classe concreta de implementação de páginas, usada pra testes de páginas
	private Product product;
	private ProductDTO dto;
	private Category category;
	
	//Fixture
	@BeforeEach
	void setup() throws Exception{
		//valores para representar o comportamento do obj Mockado nos testes
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product)); //implementado por LIST
		dto = Factory.createProductDTO();
		category = Factory.createCategory();

		//configura o comportamento do Objeto Mockado para métodos com Algum Tipo de RETORNO
		//when(quando) + ArgumentMatchers(qualquer arg) + thenReturn(então retorne)
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page); //necessário CAST pelo tipo específico de PAGEABLE
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		when(repository.findById(existingId)).thenReturn(Optional.of(product)); //retorna o obj present no Optional do ID existente
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty()); //retorna vazio quando ID não existir
		
		when(repository.getOne(existingId)).thenReturn(product);
		when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		when(categoryRepository.getOne(existingId)).thenReturn(category);
		when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		//configura o comportamento do Objeto Mockado para métodos VOID
		//ação(faça) + when(quando)
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId); //forma simplificada de Mockito.doThrow(), exige import diferent
	}
	
	//assume o lugar da dependência para teste unitário, que não carrega o contexto(MockBean do Spring)
	@Mock
	ProductRepository repository;
	
	@Mock
	CategoryRepository categoryRepository;
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.update(nonExistingId, dto);
		});
	}
	
	@Test
	public void updateShouldReturnsProductDtoWhenIdExist() {
		ProductDTO result = service.update(existingId, dto);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.findById(nonExistingId);
		});
		Mockito.verify(repository).findById(nonExistingId);
	}
	
	@Test
	public void findByIdShoulReturnsProductDtoWhenIdExist() {
		ProductDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
		Assertions.assertSame(ProductDTO.class, result.getClass());
		
		Mockito.verify(repository).findById(existingId);
	}
	
	@Test
	public void findAllPagedShouldReturnsPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependentId() {
		Assertions.assertThrows(DataBaseException.class, ()->{
			service.delete(dependentId);
		}); 
		
		verify(repository, times(1)).deleteById(dependentId);//forma simplificada de Mockito.verify(), exige import diferent
	} 
	
	@Test
	public void deleteDoNothingWhenidExists() {
		Assertions.assertDoesNotThrow(()->{
			service.delete(existingId);
		});
		
		Mockito.verify(repository, times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.delete(nonExistingId);
		});
		
		verify(repository).deleteById(nonExistingId);
	}
	
}//end
