package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Integer>{

    @Query("SELECT a FROM Author a WHERE CONCAT(a.firstName, ' ', a.lastNames) = :authorName")
    public Optional<Author> findByAuthorName(@Param("authorName") String authorName);
}
