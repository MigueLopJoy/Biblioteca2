package com.miguel.library.Validations;

import com.miguel.library.DTO.BooksEditDTOBookEdition;
import com.miguel.library.DTO.BooksSaveDTOBookEdition;
import com.miguel.library.model.BookEdition;
import com.miguel.library.repository.IBookEditionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class EditionYearNotBeforePublicationYearValidator implements ConstraintValidator<EditionYearNotBeforePublicationYear, Object> {

    @Autowired
    private IBookEditionRepository bookEditionRepository;

    @Override
    public void initialize(EditionYearNotBeforePublicationYear constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(
            Object bookEdition,
            ConstraintValidatorContext context
    ) {
        boolean isValidYear = true;
        Integer editionYear = null;
        Integer publicationYear = null;
        BooksSaveDTOBookEdition saveDTO;
        BooksEditDTOBookEdition editDTO;
        BookEdition originalBookEdition;

        if (Objects.nonNull(bookEdition)) {
            if (bookEdition instanceof BooksSaveDTOBookEdition) {
                saveDTO = (BooksSaveDTOBookEdition) bookEdition;
                editionYear = saveDTO.getEditionYear();
                publicationYear =
                        (Objects.nonNull(saveDTO.getBookWork())) ?
                                saveDTO.getBookWork().getPublicationYear() : null;

            } else if (bookEdition instanceof BooksEditDTOBookEdition) {
                editDTO = (BooksEditDTOBookEdition) bookEdition;
                originalBookEdition = bookEditionRepository.getById(editDTO.getOriginalBookEditionId());
                editionYear = editDTO.getEditionYear();
                publicationYear =
                        (Objects.nonNull(originalBookEdition.getBookWork())) ?
                                originalBookEdition.getBookWork().getPublicationYear() : null;
            }
        }

        if (Objects.nonNull(editionYear) && Objects.nonNull(publicationYear)) {
            if (editionYear < publicationYear) {
                isValidYear = false;
            }
        }

        if (!isValidYear) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Edition year should not be earlier than publication year")
                    .addPropertyNode("editionYear")
                    .addConstraintViolation();
        }

        return isValidYear;
    }
}
