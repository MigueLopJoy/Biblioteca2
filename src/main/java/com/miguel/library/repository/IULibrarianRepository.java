package com.miguel.library.repository;

import com.miguel.library.model.ULibrarian;
import com.miguel.library.model.UReader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IULibrarianRepository extends JpaRepository<ULibrarian, Integer> {
    public Optional<ULibrarian> findByEmail(String email);


}
