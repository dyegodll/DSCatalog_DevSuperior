package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	//Classe a ser testada (teste Unitário)
	@InjectMocks
	ProductService service;
	
	private Long existingId;
	private Long nonExistingId;
	
	//Fixture
	@BeforeEach
	void setup() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		
		//configura o Objeto Mockado
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
	}
	
	//assume o lugar da dependência para teste unitário que não carrega contexto
	@Mock
	ProductRepository repository;
	
	@Test
	public void deleteDoNothingWhenidExists() {
		Assertions.assertDoesNotThrow(()->{
			service.delete(existingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
}//end
