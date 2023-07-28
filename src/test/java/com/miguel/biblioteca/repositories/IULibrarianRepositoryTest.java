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
    @Autowired
    private IRoleRepository roleRepository;
    private ULibrarian uLibrarian;
    private Integer id;
    private String email;

    private Set<Role> authorities;

    @BeforeEach
    void setUp() {
        email = "example_email@example.com";

        id = 10;

        uLibrarian = new ULibrarian("Miguel", "López", "626100833", email, "1234");
    }


    @Test
    void itShouldSaveULibrarian() {
        // Given
        Integer id = 1;
        ULibrarian savedLibrarian;

        // When
        savedLibrarian = underTest.save(uLibrarian);

        // Then
        assertThat(savedLibrarian)
                .isNotNull()
                .isEqualToComparingFieldByField(uLibrarian);

        Optional<ULibrarian> fetchedLibrarian = underTest.findByUserEmail(email);

        assertThat(fetchedLibrarian)
                .isPresent()
                .hasValueSatisfying(l -> {
                    assertThat(l).isEqualToComparingFieldByField(savedLibrarian);
                });
    }

    @Test
    void itShouldNotSaveULibrarianWhenPasswordIsNull() {
        // Given
        ULibrarian nullPasswordLibrarian = new ULibrarian("Miguel", "López", "626100833", email, null);

        // When
        // Then

        assertThatThrownBy(() -> underTest.save(nullPasswordLibrarian))
                .hasMessageContaining("not-null property references a null or transient value : com.miguel.biblioteca.model.ULibrarian.password")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldFindByUserEmail() {
        // Given
       // When
        underTest.save(uLibrarian);

        // Then

        Optional<ULibrarian> optionalULibrarian = underTest.findByUserEmail(email);

        assertThat(optionalULibrarian)
                .isPresent()
                .hasValueSatisfying(l -> {
                    assertThat(l).isEqualToComparingFieldByField(uLibrarian);
                });
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