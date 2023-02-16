package com.devsuperior.dscatalog.config;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//guarda as configurações do aplicativo
//cria algum componente específico

@Configuration
public class AppConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	//componente do Spring
		// criptografa as senhas
	@Bean 	//anotation de método
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
}
