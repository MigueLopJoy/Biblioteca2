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
import java.util.Objects;

@Service
public class ImpReaderSearchService implements IReaderSearchService {

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
        LocalDate minDateOfBirth = searchRequest.getMinDateOfBirth();
        LocalDate maxDateOfBirth = searchRequest.getMinDateOfBirth();
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

        if (isValidString(email)) {
            predicates.add(criteriaBuilder.equal(root.get("email"), email));
        }

        if (isValidString(phoneNumber)) {
            predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber));
        }

        if (isValidString(readerNumber)) {
            predicates.add(criteriaBuilder.equal(root.get("readerNumber"), readerNumber));
        }

        if (Objects.nonNull(minDateOfBirth)
                && Objects.nonNull(maxDateOfBirth)) {

            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("dateOfBirth"),
                            minDateOfBirth
                    )
            );

            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("dateOfBirth"),
                            maxDateOfBirth
                    )
            );

            if (Objects.nonNull(gender)){
                predicates.add(criteriaBuilder.equal(root.get("status"), gender));
            }
        }
    }

    private Boolean isValidString (String value) {
        return !StringUtils.isEmpty(value) && !value.trim().isEmpty();
    }
}
