package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice //identifica classe responsável por tratar exceptions dos controladores
public class ResourcesExceptionsHandler {

	@ExceptionHandler(ResourceNotFoundException.class) //identifica método que intercepta a exception desse tipo com a exceção correspondente
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value()); //tipo ENUN de HTTP (NOT_FOUND = 404)
		err.setError("Resource Not Found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		//status define o status da requisição
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
}
