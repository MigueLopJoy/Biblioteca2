package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

 
public interface IAuthorRepository extends JpaRepository<Integer, Author>{

    @Query("SELECT a FROM Author u WHERE CONCAT(a.firstName, ' ', a.lastNames) = :authorName")
    public Optional<Author> findByAuthorName(@Param("fullName") String authorName);   
}
