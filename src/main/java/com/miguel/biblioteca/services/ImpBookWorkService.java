package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.BookWork;
import com.miguel.biblioteca.repositories.IBookWorkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ImpBookWorkService implements IBookWorkService{

    private final IBookWorkRepository bookWorkRepository;

    private final IAuthorService authorService;
    @Override
    public BookWork saveNewBookWork(BookWork bookWork) {
        BookWork savedBookWork = null;
        Author author = bookWork.getAuthor();

        if (author != null) {

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
            throw new RuntimeException("Author information not provided");
        }
        return savedBookWork;
    }

    @Override
    public List<BookWork> searchByTitle(String title) {
        return bookWorkRepository.findByTitle(title);
    }

    @Override
    public List<BookWork> searchByAuthor(Author author) {
        return bookWorkRepository.findByAuthor(author);
    }

    @Override
    public BookWork searchByTitleAndAuthor(String title, Author author) {
        return bookWorkRepository.findByTitleAndAuthor(title, author).orElse(null);
    }


}
