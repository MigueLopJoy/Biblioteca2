package com.miguel.library.services;

import com.miguel.library.DTO.BookEditBookWork;
import com.miguel.library.DTO.BookSaveBookWork;
import com.miguel.library.model.Author;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
                Author savedAuthor = authorService.searchByAuthorName(bookAuthor);

                if (savedAuthor != null) {
                    BookWork bookWorkWithTitleAndAuthor = this.searchByTitleAndAuthor(bookWork);

                    if (bookWorkWithTitleAndAuthor == null) {
                        bookWork.setAuthor(savedAuthor);
                        savedBookWork = bookWorkRepository.save(bookWork);
                    }
                } else {

                }
            }
        }
        return savedBookWork;
    }

    @Override
    public BookWork searchByTitleAndAuthor(BookWork bookWork) {
        BookWork foundBookWork = null;

        if (bookWork.getAuthor() != null) {
            Author fetchedAuthor = authorService.searchByAuthorName(bookWork.getAuthor());
            if (fetchedAuthor != null) {
                Optional<BookWork> optionalBookWork
                        = bookWorkRepository.findByTitleAndAuthor(bookWork.getTitle(), fetchedAuthor);

                if (optionalBookWork.isPresent()) {
                    foundBookWork = optionalBookWork.get();
                }
            }
        }
        return foundBookWork;
    }

    @Override
    public BookWork editBookWork(Integer bookWorkId, BookEditBookWork bookEdit) {
        BookWork editedBookWork = null;
        String title = bookEdit.getTitle();
        Integer publicationYear = bookEdit.getPublicationYear();

        Optional<BookWork> optionalBookWork = bookWorkRepository.findById(bookWorkId);

        if (optionalBookWork.isPresent()) {
            BookWork savedBookWork = optionalBookWork.get();

            if (!StringUtils.isEmpty(title) && !title.trim().isBlank()) {
                savedBookWork.setTitle(title);
            }

            if (publicationYear != null) {
                savedBookWork.setPublicationYear(publicationYear);
            }

            editedBookWork = this.saveNewBookWork(savedBookWork);
        }

        return editedBookWork;
    }

    @Override
    public void deleteBookWork(Integer bookWorkId) {
        Optional<BookWork> optionalBookWork = bookWorkRepository.findById(bookWorkId);

        if (optionalBookWork.isPresent()) {
            bookWorkRepository.deleteById(bookWorkId);
        }
    }

    @Override
    public BookWork createBookWorkFromBookSaveDTO(BookSaveBookWork bookWork) {
        return new BookWork().builder()
                    .title(bookWork.getTitle())
                    .author(authorService.createAuthorFromDTO(bookWork.getAuthor()))
                    .publicationYear(bookWork.getPublicationYear())
                .build();
    }

}
