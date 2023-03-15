package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration // informa que a classe é de configuração
@EnableAuthorizationServer // informa que a classe representa o AuthorizationServer do JWT
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	//configura o Token para o AuthorizationServer
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	//define autenticação e os dados do cliente(aplicação/app) - credenciais da aplicação
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory() //define processo em memória
		.withClient("dscatalog") //define o id/nome do app que será usado pela aplicação web(LOGIN da APLICAÇÃO no Servidor) para acessar o backend | OBS.: NÃO LOGIN DO USUÁRIO
		.secret(passwordEncoder.encode("dscatalog123")) //senha de acesso a APLICAÇÃO já CRIPTOGRAFADA | OBS.: NÃO SENHA DO USUÁRIO
		.scopes("read", "write") //acesso de leitura e escrita
		.authorizedGrantTypes("password") //tipo de acesso/login
		.accessTokenValiditySeconds(86400); //tempo de validação do token em segundos (nesse caso 24h)
	}

	//define quem vai autorizar e qual vai ser o formato do token (JWT)
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager) //define o AuthenticationManager que processará a autenticação
		.tokenStore(tokenStore) //processa o token
		.accessTokenConverter(accessTokenConverter);
	}

}
