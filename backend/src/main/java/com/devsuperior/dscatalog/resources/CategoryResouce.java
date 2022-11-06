 package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResouce {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> list = service.findAll();

		// o ResponseEntity é uma classe que convert a resposta para HTTP
		return ResponseEntity.ok().body(list); //passa a lista como resposta
	}

	@GetMapping(value = "/{id}") //sequencia do endpoint categories (ex: host/categories/1)
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){ //anotation vincula o endpoit ao parâmetro do método
		CategoryDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto); 
	}
}
