package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.BookWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookWorkRepository extends JpaRepository<BookWork, Integer> {

    public List<BookWork> findByTitle(String title);
    public List<BookWork> findByAuthor(Author author);
    public Optional<BookWork> findByTitleAndAuthor(String title, Author author);
}
