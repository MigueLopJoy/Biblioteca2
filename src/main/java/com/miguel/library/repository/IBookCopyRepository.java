package com.miguel.library.repository;

import com.miguel.library.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookCopyRepository extends JpaRepository<BookCopy, Integer> {
    public Optional<BookCopy> findByBookCopyCode(String bookCopyCode);

}
