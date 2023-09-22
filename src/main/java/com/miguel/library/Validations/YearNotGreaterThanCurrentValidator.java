package com.miguel.library.Validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class YearNotGreaterThanCurrentValidator implements ConstraintValidator<YearNotGreaterThanCurrent, Integer> {

    @Override
    public void initialize(YearNotGreaterThanCurrent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValidYear = false;

        if (year != null) {
            if (year <= Year.now().getValue()) {
                isValidYear = true;
            }
        }
        return isValidYear;
    }
}
