package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/* Mesmo utilizando o OAuth ou o JWT, é necessario utilizar a infraestrutura básica do Spring Security
para ter acesso ao banco de dados e ao usuário para conferir as credenciais para geração do token */

@Entity
@Table(name = "tb_user")
public class User implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String firstName;
	private String lastName;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	//muitos usuários podem ter muitos perfis
	@ManyToMany(fetch = FetchType.EAGER) 	//forsa a criação da lista dos perfis com base nos dados do BD ao instaciar o User					
	@JoinTable( 
			name="tb_user_role",		//tabela da associação (nova tabela no banco)
			//chaves estrangeiras das 2 tabelas
			joinColumns = @JoinColumn(name = "user_id"), //referência a própria classe
			inverseJoinColumns = @JoinColumn(name = "role_id") //faz referência a classe associada (componente/dependente)
			)
	private Set<Role> roles = new HashSet<>();
	
	public User() {
	}

	public User(Long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id);
	}

	//lista de papéis/perfís atribuídos ao usuário
	//converte tipo Role em GrantedAuthority a partir da lista roles
	//para enviar os dados para o AuthorizationServer do JWT(Jason Web Token) através do Spring Security
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
		//SimpleGrantedAuthority classe concreta da interface GrantedAuthority
	}

	//nome de credecial do usuário = email
	@Override
	public String getUsername() {
		return email;
	}

	//a conta não está expirada?
	@Override
	public boolean isAccountNonExpired() {
		// sim. a conta não está expirada
		return true;
	}

	//a conta não está bloqueada?
	@Override
	public boolean isAccountNonLocked() {
		// sim. a conta não está bloqueada
		return true;
	}

	//as credenciais não estão expiradas/inválidas?
	@Override
	public boolean isCredentialsNonExpired() {
		// sim. a credencial não está expirada
		return true;
	}

	//está habilitado?
	@Override
	public boolean isEnabled() {
		// sim. está habilitado
		return true;
	}

}
