package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserUpdateValid;

//DTO específico para atualização de email, usa anotation customizada para validação
/* 
	anotation @UserUpdateValid não pode ser inserida no UserDTO porque a classe UserInsertDTO extende dela e já possui outra anotation
	logo daria conflito de anotations na UserInsertDTO.java, pois ela ficaria com 2
*/

@UserUpdateValid   //processa a validação com acesso ao BD
public class UserUpdateDTO extends UserDTO{
	private static final long serialVersionUID = 1L;

	

}
