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
			+ "(:categories IS NULL OR :categories IN cats) " //Perfil TEST - H2
			+ "AND "
			+ "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%')) )")
	Page<Product> findAllPagedCategoryName(List<Category> categories, String name, Pageable pageable);

	// COALESCE(:categories) IS NULL OR cats IN :categories // Perfil DEV - POSTGRES

}
