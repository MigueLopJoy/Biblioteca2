package com.miguel.library.repository;

import com.miguel.library.model.ULibrarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILibrarianRepository extends JpaRepository<ULibrarian, Integer> {
}
