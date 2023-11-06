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
    public List<Library> searchByCity(String city) {
        List<Library> allLibraries = libraryRepository.findByCity(city);
        if (allLibraries.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No Libraries Were Found");
        }
        return allLibraries;
    }

    @Override
    public List<Library> searchByProvince(String province) {
        List<Library> allLibraries = libraryRepository.findByProvince(province);
        if (allLibraries.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No Libraries Were Found");
        }
        return allLibraries;
    }

    @Override
    public LibraryResponseDTO editLibrary(Integer libraryId, LibraryDTOEditLibrary libraryEdit) {
        String libraryName = libraryEdit.getLibraryName();
        String libraryEmail = libraryEdit.getLibraryEmail();
        String libraryPhoneNumber = libraryEdit.getLibraryPhoneNumber();
        String libraryAddress = libraryEdit.getLibraryAddress();
        String city = libraryEdit.getCity();
        String province = libraryEdit.getProvince();
        String postalCode = libraryEdit.getPostalCode();

        Library fetchedLibrary = this.searchByLibraryId(libraryId);

        if (Objects.isNull(fetchedLibrary)) {
            throw new ExceptionObjectNotFound("Library Not Found");
        }

        if (Objects.nonNull(libraryName)) {
            fetchedLibrary.setLibraryName(libraryName);
        }

        if (Objects.nonNull(libraryEmail)) {
            fetchedLibrary.setLibraryEmail(libraryEmail);
        }

        if (Objects.nonNull(libraryPhoneNumber)) {
            fetchedLibrary.setLibraryPhoneNumber(libraryPhoneNumber);
        }

        if (Objects.nonNull(libraryAddress)) {
            fetchedLibrary.setLibraryAddress(libraryAddress);
        }

        if (Objects.nonNull(city)) {
            fetchedLibrary.setCity(city);
        }

        if (Objects.nonNull(province)) {
            fetchedLibrary.setProvince(province);
        }

        if (Objects.nonNull(postalCode)) {
            fetchedLibrary.setPostalCode(postalCode);
        }

        Library libraryWithEmail = this.searchByLibraryEmail(libraryEmail);

        if (Objects.nonNull(libraryWithEmail)) {
            if (!libraryWithEmail.getIdLibrary().equals(libraryId)
            ) {
                throw new ExceptionObjectAlreadyExists("Library already exists");
            }
        }

        return new LibraryResponseDTO(
                "Library Edited Successfully",
                libraryRepository.save(fetchedLibrary)
        );
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
