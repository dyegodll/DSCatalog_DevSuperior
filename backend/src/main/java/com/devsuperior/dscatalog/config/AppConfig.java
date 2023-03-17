package com.devsuperior.dscatalog.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
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

	//acessa variável de ambiente spring em application.properties
	//JWT_SECRET = jwtSecret (padrão camelCase)
	//jwt.secret=${JWT_SECRET:MY-JWT-SECRET}, caso o valor não tenha sido defido na variável
	//use a Coalescência ( : ) para definir o valor padrão ("MY-JWT-SECRET")
	@Value("${jwt.secret}")//nome da chave
	private String jwtSecret;
	
    //componente do Spring
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
    	//classe externa que criptografa as senhas
    	return new BCryptPasswordEncoder();
    }

    //Objetos capazes de acessar os Tokens JWT (ler, decodificar, criar um token decodificando ele)
    @Bean
    JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter(); //instancia obj
        tokenConverter.setSigningKey(jwtSecret); //registra a chave do token, que só o sistema conhece
        return tokenConverter;
    }

    //Objetos capazes de acessar os Tokens JWT (ler, decodificar, criar um token decodificando ele)
    @Bean
    JwtTokenStore tokenStore() {
        //instancia obj JWT passando como argumento o Bean accessTokenConverter com a chave do token
        return new JwtTokenStore(accessTokenConverter());
    }
	
	//OBS-1.: Quanto menor for o tempo de expiração do token, mais seguro ele é!
	
	/*OBS-2.: Se o Authorization Server estiver numa aplicação diferente da aplicação do 
	 Resource Server então os dois devem conhecer o segredo(assinatura) do token(JWT).
	 Por que o Resource verifica se o token gerado pelo Authorization é valido*/ 
}
