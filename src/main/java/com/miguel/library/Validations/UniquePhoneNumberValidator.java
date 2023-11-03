package com.miguel.library.Validations;

import com.miguel.library.DTO.BooksSaveDTOBookCopy;
import com.miguel.library.DTO.UEditReaderDTO;
import com.miguel.library.DTO.USaveReaderDTO;
import com.miguel.library.model.UReader;
import com.miguel.library.services.IUReaderService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, Object> {

    @Autowired
    private IUReaderService readerService;

    @Override
    public void initialize(UniquePhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object reader, ConstraintValidatorContext context) {
        Boolean isValidPhoneNumber = true;
        String phoneNumber = null;
        USaveReaderDTO saveDTO = null;
        UEditReaderDTO editDTO = null;
        UReader readerWithPhoneNumber = null;

        if (Objects.nonNull(reader)) {
            if (reader instanceof USaveReaderDTO) {
                saveDTO = (USaveReaderDTO) reader;
                phoneNumber = saveDTO.getPhoneNumber();
            } else if (reader instanceof UEditReaderDTO) {
                editDTO = (UEditReaderDTO) reader;
                phoneNumber = editDTO.getPhoneNumber();
            }
        }

        if (Objects.nonNull(phoneNumber)) {
            readerWithPhoneNumber = readerService.searchByPhoneNumber(phoneNumber);
        }

        if (Objects.nonNull(readerWithPhoneNumber)) {
            if (Objects.nonNull(saveDTO)) {
                isValidPhoneNumber = false;
            } else if (Objects.nonNull(editDTO)) {
                if (!readerWithPhoneNumber.getIdUser().equals(editDTO.getOriginalReaderId())) {
                    isValidPhoneNumber = false;
                }
            }
        }

        if (!isValidPhoneNumber) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Phone Number Already Taken")
                    .addPropertyNode("phoneNumber")
                    .addConstraintViolation();
        }

        return isValidPhoneNumber;
    }
}
