package com.miguel.library.repository;

import com.miguel.library.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILibraryRepository extends JpaRepository<Library, Integer> {

    public Optional<Library> findByLibraryEmail(String libraryEmail);

    public Optional<Library> findByLibraryPhoneNumber(String libraryPhoneNumber);

    public Optional<Library> findByLibraryName(String libraryName);
}
