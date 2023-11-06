package com.miguel.library.Validations;

import com.miguel.library.DTO.BooksEditDTOBookEdition;
import com.miguel.library.DTO.BooksSaveDTOBookEdition;
import com.miguel.library.DTO.LibraryDTOEditLibrary;
import com.miguel.library.DTO.LibraryDTOSaveLibrary;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.Library;
import com.miguel.library.services.ILibraryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UniqueLibraryNameValidator implements ConstraintValidator<UniqueLibraryName, Object> {

    @Autowired
    private ILibraryService libraryService;

    @Override
    public void initialize(UniqueLibraryName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object library, ConstraintValidatorContext context) {
        Boolean isValidLibraryName = true;
        String libraryName = null;
        LibraryDTOSaveLibrary saveDTO = null;
        LibraryDTOEditLibrary editDTO = null;
        Library libraryWithName = null;

        if (Objects.nonNull(library)) {
            if (library instanceof LibraryDTOSaveLibrary) {
                saveDTO = (LibraryDTOSaveLibrary) library;
                libraryName = saveDTO.getLibraryName();
            } else if (library instanceof LibraryDTOEditLibrary) {
                editDTO = (LibraryDTOEditLibrary) library;
                libraryName = editDTO.getLibraryName();
            }
        }

        if (Objects.nonNull(libraryName)) {
            libraryWithName = libraryService.searchByLibraryName(libraryName);
        }

        if (Objects.nonNull(libraryWithName)) {
            if (Objects.nonNull(saveDTO)) {
                isValidLibraryName = false;
            } else if (Objects.nonNull(editDTO)) {
                if (!libraryWithName.getIdLibrary().equals(editDTO.getOriginalLibraryId())){
                    isValidLibraryName = false;
                }
            }
        }

        if (!isValidLibraryName) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Library Name Already Taken")
                    .addPropertyNode("ISBN")
                    .addConstraintViolation();
        }

        return isValidLibraryName;
    }
}
