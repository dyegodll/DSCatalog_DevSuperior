package com.devsuperior.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

//boilerplate
//implementa a lógica da validação da anotation (UserInsertValid)
//parâmetrização genérics ConstraintValidator< (tipo da anotation customizada) , (tipo da classe que vai receber a anotation) > {
//faz validação de inserção de novo usuário
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
		//lógica ao inicializar o obj
	}

	//faz validação através do boleano
	//true = validado
	//false = não validado
	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		//lista dos possíveis erros da validação
		List<FieldMessage> list = new ArrayList<>();
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		/* quantos necessários, adicionando a lista
		 if(){
		 	list.add(e);
		 } 
		 */
		
		//testa se email já existe no banco 
		//instancia user atrvés do email vindo do dto e busca no banco pelo findByEmail, logo se usuário existe é pq foi encontrado pelo email
		User user = repository.findByEmail(dto.getEmail());
		if(user != null) {
			//então usuário com email já existe
			list.add(new FieldMessage("email", "Ops! Email já cadastrado!"));
		}
		
		
		//captura os erros(FieldMessage) da lista e insere na lista do contexto do Bean Validation(MethodArgumentNotValidException) para ser manipulado pelo Handler
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
