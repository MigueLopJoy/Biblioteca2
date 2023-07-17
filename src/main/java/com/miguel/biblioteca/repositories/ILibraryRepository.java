package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILibraryRepository extends JpaRepository<Library, Integer>{
    
}
