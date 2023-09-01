package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.BookEdition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookEditionRepository extends JpaRepository<BookEdition, Integer> {

    public Optional<BookEdition> findByEditor(String editor);

    public Optional<BookEdition> findByEditorAndEditionYear(String editor, Integer editionYear);

}
