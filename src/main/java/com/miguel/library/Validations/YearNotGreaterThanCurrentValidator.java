package com.miguel.library.Validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;
import java.util.Objects;

public class YearNotGreaterThanCurrentValidator implements ConstraintValidator<YearNotGreaterThanCurrent, Integer> {

    @Override
    public void initialize(YearNotGreaterThanCurrent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValidYear = false;

        if (Objects.isNull(year)) {
            year = 0;
        }

        if (year <= Year.now().getValue()) {
            isValidYear = true;
        }

        return isValidYear;
    }
}
