package com.miguel.library.Validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueLibraryNameValidator.class)
public @interface UniqueLibraryName {
    String message() default "Library Name Already Taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
