package com.devsuperior.dscatalog.resources.exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//classe que complementa o StandardError com uma lista dos erros de validação 
public class ValidationError extends StandardError implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> errors = new ArrayList<>();

	public List<FieldMessage> getErrors() {
		return errors;
	}
	
	//método para adicionar erros na lista
	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}
	
}
