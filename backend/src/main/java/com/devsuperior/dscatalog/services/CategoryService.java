package com.devsuperior.dscatalog.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

//informa ao Spring para gerenciar as dependências dessa classe
@Service
public class CategoryService {

	//o Spring fica responsável por instanciar uma injeção de dependência válida no obj	
	@Autowired
	private CategoryRepository repository;
	
	public List<Category> findAll(){
		return repository.findAll();
	}
	
}
