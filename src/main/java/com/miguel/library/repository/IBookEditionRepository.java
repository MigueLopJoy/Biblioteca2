package com.miguel.library.repository;

import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookEditionRepository extends JpaRepository<BookEdition, Integer> {

    public Optional<BookEdition> findByEditorAndEditionYear(String editor, Integer editionYear);

    public List<BookEdition> findByBookWork(BookWork bookWork);
}
