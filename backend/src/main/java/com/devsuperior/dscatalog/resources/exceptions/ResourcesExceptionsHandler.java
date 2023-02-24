package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice //identifica classe responsável por tratar exceptions dos controladores
public class ResourcesExceptionsHandler {

	@ExceptionHandler(ResourceNotFoundException.class) //identifica método que intercepta a exception desse tipo com a exceção correspondente
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.NOT_FOUND; //tipo ENUN de HTTP (NOT_FOUND = 404)
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value()); 
		err.setError("Resource Not Found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		//status define o status da requisição
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DataBaseException.class) //identifica método que intercepta a exception desse tipo com a exceção correspondente
	public ResponseEntity<StandardError> databaseError(DataBaseException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST; //tipo ENUN de HTTP (erro sem especificação = 400)
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value()); 
		err.setError("DataBase Exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		//status define o status da requisição
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class) //identifica método que intercepta a exception desse tipo com a exceção correspondente
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; //tipo ENUN de HTTP (erro de processamento de validação de entidade = 422)
		ValidationError err = new ValidationError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value()); 
		err.setError("Validation Exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		//erros de validação dentro da MethodArgumentNotValidException
		// e.getBindingResult() //captura os erros da validação
			// .getFieldErrors(); //converte os erros em lista, para poder capturar o campo e a msg
		
		//para cada FieldError dentro da exceção, capture e adicione a lista
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}
		
		//status define o status da requisição
		return ResponseEntity.status(status).body(err);
	}
}
