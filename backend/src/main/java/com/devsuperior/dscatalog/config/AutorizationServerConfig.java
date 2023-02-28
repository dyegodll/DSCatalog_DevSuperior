package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration //informa que a classe é de configuração
@EnableAuthorizationServer //informa que a classe representa o AuthorizationServer do JWT
public class AutorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	
	
}
