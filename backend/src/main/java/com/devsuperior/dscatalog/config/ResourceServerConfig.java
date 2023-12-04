package com.devsuperior.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration  //classe de configuração
@EnableResourceServer //implementa a funcionalidade do servidor de recursos do OAuth2 (tipo JWT)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Autowired
	private JwtTokenStore tokenStore;
	
	//ambiente de desenvolvimento da aplicação
	@Autowired
	private Environment env;
	
	//enpoints perfil públicos
	//1-tela de login,2-Console H2 Test
	private static final String[] PUBLIC = {"/oauth/token","/h2-console/**"};
	
	//endpoints para perfil OPERADOR ou ADMIN
	//todas as rotas a partir de products e categories
	private static final String[] OPERATOR_OR_ADMIN = {"/products/**","/categories/**"};

	//enpoint específico perfil ADMIN
	//todas as rotas a partir de users
	private static final String[] ADMIN = {"/users/**"};
	
	//configura a decriptação do token vindo do client, analisando o secret, claims e a validade
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	//QUEM pode acessar O QUE?!
	//configura as rotas dos recursos para liberação por usuário específico
	//o catálogo será liberado para todos, independente de realizar login
	//CRUD dos Produtos e Categoria dependem de login + roles de OPERADOR ou ADMIN atribuídos ao usuário 
	//CRUD de Usuário depende de login + roles de ADMIN atribuídos ao usuário
	@Override
	public void configure(HttpSecurity http) throws Exception {
		//define autorizações das requisições por rota
		http.authorizeRequests()
				.antMatchers(PUBLIC).permitAll() //não exige login, público, rota principal, liberado pra todos os perfis
					.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll() //liberado somente o verbo GET do HTTP para as rotas a partir de products e categories para todos 
						.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR","ADMIN") //rotas liberadas para quem tiver perfil de OPERATOR ou ADMIN
		//.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR","ADMIN") OBS.: PARA CADASTRAR O PAPEL NO BD O SPRING EXIGE O PREFIXO 'ROLE_' (ROLE_ADMIN), PORÉM PARA DEFINIR NAS AUTORIZAÇÕES NÃO É NECESSÁRIO O PREFIXO
							.antMatchers(ADMIN).hasRole("ADMIN")//rotas liberadas para quem tiver perfil de ADMIN (restringe a rota somente a esse tipo de perfil)
		//para acesso a qualquer outra rota, o usuário deve estar logado não importa o perfil atribuído a ele
								.anyRequest().authenticated();
		
		//BANCO-H2 CONSOLE
		//Quais os spring.profiles.active em execução?
		//se detre os ativos existe o perfil teste
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			//desabilite a proteção dos frames dele
			http.headers().frameOptions().disable();
		}
		
		http.cors().configurationSource(corsConfigurationSource()); //chamada do CORS
	}

	//COMPONENTE DE CONFIGURAÇÃO DE CORS(CROSS ORIGIN RESOURSE SHARING)
	//COMPARTILHA RECURSOS ENTRE APLICAÇÕES DE DIFERENTES DOMÍNIOS
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration corsConfig = new CorsConfiguration();
	    corsConfig.setAllowedOriginPatterns(Arrays.asList("*")); //define as origens/domínio permitido para acesso (ex: somente https://meudominio.com)
	    corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
	    corsConfig.setAllowCredentials(true);
	    corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
	 
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", corsConfig);
	    return source;
	}
	 
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
	    FilterRegistrationBean<CorsFilter> bean
	            = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
	    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
	    return bean;
	}

}
