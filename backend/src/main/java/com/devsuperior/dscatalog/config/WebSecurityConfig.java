package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//Configuração provisória para liberar todos endpoints

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	//configura qual algoritmo que criptografa as senhas
	//configura quem é o UserDetailsService
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//informa ao Spring Security na hora de fazer autenticação
		//como buscar o usuário(por email) e como analisar a senha criptografada(BCrypt)
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	
	//configura acesso aos Endpoints
	//ignora a segurança de todos os Endpoints mas passa pela biblioteca do OAuth para autenticação
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**"); 
	}
}
