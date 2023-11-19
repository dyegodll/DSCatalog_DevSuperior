package com.devsuperior.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

//informa ao Spring para gerenciar as dependências dessa classe
//@Repository não é mais necessário
public interface ProductRepository extends JpaRepository<Product, Long> {

	//JPQL
	@Query("SELECT DISTINCT obj FROM Product obj "
			+ "INNER JOIN obj.categories cats "
			+ "WHERE "
			+ "(:categories IS NULL OR :categories IN cats) "
			+ "AND "
			+ "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%')) )")
	Page<Product> findAllPagedCategory(List<Category> categories, String name, Pageable pageable);

	//camada de acesso ao BD
	//Já funciona, pois está herdando da Classe já Implementada JpaRepository<T, ID>
	//T = tipo/nome da classe JPA (@Entity)
	//ID = tipo do id definido na classe (nesse caso Long)
}
