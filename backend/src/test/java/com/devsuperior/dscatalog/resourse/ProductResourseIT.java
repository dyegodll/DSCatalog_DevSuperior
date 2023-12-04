package com.devsuperior.dscatalog.resourse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourseIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	ObjectMapper objectmapper; //converte o obj em texto para ser enviado no formato JSON
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	private String username;
	private String password;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		username = "maria@gmail.com";
		password = "123456";
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ProductDTO productDTO = Factory.createProductDTO();

		//objectmapper converte o obj productDTO em texto para ser enviado no formato JSON
		String jasonBody = objectmapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jasonBody) // informa a String da requisição (obj a ser atualizado em formato de texto)
				.contentType(MediaType.APPLICATION_JSON) // informa o tipo de contúdo/texto da String
				.accept(MediaType.APPLICATION_JSON)); //tipo da resposta enviada

		//Assertions
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExists() throws Exception{
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ProductDTO productDTO = Factory.createProductDTO();
		String expectedName = productDTO.getName();
		String expectedDescription = productDTO.getDescription();

		//objectmapper converte o obj productDTO em texto para ser enviado no formato JSON
		String jasonBody = objectmapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jasonBody) // informa a String da requisição (obj a ser atualizado em formato de texto)
				.contentType(MediaType.APPLICATION_JSON) // informa o tipo de contúdo/texto da String
				.accept(MediaType.APPLICATION_JSON)); //tipo da resposta enviada

		//Assertions
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() throws Exception{
		ResultActions result = mockMvc.perform( get("/products?page=0&size=5&sort=name,asc")
								.accept(MediaType.APPLICATION_JSON) )//negociação de conteúdo da resposta (formato JSON)
								//Assertion
								.andExpect(status().isOk()); //outra forma de teste, sem usar o ResultActions
		
		//Assertions
		result.andExpect(jsonPath("$.content").exists()); //verifica se existe conteúdo na pág
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		
		//verifica se os elementos estão em ordem ascendente na ' Página ' (content)
		result.andExpect( jsonPath("$.content[0].name").value("Macbook Pro") ); //1º elemento da lista da 1ª pág
		result.andExpect( jsonPath("$.content[0].id").value(3) ); //ID do 1º elemento da lista da pág ordenada
		
		result.andExpect( jsonPath("$.content[1].name").value("PC Gamer") ); //2º
		result.andExpect( jsonPath("$.content[2].name").value("PC Gamer Alfa") );//3º
	}
	
}//end
