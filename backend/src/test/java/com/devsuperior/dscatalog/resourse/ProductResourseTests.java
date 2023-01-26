package com.devsuperior.dscatalog.resourse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.resources.ProductResource;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)   //anotation para testes de Controladores que carregam a camada web
public class ProductResourseTests {           

	//objeto responsável pelas requisições (endpoints)
	@Autowired //injeção de dependências
	private MockMvc mockMvc;
	
	//simula o componente de dependência da classe, para testes unitários (sem testar a integração)
	@MockBean
	private ProductService service;
	
	private PageImpl<ProductDTO> page; //simula uma página com uma classe concreta de page
	private ProductDTO productDTO;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	
	@Autowired
	ObjectMapper objectmapper;
	
	//fixture, execute antes de cada
	@BeforeEach
	void setUp() throws Exception{
		
		//instaciação dos objetos
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		//simula o objeto mockado na chamada dos métodos do componente dependente
		//configura com qualquer argumento e seu retorno
		when(service.findAllPaged( ArgumentMatchers.any() ) ).thenReturn(page);
		
		when(service.findById(existingId)).thenReturn(productDTO); //buscar para id existente
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class); //Exceção para id Inexistente

		//função eq do Argm para usar valores específicos, quando o any
		when(service.update( ArgumentMatchers.eq(existingId), any() ) ).thenReturn(productDTO); //atualizar para id existente
		when(service.update( eq(nonExistingId), any() ) ).thenThrow(ResourceNotFoundException.class); //Exceção para id Inexistente
	
		when(service.insert(any())).thenReturn(productDTO);
		
		//métodos VOID, só chamam outro método
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DataBaseException.class).when(service).delete(dependentId);
		
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform( delete("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON)	);
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform( delete("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON)	);
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void insertShouldReturnProdctDTOCreated() throws Exception {
		
		String jsonBody = objectmapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform( post("/products")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExists() throws Exception{
		
		//converte obj productDTO em texto (para ser enviado no formato JSON)
		String jasonBody = objectmapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jasonBody) // informa a String da requisição (obj a ser atualizado em formato de texto)
				.contentType(MediaType.APPLICATION_JSON) // informa o tipo de contúdo/texto da String
				.accept(MediaType.APPLICATION_JSON)); //tipo da resposta enviada

		//Assertions
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		//converte obj productDTO em texto (para ser enviado no formato JSON)
		String jasonBody = objectmapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jasonBody) // informa a String da requisição (obj a ser atualizado em formato de texto)
				.contentType(MediaType.APPLICATION_JSON) // informa o tipo de contúdo/texto da String
				.accept(MediaType.APPLICATION_JSON)); //tipo da resposta enviada

		//Assertions
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findAllPagedShouldReturnPage() throws Exception{
		//simula o método http get através perform do mokvc
		//informa que o método aceitará o tipo JSON
		// ACT
		ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		
		//ASSERTION
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnProductDtoWhenIdExists() throws Exception{
		//endpoint findById
		ResultActions result = mockMvc.perform(get("/products/{id}",existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		//Assertions
		result.andExpect(status().isOk()); //http 200
		
		//acessa o objeto json ($) e verifica a existencia dos parâmetros
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		//endpoint findById
		ResultActions result = mockMvc.perform(get("/products/{id}",nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		//Assertions
		result.andExpect(status().isNotFound()); //http 404
	}
	
}//end
