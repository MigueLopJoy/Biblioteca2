package com.miguel.library.repository;

import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookCopyRepository extends JpaRepository<BookCopy, Integer> {
    public Optional<BookCopy> findByBarCode(String barCode);

    public Optional<BookCopy> findByRegistrationNumber(Long registrationNumber);

    public List<BookCopy> findByBookEdition(BookEdition bookEdition);

}
