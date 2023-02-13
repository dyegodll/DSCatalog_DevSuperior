package com.devsuperior.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Role;

//informa ao Spring para gerenciar as dependências dessa classe
//@Repository não é mais necessário
public interface RoleRepository extends JpaRepository<Role, Long> {
	//camada de acesso ao BD
	//Já funciona, pois está herdando da Classe já Implementada JpaRepository<T, ID>
	//T = tipo/nome da classe JPA (@Entity)
	//ID = tipo do id definido na classe (nesse caso Long)
}
