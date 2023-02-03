package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "tb_category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	//para Auditoria, grava instante da criação dos dados
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") //Formato UTC GMT (não é GMT-3 por exemplo)
	private Instant createdAt;
	
	//para Auditoria, grava instante da atualização dos dados
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") //no TIME ZONE UNIVERSAL de Greenwich Mean Time (GMT)
	private Instant updatedAt;

	//complemento do mapeamento da relação entre as tabelas
	@ManyToMany(mappedBy = "categories") //permite que a JPA acesse os produtos que estão vinculados as categorias
	private Set<Product> products = new HashSet<>();
	
	public Category() {
	}

	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
	
	//método que atribui o instante atual da criação antes de salvar no banco de dados
	//executado quando o save create da JPA for executado pela *primeira vez
	@PrePersist
	public void prePersist() {
		createdAt = Instant.now(); //grava instante da criação dos dados na variável
	}
	
	//método que atribui o instante atual da atualização antes de salvar no banco de dados
	//executado sempre que o save update da JPA for executado 
	@PreUpdate
	public void updatedPersist() {
		updatedAt = Instant.now();//grava instante da atualização dos dados na variável
	}
	
	public Set<Product> getProducts() {
		return products;
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

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}

}
