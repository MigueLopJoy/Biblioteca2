package com.miguel.library.Validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EditionYearNotBeforePublicationYearValidator.class)
public @interface EditionYearNotBeforePublicationYear {

    String message() default "Edition year should not be earlier than publication year";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}