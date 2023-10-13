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

public class UniqueReaderNumberValidator implements ConstraintValidator<UniqueReaderNumber, UEditReaderDTO> {


    @Autowired
    private IUReaderService readerService;

    @Override
    public void initialize(UniqueReaderNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UEditReaderDTO reader, ConstraintValidatorContext constraintValidatorContext) {
        Boolean isValidPhoneNumber = true;
        String readerNumber;
        UReader readerWithPhoneNumber = null;

        readerNumber = reader.getReaderNumber();

        if (Objects.nonNull(readerNumber)) {
            readerWithPhoneNumber = readerService.searchByReaderNumber(readerNumber);
        }

        if (Objects.nonNull(readerWithPhoneNumber)) {
            if (!readerWithPhoneNumber.getIdUser().equals(reader.getOriginalReaderId())) {
                isValidPhoneNumber = false;
            }
        }
        return isValidPhoneNumber;
    }
}
