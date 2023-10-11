package com.miguel.library.Validations;

import com.miguel.library.services.IUReaderService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UniqueEmailOrPhoneNumberValidator  implements ConstraintValidator<UniqueEmailOrPhoneNumber , String> {

    private IUReaderService readerService;

    @Override
    public void initialize(UniqueEmailOrPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String emailOrPhoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        Boolean isValidEmailOrPhoneNumber = false;

        if (isEmail(emailOrPhoneNumber)) {
            if (Objects.isNull(readerService.searchByEmail(emailOrPhoneNumber))) {
                isValidEmailOrPhoneNumber = true;
            }
        }

        if (isPhoneNumber((emailOrPhoneNumber))) {
            if (Objects.nonNull(readerService.searchByPhoneNumber(emailOrPhoneNumber))) {
                isValidEmailOrPhoneNumber = true;
            }
        }
        return isValidEmailOrPhoneNumber;
    }

    private boolean isEmail(String value) {
        return value != null && value.matches("^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$");
    }

    private boolean isPhoneNumber(String value) {
        return value != null && value.matches("^\\+?\\d{1,3}\\d{1,14}$");
    }
}
