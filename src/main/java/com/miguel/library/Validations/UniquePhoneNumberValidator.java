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

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, Object> {

    @Autowired
    private IUserService userService;

    @Autowired
    private ILibraryService libraryService;

    @Override
    public void initialize(UniquePhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Boolean isValidPhoneNumber = true;
        String phoneNumber = null;
        UserDTOSaveUser saveUserDTO = null;
        UserDTOEditUser editUserDTO = null;
        LibraryDTOSaveLibrary saveLibraryDTO = null;
        LibraryDTOEditLibrary editLibraryDTO = null;
        User userWithPhoneNumber = null;
        Library libraryWithPhoneNumber = null;

        if (object instanceof UserDTOSaveUser ||
                object instanceof UserDTOSaveLibrarian
        ) {
            saveUserDTO = (UserDTOSaveUser) object;
            phoneNumber = saveUserDTO.getPhoneNumber();
        } else if (object instanceof UserDTOEditUser ||
                object instanceof UserDTOEditReader
        ) {
            editUserDTO = (UserDTOEditUser) object;
            phoneNumber = editUserDTO.getPhoneNumber();
        } else if (object instanceof LibraryDTOSaveLibrary) {
            saveLibraryDTO = (LibraryDTOSaveLibrary) object;
            phoneNumber = saveLibraryDTO.getLibraryPhoneNumber();
        } else if (object instanceof LibraryDTOEditLibrary) {
            editLibraryDTO = (LibraryDTOEditLibrary) object;
            phoneNumber = editLibraryDTO.getLibraryPhoneNumber();
        }

        if (Objects.nonNull(phoneNumber)) {
            userWithPhoneNumber = userService.searchByPhoneNumber(phoneNumber);
            libraryWithPhoneNumber = libraryService.searchByLibraryPhoneNumber(phoneNumber);
        }

        if (Objects.nonNull(userWithPhoneNumber) ||
            Objects.nonNull(libraryWithPhoneNumber)
        ) {
            if (Objects.nonNull(saveUserDTO) ||
                    Objects.nonNull(saveLibraryDTO)
            ) {
                isValidPhoneNumber = false;

            } else if (Objects.nonNull(editUserDTO)) {
                if (!userWithPhoneNumber.getIdUser().equals(editUserDTO.getOriginalUserId())) {
                    isValidPhoneNumber = false;
                }
            } else if (Objects.nonNull(editLibraryDTO)) {
                if (!libraryWithPhoneNumber.getIdLibrary().equals(editLibraryDTO.getOriginalLibraryId())) {
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
