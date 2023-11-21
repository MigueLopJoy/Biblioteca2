package com.miguel.library.services;

import com.miguel.library.DTO.*;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.Library;
import com.miguel.library.model.Role;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.repository.IULibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class ImpULibrarianService implements  IULibrarianService {

    @Autowired
    private ILibraryService libraryService;

    @Autowired
    private IULibrarianRepository librarianRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTOLibrarianResponse saveNewLibrarian(ULibrarian librarian) {
        librarian.setRole(Role.MANAGER);
        return saveLibrarian(librarian);
    }

    @Override
    public UserDTOLibrarianResponse saveNewCataloger(ULibrarian librarian) {
        librarian.setRole(Role.CATALOGER);
        return saveLibrarian(librarian);
    }

    @Override
    public UserDTOLibrarianResponse saveLibraryManager(ULibrarian librarian) {
        librarian.setRole(Role.MANAGER);
        return saveLibrarian(librarian);
    }

    private UserDTOLibrarianResponse saveLibrarian(ULibrarian librarian) {
        if (Objects.isNull(librarian)) {
            throw new ExceptionNullObject("Librarian should not be null");
        }
        return new UserDTOLibrarianResponse(
                "New Librarian Created Successfully",
                librarianRepository.save(librarian)
        );
    }

    public ULibrarian searchById(Integer librarianId) {
        return librarianRepository.findById(librarianId).orElse(null);
    }
    @Override
    public List<ULibrarian> findAll() {
        List<ULibrarian> allUsers = librarianRepository.findAll();
        if (allUsers.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No Librarians Were Found");
        }
        return allUsers;
    }

    @Override
    public UserDTOLibrarianResponse editLibrarian(Integer librarianId, UserDTOEditLibrarian librarianEdit) {
        String firstName = librarianEdit.getFirstName();
        String lastName = librarianEdit.getLastName();
        String email = librarianEdit.getEmail();
        String phoneNumber = librarianEdit.getPhoneNumber();
        String password = librarianEdit.getPassword();
        Role newRole = librarianEdit.getNewRole();

        ULibrarian fetchedLibrarian = this.searchById(librarianId);

        if (Objects.isNull(fetchedLibrarian)) {
            throw new ExceptionObjectNotFound("Librarian not found");
        }

        if (Objects.nonNull(firstName)) {
            fetchedLibrarian.setFirstName(firstName);
        }

        if (Objects.nonNull(lastName)) {
            fetchedLibrarian.setLastName(lastName);
        }

        if (Objects.nonNull(email)) {
            fetchedLibrarian.setEmail(email);
        }

        if (Objects.nonNull(phoneNumber)) {
            fetchedLibrarian.setPhoneNumber(phoneNumber);
        }

        if (Objects.nonNull(password)) {
            fetchedLibrarian.setPassword(password);
        }

        if (Objects.nonNull(newRole)) {
            fetchedLibrarian.setRole(newRole);
        }

        return new UserDTOLibrarianResponse(
                "Librarian Edited Successfully",
                librarianRepository.save(fetchedLibrarian)
        );    }

    @Override
    public SuccessfulObjectDeletionDTO deleteLibrarian(Integer librarianId) {
        ULibrarian fetchedLibrarian = this.searchById(librarianId);

        if (Objects.isNull(fetchedLibrarian)) {
            throw new ExceptionObjectNotFound("Librarian Not Found");
        }
        librarianRepository.deleteById(librarianId);
        return new SuccessfulObjectDeletionDTO("Librarian Deleted Successfully");    }

    @Override
    public ULibrarian createLibrarianFromDTO(UserDTOSaveUser librarianDTO, Library library) {
        return new ULibrarian(
                librarianDTO.getFirstName(),
                librarianDTO.getLastName(),
                librarianDTO.getGender(),
                librarianDTO.getBirthYear(),
                librarianDTO.getPhoneNumber(),
                librarianDTO.getEmail(),
                passwordEncoder.encode(librarianDTO.getPassword()),
                library
        );
    }

    @Override
    public ULibrarian createLibrarianFromDTO(UserDTOSaveLibrarian librarianDTO) {
        return new ULibrarian(
                librarianDTO.getFirstName(),
                librarianDTO.getLastName(),
                librarianDTO.getGender(),
                librarianDTO.getBirthYear(),
                librarianDTO.getPhoneNumber(),
                librarianDTO.getEmail(),
                passwordEncoder.encode(librarianDTO.getPassword()),
                libraryService.searchByLibraryId(librarianDTO.getIdLibrary())
        );
    }
}
