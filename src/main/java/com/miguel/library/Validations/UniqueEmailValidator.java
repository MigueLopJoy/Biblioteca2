package com.miguel.library.Validations;

import com.miguel.library.DTO.*;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.UReader;
import com.miguel.library.services.IUReaderService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Object> {

    @Autowired
    private IUReaderService readerService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object reader, ConstraintValidatorContext constraintValidatorContext) {
        Boolean isValidEmail = true;
        String email = null;
        USaveReaderDTO saveDTO = null;
        UEditReaderDTO editDTO = null;
        UReader readerWithEmail = null;

        if (reader instanceof USaveReaderDTO) {
            saveDTO = (USaveReaderDTO) reader;
            email = saveDTO.getEmail();
        } else if (reader instanceof BooksSaveDTOBookCopy) {
            editDTO = (UEditReaderDTO) reader;
            email = editDTO.getEmail();
        }

        if (Objects.nonNull(email)) {
            readerWithEmail = readerService.searchByEmail(email);
        }

        if (Objects.nonNull(readerWithEmail)) {
            if (Objects.nonNull(saveDTO)) {
                isValidEmail = false;
            } else if (Objects.nonNull(editDTO)) {
                if (!readerWithEmail.getIdUser().equals(editDTO.getOriginalReaderId())) {
                    isValidEmail = false;
                }
            }
        }
        return isValidEmail;
    }
}
