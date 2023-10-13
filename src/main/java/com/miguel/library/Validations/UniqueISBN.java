package com.miguel.library.Validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueISBNValidator.class)
public @interface UniqueISBN {
    String message() default "ISBN Already Taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
