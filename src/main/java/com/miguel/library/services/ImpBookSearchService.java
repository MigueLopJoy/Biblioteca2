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

        if (!StringUtils.isEmpty(bookSearchRequest.getBookCopyCode())) {
            searchResults.addAll(searchBookCopies(bookSearchRequest));
        } else if (!StringUtils.isEmpty(bookSearchRequest.getEditor())) {
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
        predicates.add(criteriaBuilder.equal(root.get("bookCopyCode"), bookSearchRequest.getBookCopyCode()));

        if (!StringUtils.isEmpty(bookSearchRequest.getEditor())) {
            predicates.add(criteriaBuilder.equal(bookCopyToBookEditionJoin.get("editor"), bookSearchRequest.getEditor()));

            if (this.isBookWorkSearch(bookSearchRequest)) {

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

                if (bookSearchRequest.getMinPublicationYear() != null &&
                        bookSearchRequest.getMaxPublicationYear() != null) {

                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    bookEditionToBookWorkJoin.get("publicationYear"),
                                    bookSearchRequest.getMinPublicationYear()
                            )
                    );

                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    bookEditionToBookWorkJoin.get("publicationYear"),
                                    bookSearchRequest.getMaxPublicationYear()
                            )
                    );
                }
            }
        } else {
            if (this.isBookWorkSearch(bookSearchRequest)) {
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

                if (bookSearchRequest.getMinPublicationYear() != null &&
                        bookSearchRequest.getMaxPublicationYear() != null) {

                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    bookEditionToBookWorkJoin.get("publicationYear"),
                                    bookSearchRequest.getMinPublicationYear()
                            )
                    );

                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    bookEditionToBookWorkJoin.get("publicationYear"),
                                    bookSearchRequest.getMaxPublicationYear()
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
        predicates.add(criteriaBuilder.equal(root.get("editor"), bookSearchRequest.getEditor()));


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

            if (bookSearchRequest.getMinPublicationYear() != null &&
                    bookSearchRequest.getMaxPublicationYear() != null) {

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                bookEditionToBookWorkJoin.get("publicationYear"),
                                bookSearchRequest.getMinPublicationYear()
                        )
                );

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                bookEditionToBookWorkJoin.get("publicationYear"),
                                bookSearchRequest.getMaxPublicationYear()
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

        if (bookSearchRequest.getMinPublicationYear() != null &&
                bookSearchRequest.getMaxPublicationYear() != null) {

            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("publicationYear"),
                            bookSearchRequest.getMinPublicationYear()
                    )
            );

            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("publicationYear"),
                            bookSearchRequest.getMaxPublicationYear()
                    )
            );
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<BookWork> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    private boolean isBookWorkSearch(BookSearchRequest bookSearchRequest) {
        return (!StringUtils.isEmpty(bookSearchRequest.getTitle()) ||
                !StringUtils.isEmpty(bookSearchRequest.getAuthor()) ||
                !StringUtils.isEmpty(bookSearchRequest.getMinPublicationYear()) ||
                !StringUtils.isEmpty(bookSearchRequest.getMaxPublicationYear()));
    }
}










