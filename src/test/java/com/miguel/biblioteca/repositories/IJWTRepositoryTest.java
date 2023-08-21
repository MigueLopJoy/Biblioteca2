package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.JWT;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class IJWTRepositoryTest {
    @Autowired
    private IJWTRepository underTest;

    @Autowired
    private IULibrarianRepository uLibrarianRepository;

    @Autowired
    private IRoleRepository roleRepository;

    String jwtToken;
    ULibrarian uLibrarian;
    ULibrarian fetchedLibrarian;
    Set<Role> authorities;
    Role role;

    @BeforeEach
    void setUp(){
        jwtToken = "token";

        authorities = new HashSet<>();
        role = roleRepository.findByAuthority("MANAGER").get();
        authorities.add(role);

        uLibrarian = uLibrarianRepository.save(
                new ULibrarian("firstName",
                    "lastName",
                    "666",
                    "mail@example.com",
                    "1234",
                    authorities
                )
        );
        fetchedLibrarian = uLibrarianRepository.findAll().get(0);
    }

    @Test
    void itShouldSaveNewToken() {
        // Given
        JWT jwt = new JWT();
        jwt.setULibrarian(fetchedLibrarian);
        jwt.setToken(jwtToken);
        jwt.setExpired(false);
        jwt.setRevoked(false);

        // When
        JWT savedToken = underTest.save(jwt);

        // Then
        JWT fetchedToken = underTest.findAll().get(0);
        Integer fetchedTokenId = fetchedToken.getIdToken();

        Optional<JWT> optionalJWT = underTest.findById(fetchedTokenId);
        assertThat(optionalJWT)
                .isPresent()
                .hasValueSatisfying(j -> {
                    assertThat(j).isEqualToComparingFieldByField(savedToken);
                    assertThat(j).isEqualToComparingFieldByField(fetchedToken);
                });
    }

    @Test
    void itShouldFindAllValidTokenByUser() {
        // Given
        List<JWT> validTokens;

        JWT jwt1 = new JWT();
        jwt1.setULibrarian(fetchedLibrarian);
        jwt1.setToken(jwtToken);
        jwt1.setExpired(false);
        jwt1.setRevoked(false);

        JWT jwt2 = new JWT();
        jwt2.setULibrarian(fetchedLibrarian);
        jwt2.setToken(jwtToken + "2");
        jwt2.setExpired(false);
        jwt2.setRevoked(false);

        // When
        JWT savedToken1 = underTest.save(jwt1);
        JWT savedToken2 = underTest.save(jwt2);

        // Then
        validTokens = underTest.findAllValidTokenByUser(fetchedLibrarian.getIdUser());

        assertThat(validTokens).isNotEmpty();

        assertTrue(validTokens.size() == 2);

        assertThat(validTokens.get(0))
                .isNotNull()
                .isEqualToComparingFieldByField(savedToken1);

        assertThat(validTokens.get(1))
                .isNotNull()
                .isEqualToComparingFieldByField(savedToken2);
    }

    @Test
    void itShouldFindByToken() {

        // Given
        JWT jwt = new JWT();
        jwt.setULibrarian(fetchedLibrarian);
        jwt.setToken(jwtToken);
        jwt.setExpired(false);
        jwt.setRevoked(false);

        // When
        JWT savedToken = underTest.save(jwt);

        // Then
        Optional<JWT> optionalToken = underTest.findByToken(jwtToken);

        assertThat(optionalToken)
                .isPresent()
                .hasValueSatisfying(j -> {
                    assertThat(j).isEqualToComparingFieldByField(savedToken);
                    assertThat(j.getToken()).isEqualTo(jwtToken);
                });
    }
}