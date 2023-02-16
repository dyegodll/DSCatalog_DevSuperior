 package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	private UserService service;
	
	//EndPoint de Paginação
	@GetMapping //anotation para recuperar os dados
	public ResponseEntity<Page<UserDTO>> findAllPaged(Pageable pageable){
		
		//Classe Pageable do Spring Data Domain configura os Parâmetros(page,size,sort) automaticamente
		Page<UserDTO> list = service.findAllPaged(pageable);

		// o ResponseEntity é uma classe que convert a resposta para HTTP
		return ResponseEntity.ok().body(list); //passa a lista como resposta
	}

	@GetMapping(value = "/{id}") //sequencia do endpoint categories (ex: host/categories/1) //@PathVariable vincula o endpoit ao parâmetro do método
	public ResponseEntity<UserDTO> findById(@PathVariable Long id){ 
		UserDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto); 
	}
	
	@PostMapping //anotation para inserir os dados, obs: usa o endpoint principal ("/categories") mas através do método POST 
	public ResponseEntity<UserDTO> insert(@RequestBody UserInsertDTO dto){ 
		UserDTO newDto = service.insert(dto); //na inserção o repositorio retorna um obj com o ID
		
		//quando se trata de inserir, convém mostrar o caminho da localização do obj
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newDto.getId()).toUri();
		
		//retorna a requisição com o cód 201(obj criado) e o obj no corpo da resposta
		return ResponseEntity.created(uri).body(newDto); 
	}

	@PutMapping(value = "/{id}") //(ATUALIZAR)sequencia do endpoint categories com método/verbo http PUT de requisição (ex: host/categories/1) 
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto){ //@path identifica o elemento na url e o @req devolve no corpo da requisição
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto); 
	}
	
	@DeleteMapping(value = "/{id}") //(DELETAR)sequencia do endpoint categories com método/verbo http DELETE de requisição (ex: host/categories/1) 
	public ResponseEntity<Void> delete(@PathVariable Long id){ //@path identifica o elemento na url
		service.delete(id);
		return ResponseEntity.noContent().build(); //retorna cód 204 = não tem corpo na resposta, mas requisição está ok! 
	}
	
	
}
