package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.ULibrarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IULibrarianRepository extends JpaRepository<ULibrarian, Integer> {
    public Optional<ULibrarian> findByUserEmail(String userEmail);

    public Optional<ULibrarian> findByUserPhoneNumber(String phoneNumber);
}
