package com.miguel.library.repository;

import com.miguel.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Integer> {
    @Query("SELECT a FROM Author a WHERE CONCAT(a.firstName, ' ', a.lastName) = :authorName")
    public Optional<Author> findByAuthorName(@Param("authorName") String authorName);

    @Query("SELECT a FROM Author a WHERE CONCAT(a.firstName, ' ', a.lastName) LIKE %:authorName%")
    public List<Author> findByCustomizedSearch(
            @Param("authorName") String authorName
    );
}
