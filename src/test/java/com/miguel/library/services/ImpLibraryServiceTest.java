package com.miguel.library.services;

import com.miguel.library.DTO.LibraryResponseDTO;
import com.miguel.library.model.Library;
import com.miguel.library.repository.ILibraryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImpLibraryServiceTest {

    @Mock
    private ILibraryRepository libraryRepository;

    @InjectMocks
    private ImpLibraryService libraryService;

    @Test
    void itShouldSaveNewLibrary() {
        // Given
        Library libraryToSave = new Library();
        libraryToSave.setLibraryName("Example Library");
        libraryToSave.setLibraryPhoneNumber("+1234567890");
        libraryToSave.setLibraryEmail("library@example.com");
        libraryToSave.setLibraryAddress("123 Main Street");
        libraryToSave.setCity("Cityville");
        libraryToSave.setProvince("Provinceland");
        libraryToSave.setPostalCode("12345-6789");

        // When

        when(libraryRepository.save(any())).thenReturn(libraryToSave);

        LibraryResponseDTO response = libraryService.saveNewLibrary(libraryToSave);

        // Then
        assertEquals("New Library Created Successfully", response.getSuccessMessage());
        assertEquals(libraryToSave, response.getLibrary());
    }
}