package com.miguel.library.services;

import com.miguel.library.model.Author;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IAuthorRepository;
import com.miguel.library.repository.IBookWorkRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImpBookWorkService implements IBookWorkService{

    @Autowired
    private IBookWorkRepository bookWorkRepository;

    @Autowired
    private IAuthorService authorService;

    @Override
    public BookWork saveNewBookWork(BookWork bookWork) {
        BookWork savedBookWork = null;

        if (bookWork != null) {
            Author bookAuthor = bookWork.getAuthor();

            if (bookAuthor != null)  {
                Author savedAuthor = authorService.findByAuthorName(bookAuthor);

                if (savedAuthor != null) {
                    Optional<BookWork> optionalBookWork =
                            bookWorkRepository.findByTitleAndAuthor(
                                    bookWork.getTitle(),
                                    savedAuthor
                            );

                    if (!optionalBookWork.isPresent()) {
                        bookWork.setAuthor(savedAuthor);
                        savedBookWork = bookWorkRepository.save(bookWork);
                    } else {
                        savedBookWork = optionalBookWork.get();
                    }
                }
            }
        } else {
            throw new RuntimeException("Book Work information not provided");
        }
        return savedBookWork;
    }

    @Override
    public List<BookWork> findAll() {
        return bookWorkRepository.findAll();
    }

    @Override
    public BookWork findByTitleAndAuthor(BookWork bookWork) {
        BookWork foundBookWork = null;

        Author fetchedAuthor = authorService.findByAuthorName(bookWork.getAuthor());
        if (fetchedAuthor != null) {
            Optional<BookWork> optionalBookWork
                    = bookWorkRepository.findByTitleAndAuthor(bookWork.getTitle(), fetchedAuthor);

            if (optionalBookWork.isPresent()) {
                foundBookWork = optionalBookWork.get();
            }
        }
        return foundBookWork;
    }

    @Override
    public List<BookWork> findAuthorBookWorks(Author author) {
        List<BookWork> authorBookWorks = new ArrayList<>();

        Author fetchedAuthor = authorService.findByAuthorName(author);
        if (fetchedAuthor != null) {
            authorBookWorks.addAll(bookWorkRepository.findByAuthor(fetchedAuthor));
        }
        return authorBookWorks;
    }

}
