package com.miguel.library.Validations;

import com.miguel.library.DTO.BooksEditDTOBookCopy;
import com.miguel.library.DTO.BooksEditDTOBookEdition;
import com.miguel.library.DTO.BooksSaveDTOBookCopy;
import com.miguel.library.DTO.BooksSaveDTOBookEdition;
import com.miguel.library.model.BookCopy;
import com.miguel.library.services.IBookCopyService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UniqueRegistrationNumberValidator implements ConstraintValidator<UniqueRegistrationNumber, Object> {

    @Autowired
    private IBookCopyService bookCopyService;

    @Override
    public boolean isValid(Object bookCopy, ConstraintValidatorContext context) {
        Boolean isValidRegistrationNumber = true;
        Long registrationNumber = null;
        BooksSaveDTOBookCopy saveDTO = null;
        BooksEditDTOBookCopy editDTO = null;
        BookCopy bookCopyWithRegistrationNumber = null;

        if (bookCopy instanceof BooksSaveDTOBookCopy) {
            saveDTO = (BooksSaveDTOBookCopy) bookCopy;
            registrationNumber = saveDTO.getRegistrationNumber();
        } else if (bookCopy instanceof BooksEditDTOBookCopy) {
            editDTO = (BooksEditDTOBookCopy) bookCopy;
            registrationNumber = editDTO.getRegistrationNumber();
        }

        if (Objects.nonNull(registrationNumber)) {
            bookCopyWithRegistrationNumber = bookCopyService.searchByRegistrationNumber(registrationNumber);
        }

        if (Objects.nonNull(bookCopyWithRegistrationNumber)) {
            if (Objects.nonNull(saveDTO)) {
                isValidRegistrationNumber = false;
            } else if (Objects.nonNull(editDTO)) {
                if (!bookCopyWithRegistrationNumber.getIdBookCopy().equals(editDTO.getOriginalBookCopyId())) {
                    isValidRegistrationNumber = false;
                }
            }
        }

        if (!isValidRegistrationNumber) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Registration Number Already Taken")
                    .addPropertyNode("registrationNumber")
                    .addConstraintViolation();
        }

        return isValidRegistrationNumber;
    }
}
