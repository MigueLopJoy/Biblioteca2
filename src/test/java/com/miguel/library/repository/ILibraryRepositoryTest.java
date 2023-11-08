package com.miguel.library.repository;

import com.miguel.library.model.Library;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.Assert.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ILibraryRepositoryTest {

    @Autowired
    private ILibraryRepository libraryRepository;

    @Test
    void itShouldSave() {
        // Given
        Library libraryToSave = createSampleLibrary();

        // When
        Library savedLibrary = libraryRepository.save(libraryToSave);
        Library retrievedLibrary = libraryRepository.findById(savedLibrary.getIdLibrary()).orElse(null);

        // Then
        assertEquals(libraryToSave.getLibraryName(), retrievedLibrary.getLibraryName());

    }

    private Library createSampleLibrary() {
            Library library = new Library();
            library.setLibraryName("Example Library");
            library.setLibraryPhoneNumber("+1234567890");
            library.setLibraryEmail("library@example.com");
            library.setLibraryAddress("123 Main Street");
            library.setCity("Cityville");
            library.setProvince("Provinceland");
            library.setPostalCode("12345-6789");
            return library;
    }
}