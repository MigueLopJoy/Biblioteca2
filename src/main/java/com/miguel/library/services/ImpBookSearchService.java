package com.miguel.library.services;

import com.miguel.library.DTO.BookSearchRequestBookCopy;
import com.miguel.library.DTO.BookSearchRequestBookEdition;
import com.miguel.library.DTO.BookSearchRequestBookWork;
import com.miguel.library.model.Author;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImpBookSearchService implements IBookSearchService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Object> searchBooks(BookSearchRequestBookWork bookSearchRequest) {

        List<Object> searchResults = new ArrayList<>();

        if (bookSearchRequest instanceof BookSearchRequestBookCopy) {
            searchResults.addAll(searchBookCopies((BookSearchRequestBookCopy) bookSearchRequest));
        } else if (bookSearchRequest instanceof BookSearchRequestBookEdition) {
            searchResults.addAll(searchBookEditions((BookSearchRequestBookEdition) bookSearchRequest));
        } else if (bookSearchRequest instanceof BookSearchRequestBookWork) {
            searchResults.addAll(searchBookWorks(bookSearchRequest));
        }

        return searchResults;
    }

    private List<BookCopy> searchBookCopies(BookSearchRequestBookCopy bookSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookCopy> criteriaQuery = criteriaBuilder.createQuery(BookCopy.class);
        Root<BookCopy> root = criteriaQuery.from(BookCopy.class);

        Join<BookCopy, BookEdition> bookCopyToBookEditionJoin = root.join("bookEdition", JoinType.INNER);
        Join<BookEdition, BookWork> bookEditionToBookWorkJoin = bookCopyToBookEditionJoin.join("bookWork", JoinType.INNER);
        Join<BookWork, Author> bookWorkauthorJoin = bookEditionToBookWorkJoin.join("author");

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(bookSearchRequest.getBarCode()) &&
                !bookSearchRequest.getBarCode().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("barCode"), bookSearchRequest.getBarCode()));
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getSignature())
                && !bookSearchRequest.getSignature().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("signature"), bookSearchRequest.getSignature()));
        }


        if (bookSearchRequest.getMaxRegistrationNumber() != null
                && bookSearchRequest.getMinRegistrationNumber() != null) {

            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("registrationNumber"),
                            bookSearchRequest.getMaxRegistrationNumber()
                    )
            );

            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("registrationNumber"),
                            bookSearchRequest.getMinRegistrationNumber()
                    )
            );
        }


        if (bookSearchRequest.getMinRegistrationDate() != null
                && bookSearchRequest.getMaxRegistrationDate() != null) {

            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("registrationDate"),
                            bookSearchRequest.getMinRegistrationDate()
                    )
            );

            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("registrationDate"),
                            bookSearchRequest.getMaxRegistrationDate()
                    )
            );
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getStatus())
                && !bookSearchRequest.getStatus().trim().isEmpty()){
            predicates.add(criteriaBuilder.equal(root.get("status"), bookSearchRequest.getStatus()));
        }

        if (bookSearchRequest.getBorrowed() != null){
            predicates.add(criteriaBuilder.equal(root.get("borrowed"), bookSearchRequest.getBorrowed()));
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getISBN())
                && !bookSearchRequest.getISBN().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("ISBN"), bookSearchRequest.getISBN()));
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getEditor())
                && !bookSearchRequest.getEditor().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("editor"), bookSearchRequest.getEditor()));
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getLanguage())
                && !bookSearchRequest.getLanguage().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("language"), bookSearchRequest.getLanguage()));
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getAuthor())
                && !bookSearchRequest.getAuthor().trim().isEmpty()) {

            Expression<String> authorName = criteriaBuilder.concat(
                    criteriaBuilder.concat(
                            bookWorkauthorJoin.get("firstName"),
                            " "),
                    bookWorkauthorJoin.get("lastName")
            );

            predicates.add(
                    criteriaBuilder.like(
                            authorName,
                            "%" + bookSearchRequest.getAuthor() + "%"
                    )
            );
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getTitle())
                && !bookSearchRequest.getTitle().trim().isEmpty()) {
            predicates.add(
                    criteriaBuilder.equal(
                            bookEditionToBookWorkJoin.get("title"),
                            bookSearchRequest.getTitle()
                    )
            );
        }


        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<BookCopy> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    private List<BookEdition> searchBookEditions(BookSearchRequestBookEdition bookSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookEdition> criteriaQuery = criteriaBuilder.createQuery(BookEdition.class);
        Root<BookEdition> root = criteriaQuery.from(BookEdition.class);

        Join<BookEdition, BookWork> bookEditionToBookWorkJoin = root.join("bookWork", JoinType.INNER);
        Join<BookWork, Author> bookWorkauthorJoin = bookEditionToBookWorkJoin.join("author");

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(bookSearchRequest.getISBN())
                && !bookSearchRequest.getISBN().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("ISBN"), bookSearchRequest.getISBN()));
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getEditor())
                && !bookSearchRequest.getEditor().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("editor"), bookSearchRequest.getEditor()));
        }


        if (!StringUtils.isEmpty(bookSearchRequest.getLanguage())
                && !bookSearchRequest.getLanguage().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("language"), bookSearchRequest.getLanguage()));
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getAuthor())
                && !bookSearchRequest.getAuthor().trim().isEmpty()) {

            Expression<String> authorName = criteriaBuilder.concat(
                    criteriaBuilder.concat(
                            bookWorkauthorJoin.get("firstName"),
                            " "),
                    bookWorkauthorJoin.get("lastName")
            );

            predicates.add(
                    criteriaBuilder.like(
                            authorName,
                            "%" + bookSearchRequest.getAuthor() + "%"
                    )
            );
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getTitle())
                && !bookSearchRequest.getTitle().trim().isEmpty()) {
            predicates.add(
                    criteriaBuilder.equal(
                            bookEditionToBookWorkJoin.get("title"),
                            bookSearchRequest.getTitle()
                    )
            );
        }


        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<BookEdition> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    private List<BookWork> searchBookWorks(BookSearchRequestBookWork bookSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookWork> criteriaQuery = criteriaBuilder.createQuery(BookWork.class);
        Root<BookWork> root = criteriaQuery.from(BookWork.class);

        Join<BookWork, Author> bookWorkauthorJoin = root.join("author");

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(bookSearchRequest.getAuthor())
                && !bookSearchRequest.getAuthor().trim().isEmpty()) {

            Expression<String> authorName = criteriaBuilder.concat(
                    criteriaBuilder.concat(
                            bookWorkauthorJoin.get("firstName"),
                            " "),
                    bookWorkauthorJoin.get("lastName")
            );

            predicates.add(
                    criteriaBuilder.like(
                            authorName,
                            "%" + bookSearchRequest.getAuthor() + "%"
                    )
            );
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getTitle())
                && !bookSearchRequest.getTitle().trim().isEmpty()) {
            predicates.add(
                    criteriaBuilder.equal(
                            root.get("title"),
                            bookSearchRequest.getTitle()
                    )
            );
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<BookWork> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}