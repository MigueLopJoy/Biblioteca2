package com.miguel.library.Validations;

import com.miguel.library.services.IBookCopyService;
import com.miguel.library.services.IUReaderService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UniqueRegistrationNumberValidator implements ConstraintValidator<UniqueRegistrationNumber , Long> {

    @Autowired
    private IBookCopyService bookCopyService;

    @Override
    public boolean isValid(Long registrationNumber, ConstraintValidatorContext constraintValidatorContext) {
        Boolean isValidRegistrationNumber = false;

            if (Objects.isNull(bookCopyService.searchByRegistrationNumber(registrationNumber))) {
                isValidRegistrationNumber = true;
            }

        return isValidRegistrationNumber;
    }
}
