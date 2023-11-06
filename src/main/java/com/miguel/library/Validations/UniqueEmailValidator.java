package com.miguel.library.Validations;

import com.miguel.library.DTO.*;
import com.miguel.library.model.Library;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.model.UReader;
import com.miguel.library.model.User;
import com.miguel.library.services.ILibraryService;
import com.miguel.library.services.IULibrarianService;
import com.miguel.library.services.IUReaderService;
import com.miguel.library.services.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Object> {
    @Autowired
    private IUserService userService;
    @Autowired
    private ILibraryService libraryService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Boolean isValidEmail = true;
        String email = null;
        UserDTOSaveUser saveUserDTO = null;
        UserDTOEditUser editUserDTO = null;
        LibraryDTOSaveLibrary saveLibraryDTO = null;
        LibraryDTOEditLibrary editLibraryDTO = null;
        User userWithEmail = null;
        Library libraryWithEmail = null;

        if (object instanceof UserDTOSaveUser ||
                object instanceof UserDTOSaveLibrarian
        ) {
            saveUserDTO = (UserDTOSaveUser) object;
            email = saveUserDTO.getEmail();
        } else if (object instanceof UserDTOEditUser ||
                object instanceof UserDTOEditReader
        ) {
            editUserDTO = (UserDTOEditUser) object;
            email = editUserDTO.getEmail();
        } else if (object instanceof LibraryDTOSaveLibrary) {
            saveLibraryDTO = (LibraryDTOSaveLibrary) object;
            email = saveLibraryDTO.getLibraryEmail();
        } else if (object instanceof LibraryDTOEditLibrary) {
            editLibraryDTO = (LibraryDTOEditLibrary) object;
            email = editLibraryDTO.getLibraryEmail();
        }

        if (Objects.nonNull(email)) {
            userWithEmail = userService.searchByEmail(email);
            libraryWithEmail = libraryService.searchByLibraryEmail(email);
        }

        if (Objects.nonNull(userWithEmail) ||
                Objects.nonNull(libraryWithEmail)
        ) {
            if (Objects.nonNull(saveUserDTO) ||
                    Objects.nonNull(saveLibraryDTO)
            ) {
                isValidEmail = false;

            } else if (Objects.nonNull(editUserDTO)) {
                if (!userWithEmail.getIdUser().equals(editUserDTO.getOriginalUserId())) {
                    isValidEmail = false;
                }
            } else if (Objects.nonNull(editLibraryDTO)) {
                if (!libraryWithEmail.getIdLibrary().equals(editLibraryDTO.getOriginalLibraryId())) {
                    isValidEmail = false;
                }
            }
        }

        if (!isValidEmail) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Email Already Taken")
                    .addPropertyNode("email")
                    .addConstraintViolation();
        }

        return isValidEmail;
    }
}
