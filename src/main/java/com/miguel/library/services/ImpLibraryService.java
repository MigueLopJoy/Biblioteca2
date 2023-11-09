package com.miguel.library.services;

import com.miguel.library.DTO.AuthorResponseDTO;
import com.miguel.library.DTO.LibraryDTOEditLibrary;
import com.miguel.library.DTO.LibraryDTOSaveLibrary;
import com.miguel.library.DTO.LibraryResponseDTO;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.Exceptions.ExceptionObjectAlreadyExists;
import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.Author;
import com.miguel.library.model.Library;
import com.miguel.library.repository.ILibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ImpLibraryService implements ILibraryService{

    @Autowired
    private ILibraryRepository libraryRepository;

    @Override
    public LibraryResponseDTO saveNewLibrary(Library library) {
        if (Objects.isNull(library)) {
            throw new ExceptionNullObject("Library should not be null");
        }

        return new LibraryResponseDTO(
                "New Library Created Successfully",
                libraryRepository.save(library)
        );
    }

    @Override
    public List<Library> findAll() {
        List<Library> allLibraries = libraryRepository.findAll();
        if (allLibraries.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No Libraries Were Found");
        }
        return allLibraries;
    }

    @Override
    public Library searchByLibraryId(Integer libraryId) {
        return libraryRepository.findById(libraryId).orElse(null);
    }

    @Override
    public Library searchByLibraryEmail(String email) {
        return libraryRepository.findByLibraryEmail(email).orElse(null);
    }

    @Override
    public Library searchByLibraryPhoneNumber(String phoneNumber) {
        return libraryRepository.findByLibraryPhoneNumber(phoneNumber).orElse(null);
    }

    @Override
    public Library searchByLibraryName(String libraryName) {
        return libraryRepository.findByLibraryName(libraryName).orElse(null);
    }

    @Override
    public Library createLibraryFromDTO(LibraryDTOSaveLibrary libraryDTO) {
        return Library.builder()
                .libraryName(libraryDTO.getLibraryName())
                .libraryEmail(libraryDTO.getLibraryEmail())
                .libraryPhoneNumber(libraryDTO.getLibraryPhoneNumber())
                .libraryAddress(libraryDTO.getLibraryAddress())
                .city(libraryDTO.getCity())
                .province(libraryDTO.getProvince())
                .postalCode(libraryDTO.getPostalCode())
                .build();
    }
}
