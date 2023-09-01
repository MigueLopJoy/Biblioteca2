package com.miguel.library.repository;

import com.miguel.library.model.Author;
import com.miguel.library.model.BookWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookWorkRepository extends JpaRepository<BookWork, Integer> {

    @Query("SELECT bw FROM BookWork bw " +
            "INNER JOIN bw.author a" +
            "WHERE CONCAT(a.firstName, ' ', a.lastName) = :authorName")
    public List<BookWork> findBookWorkByAuthorName(@Param("authorName") String authorName);
    public Optional<BookWork> findByTitleAndAuthor(String title, Author author);
}
