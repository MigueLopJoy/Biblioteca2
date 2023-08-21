package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class IULibrarianRepositoryTest {

    @Autowired
    private IULibrarianRepository underTest;
    private ULibrarian uLibrarian;
    private String email;
    private String phoneNumber;
    private Set<Role> authorities;
    private Role role;
    private static Integer counter = 0;

    @BeforeEach
    void setUp() {
        email = "example_email_" + counter + "@example.com";
        phoneNumber = "phoneNumber" + counter;
        authorities = new HashSet<>();
        role = new Role("ADMIN");
        authorities.add(role);
        uLibrarian
                = new ULibrarian("Miguel", "López", phoneNumber, email, "1234", authorities);
        counter++;
    }

    @Test
    void itShouldSaveULibrarian() {
        // Given
        ULibrarian savedLibrarian;
        ULibrarian fetchedLibrarian;

        // When
        savedLibrarian = underTest.save(uLibrarian);
        fetchedLibrarian = underTest.findAll().get(0);

        // Then
        assertThat(savedLibrarian)
                .isNotNull()
                .isEqualToComparingFieldByField(uLibrarian);

        assertThat(fetchedLibrarian)
                .isNotNull()
                .isEqualToComparingFieldByField(uLibrarian);
    }

    @Test
    void itShouldNotSaveULibrarianWhenPasswordIsNull() {
        // Given
        ULibrarian nullPasswordLibrarian
                = new ULibrarian("Miguel",
                "López",
                "626100833",
                email,
                null,
                authorities);

        // When
        // Then
        assertThatThrownBy(() -> underTest.save(nullPasswordLibrarian))
                .hasMessageContaining(
                        "not-null property references a null or transient value : com.miguel.biblioteca.model.ULibrarian.password"
                )
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotFindByUserEmailWhenEmailDoesNotExist() {
        // Given
        String email = "nonexistent@email.com";

        // When
        Optional<ULibrarian> optionalULibrarian = underTest.findByUserEmail(email);

        // Then
        assertThat(optionalULibrarian)
                .isNotPresent();
    }
}