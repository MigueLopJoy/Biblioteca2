package com.miguel.library.services;

import com.miguel.library.DTO.UserDTOSearchLibrarianRequest;
import com.miguel.library.DTO.UserDTOSearchReaderRequest;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ImpUSearchService implements IUSearchService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<UReader> searchReaders(UserDTOSearchReaderRequest searchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UReader> criteriaQuery = criteriaBuilder.createQuery(UReader.class);
        Root<UReader> root = criteriaQuery.from(UReader.class);

        List<Predicate> predicates = new ArrayList<>();

        this.addReaderPredicates(
                criteriaBuilder,
                root,
                searchRequest,
                predicates
        );

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<UReader> typedQuery = entityManager.createQuery(criteriaQuery);

        List<UReader> searchResults = typedQuery.getResultList();

        if (searchResults.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No Reader Was Found");
        }

        return searchResults;
    }

    private void addReaderPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<UReader> root,
            UserDTOSearchReaderRequest searchRequest,
            List<Predicate> predicates
    ) {

        String name = searchRequest.getReaderName();
        String email = searchRequest.getEmail();
        String phoneNumber = searchRequest.getPhoneNumber();
        String readerNumber = searchRequest.getReaderNumber();
        Integer minBirthYear = searchRequest.getMinBirthYear();
        Integer maxBirthYear = searchRequest.getMaxBirthYear();
        Character gender = searchRequest.getGender();


        if (isValidString(name)) {
            Expression<String> readerName = this.getUserNameExpression(criteriaBuilder, root);
            predicates.add(
                    criteriaBuilder.like(
                            root.get("readerName"),
                            "%" + readerName + "%"
                    )
            );
        }

        if (isValidString(email)) {
            predicates.add(criteriaBuilder.equal(root.get("email"), email));
        }

        if (isValidString(phoneNumber)) {
            predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber));
        }

        if (isValidString(readerNumber)) {
            predicates.add(criteriaBuilder.equal(root.get("readerNumber"), readerNumber));
        }

        if (Objects.nonNull(minBirthYear)) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("birthYear"),
                            minBirthYear
                    )
            );
        }

        if (Objects.nonNull(maxBirthYear)) {
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("birthYear"),
                            maxBirthYear
                    )
            );
        }

        if (Objects.nonNull(gender)) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), gender));
        }
    }

    @Override
    public List<ULibrarian> searchLibrarians(UserDTOSearchLibrarianRequest searchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ULibrarian> criteriaQuery = criteriaBuilder.createQuery(ULibrarian.class);
        Root<ULibrarian> root = criteriaQuery.from(ULibrarian.class);

        Join<ULibrarian, Role> librarialToRoleJoin = root.join("authorities");

        List<Predicate> predicates = new ArrayList<>();

        this.addLibrarianPredicates(
                criteriaBuilder,
                root,
                librarialToRoleJoin,
                searchRequest,
                predicates
        );

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<ULibrarian> typedQuery = entityManager.createQuery(criteriaQuery);

        List<ULibrarian> searchResults = typedQuery.getResultList();

        if (searchResults.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No Librarian Was Found");
        }

        return searchResults;
    }

    private void addLibrarianPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<ULibrarian> root,
            Join<ULibrarian, Role> librarianToRoleJoin,
            UserDTOSearchLibrarianRequest searchRequest,
            List<Predicate> predicates
    ) {
        String name = searchRequest.getLibrarianName();
        String email = searchRequest.getEmail();
        String mainRole = searchRequest.getMainRole();

        if (isValidString(name)) {
            Expression<String> librarianName = this.getUserNameExpression(criteriaBuilder, root);
            predicates.add(
                    criteriaBuilder.like(
                            root.get("librarianName"),
                            "%" + librarianName + "%"
                    )
            );
        }

        if (isValidString(mainRole)) {
            predicates.add(
                    criteriaBuilder.equal(
                            librarianToRoleJoin.get("authority"),
                            mainRole
                    )
            );
        }

        if (isValidString(email)) {
            predicates.add(criteriaBuilder.equal(root.get("email"), email));
        }
    }

    private Expression<String> getUserNameExpression(CriteriaBuilder criteriaBuilder, Root<?> root) {
        Expression<String> authorName = criteriaBuilder.concat(
                criteriaBuilder.concat(
                        root.get("firstName"),
                        " "),
                root.get("lastName")
        );
        return authorName;
    }

    private Boolean isValidString(String value) {
        return !StringUtils.isEmpty(value) && !value.trim().isEmpty();
    }
}
