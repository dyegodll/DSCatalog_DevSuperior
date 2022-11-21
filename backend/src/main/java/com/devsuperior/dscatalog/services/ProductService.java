package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

//informa ao Spring para gerenciar as dependências dessa classe
@Service
public class ProductService {

	//o Spring fica responsável por instanciar uma injeção de dependência válida no obj	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	//garante a transação com o banco e informa que é somente leitura para não travar o banco(lock)
	@Transactional(readOnly = true) //obs.: import do Spring e não javax
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		Page<Product> list = repository.findAll(pageRequest); //busca informações no banco de acordo com o pageRequest

		//expressão lambda, onde page já é um tipo stream e não deve fazer a conversão
		return list.map( x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id); //classe Optional<E> retorna objetos diferente de nulos
		//Product entity = obj.get(); //captura objeto do Optional
		Product entity = obj.orElseThrow( () -> new ResourceNotFoundException("Essa Categoria não existe!") );//tenta capturar o obj, caso não exista executa a exceção personalizada criada através da função lambda
		return new ProductDTO(entity, entity.getCategories());
	}

	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		
		Product entity = new Product(); //instancia da entidade que será inserida no BD
		//o ID será criado automaticamente pelo BD
		copyDtoToEntity(dto, entity);
		//insere no BD através do obj repository
		entity = repository.save(entity); //na inserção o repositorio retorna um obj com o ID
		
		//retorna somente o obj dto instanciado com os dados da entidade
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID "+ id + " not found!");
		}
	}

	//não deve usar a anotation transaction por causa das exceções que pode dar
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(e.getMessage()); 
		}
		catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation"); //tenta deletar obj que outros dependem
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {

		//copia dados DTO para Entidade Product
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());
		
		//Obs: produto também tem categorias
		
		//limpa possíveis categorias existentes na lista
		entity.getCategories().clear();
		
		//percorre cada elemento Category da Lista no DTO
		for(CategoryDTO catDto : dto.getCategories()) {
			//instancia entidade categoria pelo JPA
			Category category = categoryRepository.getOne(catDto.getId()); //sem acessar o BD ainda
			//adiciona as categorias a entidade 
			entity.getCategories().add(category);
		}
	}
	
}
