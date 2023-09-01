package com.miguel.library.services;

import com.miguel.library.model.Author;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookWorkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ImpBookWorkService implements IBookWorkService{

    private final IBookWorkRepository bookWorkRepository;

    private final IAuthorService authorService;

    @Override
    public BookWork saveNewBookWork(BookWork bookWork) {
        BookWork savedBookWork = null;
        Author author;

        if (bookWork != null) {
            author = bookWork.getAuthor();
            authorService.saveNewAuthor(author);

            Optional<BookWork> optionalBookWork =
                    bookWorkRepository.findByTitleAndAuthor(
                            bookWork.getTitle(),
                            bookWork.getAuthor()
                    );

            if (!optionalBookWork.isPresent()) {
                savedBookWork = bookWorkRepository.save(bookWork);
            }
        } else {
            throw new RuntimeException("Book Work information not provided");
        }

        return savedBookWork;
    }
}
