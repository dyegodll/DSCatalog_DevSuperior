package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

//informa ao Spring para gerenciar as dependências dessa classe
@Service
public class UserService {

	//o Spring fica responsável por instanciar uma injeção de dependência válida no obj	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//garante a transação com o banco e informa que é somente leitura para não travar o banco(lock)
	@Transactional(readOnly = true) //obs.: import do Spring e não javax
	public Page<UserDTO> findAllPaged(Pageable pageable){
		Page<User> list = repository.findAll(pageable); //busca informações no banco de acordo com o pageRequest

		//expressão lambda, onde page já é um tipo stream e não deve fazer a conversão
		return list.map( x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id); //classe Optional<E> retorna objetos diferente de nulos
		//User entity = obj.get(); //captura objeto do Optional
		User entity = obj.orElseThrow( () -> new ResourceNotFoundException("Ops! Usuário não cadastrado") );//tenta capturar o obj, caso não exista executa a exceção personalizada criada através da função lambda
		return new UserDTO(entity);
	}

	
	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		
		User entity = new User(); //instancia da entidade que será inserida no BD
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));// criptografa a senha
		entity = repository.save(entity); //na inserção o repositorio retorna um obj com o ID
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID "+ id + " not found!");
		}
	}

	//não deve usar a anotation transaction por causa das exceções que pode dar
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("ID "+id+" Not Found!"); 
		}
		catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation"); //tenta deletar obj que outros dependem
		}
	}
	
	private void copyDtoToEntity(UserDTO dto, User entity) {

		//copia dados DTO para Entidade User
		entity.setFirstName( dto.getFirstName() );
		entity.setLastName( dto.getLastName() );
		entity.setEmail( dto. getEmail() );
		
		//Obs: produto pode ter mais de um perfil
		//limpa os possíveis perfis existentes na lista
		entity.getRoles().clear();
		
		//percorre cada elemento Role da lista do DTO
		for(RoleDTO roleDto : dto.getRoles()) {
			//instancia entidade Role pelo JPA
			Role role = roleRepository.getOne(roleDto.getId());
			//adiciona o role existente na lista 
			entity.getRoles().add(role);
		}
	}
	
}
