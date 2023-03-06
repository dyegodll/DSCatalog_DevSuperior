package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//Spring Security para configurações de autenticação e autorização!

/* Mesmo utilizando o OAuth ou o JWT, é necessario utilizar a infraestrutura básica do Spring Security
   para ter acesso ao banco de dados e ao usuário para conferir as credenciais para geração do token */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//Bean de classe externa, no AppConfig
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//implementado no UserService
	@Autowired
	private UserDetailsService userDetailsService;
	
	//configura qual algoritmo que criptografa as senhas
	//configura quem é o UserDetailsService
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//informa ao Spring Security na hora de fazer autenticação
		//como buscar o usuário por email e como analisar a senha criptografada(BCrypt)
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	
	//configura acesso aos Endpoints a passar pela biblioteca do actuator do Spring Cloud OAuth 2 para autenticação
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**"); 
	}
	
	@Override
	@Bean //transforma o AuthenticationManager em componente do sistema
	protected AuthenticationManager authenticationManager() throws Exception {
		// necessário para o AuthorizationServer
		return super.authenticationManager();
	}
}
