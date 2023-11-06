package com.miguel.library.Validations;

import com.miguel.library.DTO.UserDTOEditReader;
import com.miguel.library.model.UReader;
import com.miguel.library.services.IUReaderService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UniqueReaderNumberValidator implements ConstraintValidator<UniqueReaderNumber, UserDTOEditReader> {
    @Autowired
    private IUReaderService readerService;

    @Override
    public void initialize(UniqueReaderNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDTOEditReader reader, ConstraintValidatorContext context) {
        Boolean isValidReaderNumber = true;
        String readerNumber;
        UReader readerWithReaderNumber = null;

        readerNumber = reader.getReaderNumber();

        if (Objects.nonNull(readerNumber)) {
            readerWithReaderNumber = readerService.searchByReaderNumber(readerNumber);
        }

        if (Objects.nonNull(readerWithReaderNumber)) {
            if (!readerWithReaderNumber.getIdUser().equals(reader.getOriginalUserId())) {
                isValidReaderNumber = false;
            }
        }

        if (!isValidReaderNumber) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Reader Number Already Taken")
                    .addPropertyNode("readerNumber")
                    .addConstraintViolation();
        }

        return isValidReaderNumber;
    }
}
