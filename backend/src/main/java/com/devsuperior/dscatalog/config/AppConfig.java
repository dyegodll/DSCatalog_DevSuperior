package com.devsuperior.dscatalog.config;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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
	
	//Objetos capazes de acessar os Tokens JWT (ler, decodificar, criar um token decodificando ele)
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter(); //instancia obj
		tokenConverter.setSigningKey("MY-JWT-SECRET"); //registra a chave do token
		return tokenConverter; //retorna
	}

	//Objetos capazes de acessar os Tokens JWT (ler, decodificar, criar um token decodificando ele)
	@Bean
	public JwtTokenStore tokenStore() {
		//instancia obj JWT passando como argumento o Bean accessTokenConverter com a chave do token
		return new JwtTokenStore(accessTokenConverter()); 
	}
	
}
