package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product p = new Product(1L, "PC Gamer Nitro", "Lorem ipsum dolor sit amet", 5799.90, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/11-big.jpg", Instant.parse("2020-07-14T10:00:00Z"));
		p.getCategories().add(createCategory());
		return p;
	}
	
	public static ProductDTO createProductDTO() {
		Product prod = createProduct();
		return new ProductDTO(prod, prod.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(3L,"Computers");
	}
}
