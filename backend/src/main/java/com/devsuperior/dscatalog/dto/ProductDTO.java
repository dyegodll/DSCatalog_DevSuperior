package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

/* 
 lista das anotations de validação
import javax.validation.constraints. 
*/

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@Size(min=5, max=60, message="Campo deve ter entre 5 e 60 caracteres")
	@NotBlank(message = "Campo Obrigatório!")
	private String name;
	
	private String description;
	
	@Positive(message = "O preço deve ser um valor Positivo")
	private Double price;
	private String imgUrl;
	
	@PastOrPresent(message = "A data não deve ser maior que a data atual")
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>();
	
	public ProductDTO() {
	}

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}

	public ProductDTO(Product entity) {
		id = entity.getId();
		name = entity.getName();
		description = entity.getDescription();
		price = entity.getPrice();
		imgUrl = entity.getImgUrl();
		date = entity.getDate();
		categories = entity.getCategories().stream().map( x -> new CategoryDTO(x)).collect(Collectors.toList());
	}
	
	//construtor que popula a lista de categorias, que pode ter um ou mais
	public ProductDTO(Product entity, Set<Category> categories) {
		this(entity); //chama o construtor que tem só a entitade como argumento
		//acessa a lista categories que veio no argumento
		//para cada elemento da lista (cat)
		//acessa o atributo(classe) lista categories e adiciona o elemento 
		//instanciando o CategoryDTO(Category entity)
		categories.forEach(cat -> this.categories.add( new CategoryDTO(cat) ) );
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}
	
}
