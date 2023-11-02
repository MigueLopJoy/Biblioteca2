package com.miguel.library.services;

import com.miguel.library.DTO.BookSearchRequestBookCopy;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ImpBookSearchServiceTest {

    @InjectMocks
    private IBookSearchService bookSearchService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<BookCopy> criteriaQuery;

    @Mock
    private Root<BookCopy> root;

    @Mock
    private Join<BookCopy, BookEdition> bookCopyToBookEditionJoin;

    @Test
    void itShouldSearchBooks() {
        // Given
        // When
        // Then
        // Configura tu objeto bookSearchRequest con las restricciones necesarias
        BookSearchRequestBookCopy bookSearchRequest = new BookSearchRequestBookCopy();
        bookSearchRequest.setMaxRegistrationNumber(10L); // Cambia este valor según tus necesidades

        // Configura el comportamiento esperado del EntityManager y otros mocks según sea necesario

        // Llama al método que deseas probar
        List<?> result = bookSearchService.searchBooks(bookSearchRequest);

        // Realiza las afirmaciones necesarias
        // Por ejemplo, verifica que solo se devuelvan libros con registrationNumber <= 100L
        for (Object bookCopy : result) {

            assertTrue(bookCopy.getRegistrationNumber() <= 100L);
        }

        // También puedes verificar que se hayan llamado los métodos adecuados en los mocks
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).getResultList();
        // Agrega más verificaciones según sea necesario

        // Limpia los mocks después del test
        reset(entityManager, criteriaBuilder, criteriaQuery, root, bookCopyToBookEditionJoin);
    }
}
}