package com.miguel.library.Validations;

import com.miguel.library.DTO.BooksEditDTOBookEdition;
import com.miguel.library.DTO.BooksSaveDTOBookEdition;
import com.miguel.library.model.BookEdition;
import com.miguel.library.services.IBookEditionService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UniqueISBNValidator implements ConstraintValidator<UniqueISBN , Object> {

    @Autowired
    private IBookEditionService bookEditionService;

    @Override
    public void initialize(UniqueISBN constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object bookEdition, ConstraintValidatorContext context) {
        Boolean isValidISBN = true;
        String ISBN = null;
        BooksSaveDTOBookEdition saveDTO = null;
        BooksEditDTOBookEdition editDTO = null;
        BookEdition bookEditionWithISBN = null;

        if (Objects.nonNull(bookEdition)) {
            if (bookEdition instanceof BooksSaveDTOBookEdition) {
                saveDTO = (BooksSaveDTOBookEdition) bookEdition;
                ISBN = saveDTO.getISBN();
            } else if (bookEdition instanceof BooksEditDTOBookEdition) {
                editDTO = (BooksEditDTOBookEdition) bookEdition;
                ISBN = editDTO.getISBN();
            }
        }

        if (Objects.nonNull(ISBN)) {
            bookEditionWithISBN = bookEditionService.searchByISBN(ISBN);
        }

        if (Objects.nonNull(bookEditionWithISBN)) {
            if (Objects.nonNull(saveDTO)) {
                isValidISBN = false;
            } else if (Objects.nonNull(editDTO)) {
                if (!bookEditionWithISBN.getIdBookEdition().equals(editDTO.getOriginalBookEditionId())){
                    isValidISBN = false;
                }
            }
        }

        if (!isValidISBN) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("ISBN Already Taken")
                    .addPropertyNode("ISBN")
                    .addConstraintViolation();
        }

        return isValidISBN;
    }
}
