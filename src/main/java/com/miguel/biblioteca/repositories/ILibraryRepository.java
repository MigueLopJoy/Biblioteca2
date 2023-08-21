package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILibraryRepository extends JpaRepository<Library, Integer>{
    @Query("SELECT l " +
            "FROM Library l " +
            "WHERE CONCAT(" +
            "l.libraryName, ' ', l.libraryAddress, ' ', l.city, ' ', l.province, ' ', l.postalCode" +
            ") = :libraryNameAddressAndPlace")
    public Optional<Library> findLibraryByNameAddressAndPlace(
            @Param("libraryNameAddressAndPlace") String libraryNameAddressAndPlace
    );

    @Query("SELECT l " +
            "FROM Library l " +
            "WHERE CONCAT(" +
            "l.libraryName, ' ', l.city, ' ', l.province, ' ', l.postalCode" +
            ") = :libraryNameAndPlace")
    public Optional<Library> findLibraryByNameAndPlace(
            @Param("libraryNameAndPlace") String libraryNameAndPlace
    );
    @Query("SELECT l " +
            "FROM Library l " +
            "WHERE CONCAT(" +
            "l.libraryAddress, ' ', l.city, ' ', l.province, ' ', l.postalCode" +
            ") = :libraryAddressAndPlace")
    public Optional<Library> findLibraryByAddressAndPlace(
            @Param("libraryAddressAndPlace") String libraryAddressAndPlace
    );
}
