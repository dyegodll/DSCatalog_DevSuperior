 package com.devsuperior.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResouce {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping //anotation para recuperar os dados
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> list = service.findAll();

		// o ResponseEntity é uma classe que convert a resposta para HTTP
		return ResponseEntity.ok().body(list); //passa a lista como resposta
	}

	@GetMapping(value = "/{id}") //sequencia do endpoint categories (ex: host/categories/1) //@PathVariable vincula o endpoit ao parâmetro do método
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){ 
		CategoryDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto); 
	}
	
	@PostMapping //anotation para inserir os dados, obs: usa o endpoint principal ("/categories") mas através do método POST 
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){ 
		dto = service.insert(dto); //na inserção o repositorio retorna um obj com o ID
		
		//quando se trata de inserir, convém mostrar o caminho da localização do obj
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		
		//retorna a requisição com o cód 201(obj criado) e o obj no corpo da resposta
		return ResponseEntity.created(uri).body(dto); 
	}
}
