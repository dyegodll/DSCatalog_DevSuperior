 package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductResouce {
	
	@Autowired
	private ProductService service;
	
	//EndPoint de Paginação
	@GetMapping //anotation para recuperar os dados
	public ResponseEntity<Page<ProductDTO>> findAllPaged(
			//parâmetros da paginação
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy
			){
		
		//instancia a variável pelo builder da classe com os parâmetros declarados, obs: direction deve ser convertido para o tipo ENUM
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<ProductDTO> list = service.findAllPaged(pageRequest);

		// o ResponseEntity é uma classe que convert a resposta para HTTP
		return ResponseEntity.ok().body(list); //passa a lista como resposta
	}

	@GetMapping(value = "/{id}") //sequencia do endpoint categories (ex: host/categories/1) //@PathVariable vincula o endpoit ao parâmetro do método
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){ 
		ProductDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto); 
	}
	
	@PostMapping //anotation para inserir os dados, obs: usa o endpoint principal ("/categories") mas através do método POST 
	public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto){ 
		dto = service.insert(dto); //na inserção o repositorio retorna um obj com o ID
		
		//quando se trata de inserir, convém mostrar o caminho da localização do obj
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		
		//retorna a requisição com o cód 201(obj criado) e o obj no corpo da resposta
		return ResponseEntity.created(uri).body(dto); 
	}

	@PutMapping(value = "/{id}") //(ATUALIZAR)sequencia do endpoint categories com método/verbo http PUT de requisição (ex: host/categories/1) 
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto){ //@path identifica o elemento na url e o @req devolve no corpo da requisição
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto); 
	}
	
	@DeleteMapping(value = "/{id}") //(DELETAR)sequencia do endpoint categories com método/verbo http DELETE de requisição (ex: host/categories/1) 
	public ResponseEntity<Void> delete(@PathVariable Long id){ //@path identifica o elemento na url
		service.delete(id);
		return ResponseEntity.noContent().build(); //retorna cód 204 = não tem corpo na resposta, mas requisição está ok! 
	}
	
	
}
