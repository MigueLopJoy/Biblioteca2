package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.BookEdition;
import com.miguel.biblioteca.model.BookWork;
import com.miguel.biblioteca.repositories.IBookEditionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ImpBookEditionService implements IBookEditionService{

    private final IBookEditionRepository bookEditionRepository;
    private final IBookWorkService bookWorkService;
    @Override
    public BookEdition saveNewBookEdition(BookEdition bookEdition) {
        BookEdition savedBookEdition = null;

        BookWork bookWork = bookEdition.getBookWork();

        if (bookWork != null) {

            bookWorkService.saveNewBookWork(bookWork);

            Optional<BookEdition> optionalBookEdition
                    = bookEditionRepository.findByEditorAndEditionYear(
                        bookEdition.getEditor(),
                        bookEdition.getEditionYear()
                    );

            if (!optionalBookEdition.isPresent()) {
                savedBookEdition = bookEditionRepository.save(bookEdition);
            }
        } else {
            throw new RuntimeException("Book work information not provided");
        }

        return savedBookEdition;
    }

    @Override
    public BookEdition searchByEditorAndEditionYear(String edition, Integer editionYear) {
        return bookEditionRepository.findByEditorAndEditionYear(edition, editionYear).orElse(null);
    }


}
