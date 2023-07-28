package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.Library;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class ILibraryRepositoryTest {

    @Autowired
    private ILibraryRepository underTest;

    @Test
    void itShouldSaveNewLibrary() {
        // Given

        Library library = new Library("BPM de Teba", "C/ San Francisco nº 46", "Teba", "Málaga", "29327");
        // When

        underTest.save(library);

        // Then

        Library savedLibrary = underTest.findAll().get(0);
        Integer savedLibraryId = savedLibrary.getIdLibrary();

        Optional<Library> optionalLibrary = underTest.findById(savedLibraryId);
        assertThat(optionalLibrary)
                .isPresent()
                .hasValueSatisfying(r -> {
                    assertThat(r).isEqualToComparingFieldByField(library);
                });
    }

    @Test
    void itShouldNotSaveNewLibraryWhenPostalCodeIsNull() {
        // Given

        Library library = new Library("BPM de Teba", "C/ San Francisco nº 46", "Teba", "Málaga", null);
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining("not-null property references a null or transient value : com.miguel.biblioteca.model.Library.postalCode")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveNewLibraryWhenProvinceIsNull() {
        // Given

        Library library = new Library("BPM de Teba", "C/ San Francisco nº 46", "Teba", null, "29327");
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining("not-null property references a null or transient value : com.miguel.biblioteca.model.Library.province")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveNewLibraryWhenCityIsNull() {
        // Given

        Library library = new Library("BPM de Teba", "C/ San Francisco nº 46", null, "Málaga", "29327");
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining("not-null property references a null or transient value : com.miguel.biblioteca.model.Library.city")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveNewLibraryWhenLibraryAddressIsNull() {
        // Given

        Library library = new Library("BPM de Teba", null, "Teba", "Málaga", "29327");
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining("not-null property references a null or transient value : com.miguel.biblioteca.model.Library.libraryAddress")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveNewLibraryWhenLibraryNameIsNull() {
        // Given

        Library library = new Library(null, "C/ San Francisco nº 46", "Teba", "Málaga", "29327");
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining("not-null property references a null or transient value : com.miguel.biblioteca.model.Library.libraryName")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}