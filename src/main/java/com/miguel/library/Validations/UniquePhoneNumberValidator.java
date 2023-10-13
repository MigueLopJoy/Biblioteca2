package com.miguel.library.Validations;

import com.miguel.library.DTO.BooksSaveDTOBookCopy;
import com.miguel.library.DTO.UEditReaderDTO;
import com.miguel.library.DTO.USaveReaderDTO;
import com.miguel.library.model.UReader;
import com.miguel.library.services.IUReaderService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, Object> {

    private IUReaderService readerService;

    @Override
    public void initialize(UniquePhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object reader, ConstraintValidatorContext constraintValidatorContext) {
        Boolean isValidPhoneNumber = true;
        String phoneNumber = null;
        USaveReaderDTO saveDTO = null;
        UEditReaderDTO editDTO = null;
        UReader readerWithPhoneNumber = null;

        if (reader instanceof USaveReaderDTO) {
            saveDTO = (USaveReaderDTO) reader;
            phoneNumber = saveDTO.getPhoneNumber();
        } else if (reader instanceof BooksSaveDTOBookCopy) {
            editDTO = (UEditReaderDTO) reader;
            phoneNumber = editDTO.getPhoneNumber();
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
        return isValidPhoneNumber;
    }
}
