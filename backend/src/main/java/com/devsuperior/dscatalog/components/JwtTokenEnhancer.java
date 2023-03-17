package com.devsuperior.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;

//TokenEnhancer = aprimorador/melhorador de tokens
//usado para acrescentar informações ao token
@Component //classe componente do SpringBoot
public class JwtTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserRepository userRepository;
	
	//entra no ciclo de vida do token, acrescentando os objs informados no momento da criação
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		//acrescenta nome do usuário ao token
		//recupera o username do usuário do token
		//instancia user com username que é email através do método findByEmail do repositório
		User user = userRepository.findByEmail(authentication.getName());
		
		//definindo obj a serem acrescentados
		//objetos na forma de Map< tipo_chave, tipo_valor >
		//tipo valor deve ser Object porque aceita qualquer tipo de obj
		Map<String,Object> map = new HashMap<>();
		map.put("userFirstName", user.getFirstName());
		map.put("userId", user.getId());
		
		//inserindo obj no token
		//método na classe específica DefaultOAuth2AccessToken, necessário downcast
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		token.setAdditionalInformation(map);
		
		//return token; //super classe com as informações adicionadas
		return accessToken; //subclasse com as informações adicionais herdadas
	}

}
