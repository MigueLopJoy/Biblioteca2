package com.miguel.library.Validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearNotGreaterThanCurrentValidator.class)
@Documented
public @interface YearNotGreaterThanCurrent {

    String message() default "{Invalid year}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
