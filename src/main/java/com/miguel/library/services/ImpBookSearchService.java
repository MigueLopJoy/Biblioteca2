package com.miguel.library.services;

import com.miguel.library.DTO.BookSearchRequest;
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
public class ImpBookSearchService implements IBookSearchService{

    @Autowired
    private EntityManager entityManager;
    @Override
    public List<Object> searchBooks(BookSearchRequest bookSearchRequest) {

        List<Object> searchResults = new ArrayList<>();

        if (isBookCopySearch(bookSearchRequest)) {
            searchResults.addAll(searchBookCopies(bookSearchRequest));
        } else if (isBookEditionSearch(bookSearchRequest)) {
            searchResults.addAll(searchBookEditions(bookSearchRequest));
        } else if (isBookWorkSearch(bookSearchRequest)) {
            searchResults.addAll(searchBookWorks(bookSearchRequest));
        }

        return searchResults;
    }

    private List<BookCopy> searchBookCopies(BookSearchRequest bookSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookCopy> criteriaQuery = criteriaBuilder.createQuery(BookCopy.class);
        Root<BookCopy> root = criteriaQuery.from(BookCopy.class);

        Join<BookCopy, BookEdition> bookCopyToBookEditionJoin = root.join("bookEdition", JoinType.INNER);
        Join<BookEdition, BookWork> bookEditionToBookWorkJoin = bookCopyToBookEditionJoin.join("bookWork", JoinType.INNER);
        Join<BookWork, Author> bookWorkauthorJoin = bookEditionToBookWorkJoin.join("author");

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(bookSearchRequest.getBarCode())) {
            predicates.add(criteriaBuilder.equal(root.get("barCode"), bookSearchRequest.getBarCode()));
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getSignature())) {
            predicates.add(criteriaBuilder.equal(root.get("signature"), bookSearchRequest.getSignature()));
        }


        if (bookSearchRequest.getMaxRegistrationNumber() != null && bookSearchRequest.getMinRegistrationNumber() != null) {

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


        if (bookSearchRequest.getMinRegistrationDate() != null && bookSearchRequest.getMaxRegistrationDate() != null) {

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

        if (isBookEditionSearch(bookSearchRequest)) {

            if (!StringUtils.isEmpty(bookSearchRequest.getISBN())) {
                predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("ISBN"), bookSearchRequest.getISBN()));
            }

            if (!StringUtils.isEmpty(bookSearchRequest.getEditor())) {
                predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("editor"), bookSearchRequest.getEditor()));
            }

            if (!StringUtils.isEmpty(bookSearchRequest.getLanguage())) {
                predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("language"), bookSearchRequest.getLanguage()));
            }

            if (isBookWorkSearch(bookSearchRequest)) {

                if (!StringUtils.isEmpty(bookSearchRequest.getAuthor())) {

                    Expression<String> authorName = criteriaBuilder.concat(
                            criteriaBuilder.concat(
                                    bookWorkauthorJoin.get("firstName"),
                                    " "),
                            bookWorkauthorJoin.get("lastName")
                    );


                    predicates.add(
                            criteriaBuilder.equal(
                                authorName,
                                bookSearchRequest.getAuthor()
                        )
                    );
                }

                if (!StringUtils.isEmpty(bookSearchRequest.getTitle())) {
                    predicates.add(
                            criteriaBuilder.equal(
                                bookEditionToBookWorkJoin.get("title"),
                                bookSearchRequest.getTitle()
                            )
                    );
                }
            }
        } else {
            if (isBookWorkSearch(bookSearchRequest)) {
                if (!StringUtils.isEmpty(bookSearchRequest.getAuthor())) {

                    Expression<String> authorName = criteriaBuilder.concat(
                            criteriaBuilder.concat(
                                    bookWorkauthorJoin.get("firstName"),
                                    " "),
                            bookWorkauthorJoin.get("lastName")
                    );


                    predicates.add(
                            criteriaBuilder.equal(
                                    authorName,
                                    bookSearchRequest.getAuthor()
                            )
                    );
                }

                if (!StringUtils.isEmpty(bookSearchRequest.getTitle())) {
                    predicates.add(
                            criteriaBuilder.equal(
                                    bookEditionToBookWorkJoin.get("title"),
                                    bookSearchRequest.getTitle()
                            )
                    );
                }
            }
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<BookCopy> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    private List<BookEdition> searchBookEditions(BookSearchRequest bookSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookEdition> criteriaQuery = criteriaBuilder.createQuery(BookEdition.class);
        Root<BookEdition> root = criteriaQuery.from(BookEdition.class);

        Join<BookEdition, BookWork> bookEditionToBookWorkJoin = root.join("bookWork", JoinType.INNER);
        Join<BookWork, Author> bookWorkauthorJoin = bookEditionToBookWorkJoin.join("author");

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(bookSearchRequest.getISBN())) {
            predicates.add(criteriaBuilder.equal(root.get("ISBN"), bookSearchRequest.getISBN()));
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getEditor())) {
            predicates.add(criteriaBuilder.equal(root.get("editor"), bookSearchRequest.getEditor()));
        }


        if (!StringUtils.isEmpty(bookSearchRequest.getLanguage())) {
            predicates.add(criteriaBuilder.equal(root.get("language"), bookSearchRequest.getLanguage()));
        }

        if (isBookWorkSearch(bookSearchRequest)) {
            if (!StringUtils.isEmpty(bookSearchRequest.getAuthor())) {

                Expression<String> authorName = criteriaBuilder.concat(
                        criteriaBuilder.concat(
                                bookWorkauthorJoin.get("firstName"),
                                " "),
                        bookWorkauthorJoin.get("lastName")
                );


                predicates.add(
                        criteriaBuilder.equal(
                                authorName,
                                bookSearchRequest.getAuthor()
                        )
                );
            }

            if (!StringUtils.isEmpty(bookSearchRequest.getTitle())) {
                predicates.add(
                        criteriaBuilder.equal(
                                bookEditionToBookWorkJoin.get("title"),
                                bookSearchRequest.getTitle()
                        )
                );
            }
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<BookEdition> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    private List<BookWork> searchBookWorks(BookSearchRequest bookSearchRequest){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookWork> criteriaQuery = criteriaBuilder.createQuery(BookWork.class);
        Root<BookWork> root = criteriaQuery.from(BookWork.class);

        Join<BookWork, Author> bookWorkauthorJoin = root.join("author");

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(bookSearchRequest.getAuthor())) {

            Expression<String> authorName = criteriaBuilder.concat(
                    criteriaBuilder.concat(
                            bookWorkauthorJoin.get("firstName"),
                            " "),
                    bookWorkauthorJoin.get("lastName")
            );


            predicates.add(
                    criteriaBuilder.equal(
                            authorName,
                            bookSearchRequest.getAuthor()
                    )
            );
        }

        if (!StringUtils.isEmpty(bookSearchRequest.getTitle())) {
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

    private boolean isBookCopySearch(BookSearchRequest bookSearchRequest) {
        return (!StringUtils.isEmpty(bookSearchRequest.getBarCode()) ||
                (bookSearchRequest.getMinRegistrationNumber() != null &&
                    bookSearchRequest.getMaxRegistrationNumber() != null) ||
                (bookSearchRequest.getMinRegistrationDate() != null &&
                    bookSearchRequest.getMaxRegistrationDate() != null) ||
                !StringUtils.isEmpty(bookSearchRequest.getSignature()));
    }

    private boolean isBookEditionSearch(BookSearchRequest bookSearchRequest) {
        return (!StringUtils.isEmpty(bookSearchRequest.getEditor()) ||
                !StringUtils.isEmpty(bookSearchRequest.getISBN())) ||
                !StringUtils.isEmpty(bookSearchRequest.getLanguage());
    }


    private boolean isBookWorkSearch(BookSearchRequest bookSearchRequest) {
        return (!StringUtils.isEmpty(bookSearchRequest.getTitle()) ||
                !StringUtils.isEmpty(bookSearchRequest.getAuthor()));
    }
}










