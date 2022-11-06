package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

//informa ao Spring para gerenciar as dependências dessa classe
@Service
public class CategoryService {

	//o Spring fica responsável por instanciar uma injeção de dependência válida no obj	
	@Autowired
	private CategoryRepository repository;
	
	//garante a transação com o banco e informa que é somente leitura para não travar o banco(lock)
	@Transactional(readOnly = true) //obs.: import do Spring e não javax
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll(); //busca informações no banco
		
		/*forma 1: 
		  List<CategoryDTO> listDto = new ArrayList<>(); //converte a lista para tipo
		  DTO for(Category cat : list) { listDto.add(new CategoryDTO(cat)); }
		 
		  return listDto; //retorna a lista DTO
		 */	

		//forma 2: expressão lambda!
		return list.stream().map( x -> new CategoryDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		Optional<Category> obj = repository.findById(id); //classe Optional<E> retorna objetos diferente de nulos
		//Category entity = obj.get(); //captura objeto do Optional
		Category entity = obj.orElseThrow( () -> new EntityNotFoundException("Essa Categoria não existe!") );//tenta capturar o obj, caso não exista executa a exceção personalizada criada através da função lambda
		return new CategoryDTO(entity);
	}
	
}
