package com.miguel.library.Validations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = UniqueEmailOrPhoneNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmailOrPhoneNumber {
    String message() default "Field value is duplicated";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}