package com.miguel.library.Validations;

import com.miguel.library.DTO.BooksEditDTOBookEdition;
import com.miguel.library.DTO.BooksSaveDTOBookEdition;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class EditionYearNotBeforePublicationYearValidator implements ConstraintValidator<EditionYearNotBeforePublicationYear, Object> {

    private Class<?> dtoClass;

    @Override
    public void initialize(EditionYearNotBeforePublicationYear constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(
            Object bookEdition,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        boolean isValidYear = false;
        Integer editionYear = null;
        Integer publicationYear = null;
        BooksSaveDTOBookEdition saveDTO = null;
        BooksEditDTOBookEdition editDTO = null;

        if (Objects.isNull(bookEdition)) {
            isValidYear = true;
        } else {
            if (bookEdition instanceof BooksSaveDTOBookEdition) {
                saveDTO = (BooksSaveDTOBookEdition) bookEdition;
                editionYear = saveDTO.getEditionYear();
                publicationYear = (Objects.nonNull(saveDTO.getBookWork())) ? saveDTO.getBookWork().getPublicationYear() : null;
            } else if (bookEdition instanceof BooksEditDTOBookEdition) {
                editDTO = (BooksEditDTOBookEdition) bookEdition;
                editionYear = editDTO.getEditionYear();
                publicationYear = (Objects.nonNull(editDTO.getEditionYear()) ? editDTO.get().getPublicationYear() : null;
            }
        }

        if (Objects.isNull(bookEdition) || Objects.isNull(bookEdition.getBookWork())) {
            isValidYear = true;
        } else {
            Integer editionYear = bookEdition.getEditionYear();
            Integer publicationYear = bookEdition.getBookWork().getPublicationYear();

            if (Objects.isNull(editionYear) || Objects.isNull(publicationYear)) {
                isValidYear = true;
            } else {
                if (editionYear >= publicationYear) {
                    isValidYear = true;
                }
            }
        }
        return isValidYear;
    }
}
