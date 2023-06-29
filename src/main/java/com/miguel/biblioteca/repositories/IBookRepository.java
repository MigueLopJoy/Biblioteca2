package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository{
    public Book findByTitle(String title);
    public List<Book> findByAuthor(Author author);
}
