package com.miguel.library.services;

import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookEditionRepository;
import com.miguel.library.repository.IBookWorkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ImpBookEditionService implements IBookEditionService{

    private final IBookEditionRepository bookEditionRepository;

    private final IBookWorkService bookWorkService;

    private final IBookWorkRepository bookWorkRepository;


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

    @Override
    public List<BookEdition> findEditionsByAuthorName(String authorName) {
        List<BookEdition> bookEditions = new ArrayList<>();
        List<BookWork> authorBookWorks = bookWorkRepository.findBookWorkByAuthorName(authorName);

        if (authorBookWorks.size() > 0) {
            authorBookWorks.forEach(authorBookWork -> {
                
            });
        }
    }
}
