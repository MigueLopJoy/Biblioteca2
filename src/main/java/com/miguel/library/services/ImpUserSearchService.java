package com.miguel.library.services;

import com.miguel.library.DTO.USearchReaderDTO;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImpUserSearchService implements IUserSearchService{

    @Autowired
    private EntityManager entityManager;
    public List<UReader> searchReaders(USearchReaderDTO searchRequest) {
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
            throw new ExceptionNoSearchResultsFound("No search results were found");
        }

        return searchResults;
    }

    private void addReaderPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<UReader> root,
            USearchReaderDTO searchRequest,
            List<Predicate> predicates
    ) {

        String firstName = searchRequest.getFirstName();
        String lastName = searchRequest.getLastName();
        String email = searchRequest.getEmail();
        String phoneNumber = searchRequest.getPhoneNumber();
        String readerNumber = searchRequest.getReaderNumber();
        LocalDate dateOfBirth = searchRequest.getDateOfBirth();
        Character gender = searchRequest.getGender();


        if (isValidString(firstName)) {
            predicates.add(
                    criteriaBuilder.like(
                            root.get("firstName"),
                            "%" + firstName + "%"
                    )
            );
        }

        if (isValidString(lastName)) {
            predicates.add(
                    criteriaBuilder.like(
                            root.get("lastName"),
                            "%" + lastName + "%"
                    )
            );
        }

    }

    private Boolean isValidString (String value) {
        return !StringUtils.isEmpty(value) && !value.trim().isEmpty();
    }
}
