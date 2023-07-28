package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class IRoleRepositoryTest {

    @Autowired
    private IRoleRepository underTest;

    @Test
    void itShouldNotSaveRoleWhenAuthorityIsNull() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> underTest.save(new Role(1, null)))
                .hasMessageContaining("not-null property references a null or transient value : com.miguel.biblioteca.model.Role.authority")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldFindByAuthority() {
        // Given
        String authority = "ADMIN";
        Role role = new Role(authority);

        // When
        underTest.save(role);

        // Then
        Optional<Role> optionalRole = underTest.findByAuthority(authority);
        assertThat(optionalRole)
                .isPresent()
                .hasValueSatisfying(r -> {
                    assertThat(r).isEqualToComparingFieldByField(role);
                });
    }

    @Test
    void itShouldNotFindByAuthorityWhenAuthorityDoesNotExist() {
        // Given
        String authority = "NON EXISTING AUTHORITY";

        // When
        Optional<Role> optionalRole = underTest.findByAuthority(authority);

        // Then
        assertThat(optionalRole)
                .isNotPresent();
    }
}