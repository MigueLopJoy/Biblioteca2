package com.miguel.library.Validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueRegistrationNumberValidator.class)
public @interface UniqueRegistrationNumber {
    String message() default "Registration Number Already Taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}