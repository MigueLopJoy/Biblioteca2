package com.miguel.library.services;

import com.miguel.library.DTO.LibraryDTOSearchLibrary;
import com.miguel.library.DTO.UserDTOSearchReaderRequest;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.model.Library;
import com.miguel.library.model.UReader;
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
public class ImpLibrarySearchService implements ILibrarySearchService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Library> searchLibrary(LibraryDTOSearchLibrary searchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Library> criteriaQuery = criteriaBuilder.createQuery(Library.class);
        Root<Library> root = criteriaQuery.from(Library.class);

        List<Predicate> predicates = new ArrayList<>();

        this.addLibraryPredicates(
                criteriaBuilder,
                root,
                searchRequest,
                predicates
        );

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Library> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Library> searchResults = typedQuery.getResultList();

        if (searchResults.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No Library Was Found");
        }
        return searchResults;
    }

    private void addLibraryPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<Library> root,
            LibraryDTOSearchLibrary searchRequest,
            List<Predicate> predicates
    ) {

        String libraryName = searchRequest.getLibraryName();
        String city = searchRequest.getCity();
        String province = searchRequest.getProvince();

        if (isValidString(libraryName)) {
            predicates.add(criteriaBuilder.equal(root.get("email"), libraryName));
        }

        if (isValidString(city)) {
            predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), city));
        }

        if (isValidString(province)) {
            predicates.add(criteriaBuilder.equal(root.get("readerNumber"), province));

        }
    }

    private Boolean isValidString(String value) {
        return !StringUtils.isEmpty(value) && !value.trim().isEmpty();
    }
}