package com.bindord.eureka.keycloak.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Email(message="Por favor ingrese un correo electrónico válido")
@Pattern(regexp=".+@.+\\..+", message="Por favor ingrese un correo electrónico válido")
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ExtendedEmailValidator {
    String message() default "Por favor ingrese un correo electrónico válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
