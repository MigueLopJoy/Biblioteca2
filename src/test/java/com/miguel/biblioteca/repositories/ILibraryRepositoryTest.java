package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.Library;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class ILibraryRepositoryTest {
    private String libraryName;
    private String libraryPhoneNumber;
    private String libraryEmail;
    private String libraryAddress;
    private String city;
    private String province;
    private String postalCode;
    private List<ULibrarian> currentLibrarians;
    private ULibrarian libraryManager;
    @Autowired
    private IULibrarianRepository iuLibrarianRepository;
    @Autowired
    private ILibraryRepository underTest;

    @BeforeEach
    void setUp() {
        libraryName = "libraryName";
        libraryPhoneNumber = "6666";
        libraryEmail = "library@email.com";
        libraryAddress = "libraryAddress";
        city = "City";
        province = "Province";
        postalCode = "postalCode";

        currentLibrarians = new ArrayList<>();
    }

    @Test
    void itShouldSaveNewLibraryWithUniqueDetails() {
        // Given

        Library library
                = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

        // When
        Library savedLibrary = underTest.save(library);

        // Then
        Library fetchedLibrary = underTest.findAll().get(0);
        Integer savedLibraryId = fetchedLibrary.getIdLibrary();

        Optional<Library> optionalLibrary = underTest.findById(savedLibraryId);
        assertThat(optionalLibrary)
                .isPresent()
                .hasValueSatisfying(r -> {
                    assertThat(r).isEqualToComparingFieldByField(savedLibrary);
                    assertThat(r).isEqualToComparingFieldByField(fetchedLibrary);
                });
    }

    @Test
    void itShouldNotSaveNewLibraryWhenPostalCodeIsNull() {
        // Given

        Library library
                = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, null, currentLibrarians);
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining(
                        "not-null property references a null or transient value : com.miguel.biblioteca.model.Library.postalCode"
                )
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveNewLibraryWhenProvinceIsNull() {
        // Given

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, null, postalCode, currentLibrarians);
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining(
                        "not-null property references a null or transient value : com.miguel.biblioteca.model.Library.province"
                )
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveNewLibraryWhenCityIsNull() {
        // Given
        given(iuLibrarianRepository.save(any(ULibrarian.class))).willReturn(libraryManager);
        currentLibrarians.add(libraryManager);

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, null, province, postalCode, currentLibrarians);
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining(
                        "not-null property references a null or transient value : com.miguel.biblioteca.model.Library.city"
                )
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveNewLibraryWhenLibraryAddressIsNull() {
        // Given
        given(iuLibrarianRepository.save(any(ULibrarian.class))).willReturn(libraryManager);
        currentLibrarians.add(libraryManager);

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, null, city, province, postalCode, currentLibrarians);
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining(
                        "not-null property references a null or transient value : com.miguel.biblioteca.model.Library.libraryAddress"
                )
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveNewLibraryWhenLibraryNameIsNull() {
        // Given

        Library library = new Library(null, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);
        // When
        // Then

        assertThatThrownBy(() -> underTest.save(library))
                .hasMessageContaining("not-null property references a null or transient value : com.miguel.biblioteca.model.Library.libraryName")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldFindLibraryByNameAddressAndPlace() {
        // Given
        String place = city + " " + province + " " + postalCode;
        String libraryNameAddressAndPlace = libraryName + " " + libraryAddress + " " + place;

        System.out.println(libraryNameAddressAndPlace);

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

        Library savedLibrary = underTest.save(library);
        Optional<Library> optionalLibrary;

        // When
        optionalLibrary = underTest.findLibraryByNameAddressAndPlace(libraryNameAddressAndPlace);

        // Then
        assertThat(optionalLibrary)
                .isPresent()
                .hasValueSatisfying(l -> {
                    assertThat(l).isEqualToComparingFieldByField(savedLibrary);
                });
    }

    @Test
    void itShouldFindLibraryByNameAndPlace() {
        // Given
        String place = city + " " + province + " " + postalCode;
        String libraryNameAndPlace = libraryName + " " + place;

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

        Library savedLibrary = underTest.save(library);
        Optional<Library> optionalLibrary;

        // When
        optionalLibrary = underTest.findLibraryByNameAndPlace(libraryNameAndPlace);

        // Then
        assertThat(optionalLibrary)
                .isPresent()
                .hasValueSatisfying(l -> {
                    assertThat(l).isEqualToComparingFieldByField(library);
                });
    }

    @Test
    void itShouldFindLibraryByAddressAndPlace() {
        // Given
        given(iuLibrarianRepository.save(any(ULibrarian.class))).willReturn(libraryManager);
        currentLibrarians.add(libraryManager);

        String place = city + " " + province + " " + postalCode;
        String libraryAddressAndPlace = libraryAddress + " " + place;

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

        Library savedLibrary = underTest.save(library);
        Optional<Library> optionalLibrary;

        // When
        optionalLibrary = underTest.findLibraryByAddressAndPlace(libraryAddressAndPlace);

        // Then
        assertThat(optionalLibrary)
                .isPresent()
                .hasValueSatisfying(l -> {
                    assertThat(l).isEqualToComparingFieldByField(savedLibrary);
                });
    }

    @Test
    void itShouldNotFindLibraryByNameAddressAndPlaceWhenLibraryNameDoesNotExist() {
        // Given
        String nonExistingName = "Non Existing Name";
        String place = city + " " + province + " " + postalCode;
        String libraryNameAddressAndPlace = nonExistingName + " " + libraryAddress + " " + place;

        System.out.println(libraryNameAddressAndPlace);

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

        Library savedLibrary = underTest.save(library);

        Optional<Library> optionalLibrary;

        // When
        optionalLibrary = underTest.findLibraryByNameAddressAndPlace(libraryNameAddressAndPlace);

        // Then
        assertThat(optionalLibrary).isNotPresent();
    }

    @Test
    void itShouldNotFindLibraryByNameAddressAndPlaceWhenLibraryPlaceDoesNotExist() {
        // Given
        String nonExistingPlace = "Non Existing Place";
        String libraryNameAddressAndPlace = libraryName + " " + libraryAddress + " " + nonExistingPlace;

        System.out.println(libraryNameAddressAndPlace);

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

        Library savedLibrary = underTest.save(library);

        Optional<Library> optionalLibrary;

        // When
        optionalLibrary = underTest.findLibraryByNameAddressAndPlace(libraryNameAddressAndPlace);

        // Then
        assertThat(optionalLibrary).isNotPresent();
    }

    @Test
    void itShouldNotFindLibraryByNameAddressAndPlaceWhenLibraryAddressDoesNotExist() {
        // Given
        String nonExistingAddress = "Non Existing Address";
        String place = city + " " + province + " " + postalCode;
        String libraryNameAddressAndPlace = libraryName + " " + nonExistingAddress + " " + place;

        System.out.println(libraryNameAddressAndPlace);

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

        Library savedLibrary = underTest.save(library);

        Optional<Library> optionalLibrary;

        // When
        optionalLibrary = underTest.findLibraryByNameAddressAndPlace(libraryNameAddressAndPlace);

        // Then
        assertThat(optionalLibrary).isNotPresent();
    }

    @Test
    void itShouldNotFindLibraryByNameAddressAndPlaceWhenLibraryNameAddressAndPlaceDoNotExist() {
        // Given
        String nonExistingName = "Non Existing Name";
        String nonExistingAddress = "Non Existing Address";
        String nonExistingPlace = "Non existing Place";
        String libraryNameAddressAndPlace = nonExistingName + " " + nonExistingAddress + " " + nonExistingPlace;

        System.out.println(libraryNameAddressAndPlace);

        Library library = new Library(libraryName, libraryPhoneNumber, libraryEmail, libraryAddress, city, province, postalCode, currentLibrarians);

        Library savedLibrary = underTest.save(library);

        Optional<Library> optionalLibrary;

        // When
        optionalLibrary = underTest.findLibraryByNameAddressAndPlace(libraryNameAddressAndPlace);

        // Then
        assertThat(optionalLibrary).isNotPresent();
    }
}