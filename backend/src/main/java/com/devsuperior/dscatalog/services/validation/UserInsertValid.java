package com.devsuperior.dscatalog.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UserInsertValidator.class) //anotation criada(UserInsertValid) faz uso da classe informada no @Constraint (UserInsertValidator)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

//classe responsável por criar anotation de valização personalizada, como acesso ao BD
//usada para toda valização customizada (boilerplate = template de códigos padrão)
public @interface UserInsertValid {
	String message() default "Validation error";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
