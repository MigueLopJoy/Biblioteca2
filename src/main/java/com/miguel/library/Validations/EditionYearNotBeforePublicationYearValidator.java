package com.miguel.library.Validations;

import com.miguel.library.DTO.BooksSaveDTOBookEdition;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class EditionYearNotBeforePublicationYearValidator implements ConstraintValidator<EditionYearNotBeforePublicationYear, BooksSaveDTOBookEdition> {
    @Override
    public void initialize(EditionYearNotBeforePublicationYear constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(
            BooksSaveDTOBookEdition bookEdition,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        boolean isValidYear = false;

        if (Objects.isNull(bookEdition) || Objects.isNull(bookEdition.getBookWork())) {
            isValidYear = true;
        }

        Integer editionYear = bookEdition.getEditionYear();
        Integer publicationYear = bookEdition.getBookWork().getPublicationYear();

        if (Objects.isNull(editionYear) || Objects.isNull(publicationYear)) {
            isValidYear = true;
        } else {
            if (editionYear >= publicationYear) {
                isValidYear = true;
            }
        }
        return isValidYear;
    }

}
