package com.devsuperior.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;

//informa ao Spring para gerenciar as dependências dessa classe
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	//camada de acesso ao BD
	//Já funciona, pois está herdando da Classe já Implementada JpaRepository<T, ID>
	//T = tipo/nome da classe JPA (@Entity)
	//ID = tipo do id definido na classe (nesse caso Long)
}
