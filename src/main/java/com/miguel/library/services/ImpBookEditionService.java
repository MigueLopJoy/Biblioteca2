package com.miguel.library.services;

import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImpBookEditionService implements IBookEditionService{

    @Autowired
    private IBookEditionRepository bookEditionRepository;

    @Autowired
    private IBookWorkService bookWorkService;

    @Override
    public BookEdition saveNewBookEdition(BookEdition bookEdition) {
        BookEdition savedBookEdition = null;

        if (bookEdition != null) {
            BookWork bookWork = bookEdition.getBookWork();

            if (bookWork != null) {
                BookWork savedBookWork = bookWorkService.findByTitleAndAuthor(bookWork);

                if (savedBookWork != null) {
                    Optional<BookEdition> optionalBookEdition
                            = bookEditionRepository.findByISBN(bookEdition.getISBN());

                    if (!optionalBookEdition.isPresent()) {
                        bookEdition.setBookWork(savedBookWork);
                        savedBookEdition = bookEditionRepository.save(bookEdition);
                    } else {
                        savedBookEdition = optionalBookEdition.get();
                    }
                }
            }
        } else {
            throw new RuntimeException("Book Edition information not provided");
        }
        return savedBookEdition;
    }

    @Override
    public BookEdition findByISBN(String ISBN) {
        return bookEditionRepository.findByISBN(ISBN).orElse(null);
    }

    @Override
    public List<BookEdition> findBookWorkEditions(BookWork bookWork) {
        List<BookEdition> bookWorkEditions = new ArrayList<>();

        if (bookWork != null) {
            BookWork fetchedBookWork = this.fetchBookWork(bookWork);
            if (fetchedBookWork != null) {
                bookWorkEditions.addAll(bookEditionRepository.findByBookWork(fetchedBookWork));
            }
        }
        return bookWorkEditions;
    }

    private BookWork fetchBookWork(BookWork bookWork) {
        return bookWorkService.findByTitleAndAuthor(bookWork);
    }
}
