package com.miguel.library.services;

import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookEditionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ImpBookEditionService implements IBookEditionService{

    private final IBookEditionRepository bookEditionRepository;

    private final IBookWorkService bookWorkService;


    @Override
    public BookEdition saveNewBookEdition(BookEdition bookEdition) {
        BookEdition savedBookEdition = null;
        BookWork bookWork;

        if (bookEdition != null) {
            bookWork = bookEdition.getBookWork();
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
            throw new RuntimeException("Book Edition information not provided");
        }

        return savedBookEdition;
    }
}
