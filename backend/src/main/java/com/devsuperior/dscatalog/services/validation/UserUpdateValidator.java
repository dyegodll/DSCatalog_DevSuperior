package com.devsuperior.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

//boilerplate
//implementa a lógica da validação da anotation (UserUpdateValid)
//parâmetrização genérics ConstraintValidator< (tipo da anotation customizada) , (tipo da classe que vai receber a anotation) > {
//faz validação de inserção de novo usuário
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
	
	/* guarda as informações da requisição
	para podermos acessar o id no método update */
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserUpdateValid ann) {
		//lógica ao inicializar o obj
	}

	//faz validação através do boleano
	//true = validado
	//false = não validado
	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
		
		//obs.: var == Object (tipo genérico de variável)
		//acessa as variáveis da url
		//cast de Map<chave,valor>, onde no HTTP tudo é String
		@SuppressWarnings("unchecked") //suprime aviso typesafety
		var uriVars = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);	//mapeia os atribuitos da url
		Long userId = Long.parseLong(uriVars.get("id"));
		
		//lista dos possíveis erros da validação
		List<FieldMessage> list = new ArrayList<>();
		
		// Coloque aqui os testes de validação, acrescentando objetos FieldMessage à lista
		/* quantos necessários, adicionando a lista
		 if(){
		 	list.add(e);
		 } 
		 */
		
		//testa se email já existe no banco 
		//instancia user atrvés do email vindo do dto e busca no banco pelo findByEmail, logo se usuário existe é pq foi encontrado pelo email
		User user = repository.findByEmail(dto.getEmail());
		if(user != null && userId != user.getId()) {
			//então usuário com email já existe
			// só pode atualizar email do mesmo usuário
			list.add(new FieldMessage("email", "Ops! Email já cadastrado!"));
		}
		
		//insere os erros (FieldMessage) na lista do Bean Validation(ex.: MethodArgumentNotValidException) do contexto
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		
		//a lista vai continuar vazia dependendo dos erros adicionados
		//lista vazia = true = método retorna true = validado
		//lista com algum item = false = método retorna false = não validado
		return list.isEmpty();
	}
}
