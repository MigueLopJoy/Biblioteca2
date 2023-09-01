package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.BookCopy;
import com.miguel.biblioteca.model.BookWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookCopyRepository extends JpaRepository<BookCopy, Integer> {
    public Optional<BookCopy> findByBookCopyCode(String bookCopyCode);

}
