package com.miguel.library.services;

import com.miguel.library.DTO.BookSearchRequestBookCopy;
import com.miguel.library.DTO.BookSearchRequestBookEdition;
import com.miguel.library.DTO.BookSearchRequestBookWork;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
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

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        Join<BookWork, Author> bookWorkAuthorJoin = bookEditionToBookWorkJoin.join("author");

        List<Predicate> predicates = new ArrayList<>();

        this.addBookCopyPredicates(
                criteriaBuilder,
                root,
                bookCopyToBookEditionJoin,
                bookEditionToBookWorkJoin,
                bookWorkAuthorJoin,
                bookSearchRequest,
                predicates
        );

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<BookCopy> typedQuery = entityManager.createQuery(criteriaQuery);

        List<BookCopy> searchResults = typedQuery.getResultList();

        if (searchResults.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No search results were found");
        }

        return searchResults;
    }

    private void addBookCopyPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<BookCopy> root,
            Join<BookCopy, BookEdition> bookCopyToBookEditionJoin,
            Join<BookEdition, BookWork> bookEditionToBookWorkJoin,
            Join<BookWork, Author> bookWorkAuthorJoin,
            BookSearchRequestBookCopy bookSearchRequest,
            List<Predicate> predicates
    ) {
        String barCode = bookSearchRequest.getBarCode();
        String signature = bookSearchRequest.getSignature();
        Long minRegistrationNumber = bookSearchRequest.getMinRegistrationNumber();
        Long maxRegistrationNumber = bookSearchRequest.getMaxRegistrationNumber();
        LocalDate minRegistrationDate = bookSearchRequest.getMinRegistrationDate();
        LocalDate maxRegistrationDate = bookSearchRequest.getMaxRegistrationDate();
        Character status = bookSearchRequest.getStatus();
        Boolean borrowed = bookSearchRequest.getBorrowed();
        String ISBN = bookSearchRequest.getISBN();
        String editor = bookSearchRequest.getEditor();
        String language = bookSearchRequest.getLanguage();
        String author = bookSearchRequest.getAuthor();
        String title = bookSearchRequest.getTitle();

        if (isValidString(barCode)) {
            predicates.add(criteriaBuilder.equal(root.get("barCode"), barCode));
        }

        if (isValidString(signature)) {
            predicates.add(criteriaBuilder.equal(root.get("signature"), signature));
        }

        if (Objects.nonNull(minRegistrationNumber)) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("registrationNumber"),
                            minRegistrationNumber
                    )
            );
        }

        if (Objects.nonNull(maxRegistrationNumber)) {
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("registrationNumber"),
                            maxRegistrationNumber
                    )
            );
        }

        if (Objects.nonNull(minRegistrationDate)) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("registrationDate"),
                            minRegistrationDate
                    )
            );
        }

        if (Objects.nonNull(maxRegistrationDate)) {
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("registrationDate"),
                            maxRegistrationDate
                    )
            );
        }

        if (Objects.nonNull(status)){
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }

        if (Objects.nonNull(borrowed)) {
            predicates.add(criteriaBuilder.equal(root.get("borrowed"), borrowed));
        }
        if (isValidString(ISBN)) {
            predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("ISBN"), ISBN));
        }

        if (isValidString(editor)) {
            predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("editor"), editor));
        }


        if (isValidString(language)) {
            predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("language"), language));
        }

        if (isValidString(author)) {
            Expression<String> authorName = this.getAuthorNameExpression(criteriaBuilder, bookWorkAuthorJoin);

            predicates.add(
                    criteriaBuilder.like(
                            authorName,
                            "%" + author + "%"
                    )
            );
        }

        if (isValidString(title)) {
            predicates.add(
                    criteriaBuilder.like(
                            bookEditionToBookWorkJoin.get("title"),
                            "%" + title + "%"
                    )
            );
        }
    }

    private List<BookEdition> searchBookEditions(BookSearchRequestBookEdition bookSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookEdition> criteriaQuery = criteriaBuilder.createQuery(BookEdition.class);
        Root<BookEdition> root = criteriaQuery.from(BookEdition.class);

        Join<BookEdition, BookWork> bookEditionToBookWorkJoin = root.join("bookWork", JoinType.INNER);
        Join<BookWork, Author> bookWorkAuthorJoin = bookEditionToBookWorkJoin.join("author");

        List<Predicate> predicates = new ArrayList<>();

        this.addBookEditionPredicates(
                criteriaBuilder,
                root,
                bookEditionToBookWorkJoin,
                bookWorkAuthorJoin,
                bookSearchRequest,
                predicates
        );

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<BookEdition> typedQuery = entityManager.createQuery(criteriaQuery);

        List<BookEdition> searchResults = typedQuery.getResultList();

        if (searchResults.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No search results were found");
        }

        return searchResults;
    }
    private void addBookEditionPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<BookEdition> root,
            Join<BookEdition, BookWork> bookEditionBookWorkJoin,
            Join<BookWork, Author> bookWorkAuthorJoin,
            BookSearchRequestBookEdition bookSearchRequest,
            List<Predicate> predicates
    ) {
        String ISBN = bookSearchRequest.getISBN();
        String editor = bookSearchRequest.getEditor();
        String language = bookSearchRequest.getLanguage();
        String author = bookSearchRequest.getAuthor();
        String title = bookSearchRequest.getTitle();

        if (isValidString(ISBN)) {
            predicates.add(criteriaBuilder.equal(root.get("ISBN"), ISBN));
        }

        if (isValidString(editor)) {
            predicates.add(criteriaBuilder.equal(root.get("editor"), editor));
        }


        if (isValidString(language)) {
            predicates.add(criteriaBuilder.equal(root.get("language"), language));
        }

        if (isValidString(author)) {
            Expression<String> authorName = this.getAuthorNameExpression(criteriaBuilder, bookWorkAuthorJoin);

            predicates.add(
                    criteriaBuilder.like(
                            authorName,
                            "%" + author + "%"
                    )
            );
        }

        if (isValidString(title)) {
            predicates.add(
                    criteriaBuilder.like(
                            bookEditionBookWorkJoin.get("title"),
                            "%" + title + "%"
                    )
            );
        }
    }

    private List<BookWork> searchBookWorks(BookSearchRequestBookWork bookSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookWork> criteriaQuery = criteriaBuilder.createQuery(BookWork.class);
        Root<BookWork> root = criteriaQuery.from(BookWork.class);

        Join<BookWork, Author> bookWorkAuthorJoin = root.join("author");

        List<Predicate> predicates = new ArrayList<>();

        this.addBookWorksPredicates(
                criteriaBuilder,
                root,
                bookWorkAuthorJoin,
                bookSearchRequest,
                predicates
        );

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<BookWork> typedQuery = entityManager.createQuery(criteriaQuery);

        List<BookWork> searchResults = typedQuery.getResultList();

        if (searchResults.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No search results were found");
        }

        return searchResults;
    }

    private List<Predicate> addBookWorksPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<BookWork> root,
            Join<BookWork, Author> bookWorkAuthorJoin,
            BookSearchRequestBookWork bookSearchRequest,
            List<Predicate> predicates
    ) {
        String author = bookSearchRequest.getAuthor();
        String title = bookSearchRequest.getTitle();

        if (isValidString(author)) {

            Expression<String> authorName = this.getAuthorNameExpression(criteriaBuilder, bookWorkAuthorJoin);

            predicates.add(
                    criteriaBuilder.like(
                            authorName,
                            "%" + author + "%"
                    )
            );
        }

        if (isValidString(title)) {
            predicates.add(
                    criteriaBuilder.like(
                            root.get("title"),
                            "%" + title + "%"
                    )
            );
        }
        return predicates;
    }

    private Expression<String> getAuthorNameExpression(CriteriaBuilder criteriaBuilder, Join<?, Author> authorJoin) {
        Expression<String> authorName = criteriaBuilder.concat(
                criteriaBuilder.concat(
                        authorJoin.get("firstName"),
                        " "),
                authorJoin.get("lastName")
        );
        return authorName;
    }
    private Boolean isValidString (String value) {
        return !StringUtils.isEmpty(value) && !value.trim().isEmpty();
    }
}