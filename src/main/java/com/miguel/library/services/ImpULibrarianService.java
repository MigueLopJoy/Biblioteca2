package com.miguel.library.services;

import com.miguel.library.DTO.*;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.model.UReader;
import com.miguel.library.repository.IULibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class ImpULibrarianService implements  IULibrarianService {

    @Autowired
    private IULibrarianRepository librarianRepository;
    @Override
    public ULibrarianResponseDTO saveNewLibrarian(ULibrarian librarian) {
        if (Objects.isNull(librarian)) {
            throw new ExceptionNullObject("Librarian should not be null");
        }
        return new ULibrarianResponseDTO(
                "New Librarian Created Successfully",
                librarianRepository.save(librarian)
        );
    }

    @Override
    public ULibrarian searchByEmail(String email) {
        return librarianRepository.findByEmail(email).orElse(null);
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
    public ULibrarianResponseDTO editLibrarian(Integer librarianId, UEditLibrarianDTO librarianEdit) {
        String firstName = librarianEdit.getFirstName();
        String lastName = librarianEdit.getLastName();
        String email = librarianEdit.getEmail();
        String phoneNumber = librarianEdit.getPhoneNumber();
        String password = librarianEdit.getPassword();

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

        return new ULibrarianResponseDTO(
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
    public ULibrarian createLibrarianFromDTO(USaveLibrarianDTO librarianDTO) {
        return new ULibrarian(
                librarianDTO.getFirstName(),
                librarianDTO.getLastName(),
                librarianDTO.getEmail(),
                librarianDTO.getPassword(),
                librarianDTO.getPhoneNumber(),
                librarianDTO.getAuthorities()
        );
    }
}
