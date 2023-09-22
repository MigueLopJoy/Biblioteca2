package com.miguel.library.services;

import com.miguel.library.DTO.BookEditBookWork;
import com.miguel.library.DTO.BookSaveBookWork;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.Exceptions.ExceptionObjectAlreadyExists;
import com.miguel.library.Exceptions.ExceptionObjectNotFound;
import com.miguel.library.model.Author;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
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

        if (Objects.isNull(bookWork)) {
            throw new ExceptionNullObject("Book work should not be null");
        }

            Author bookAuthor = bookWork.getAuthor();

        if (Objects.isNull(bookAuthor)) {
            throw new ExceptionNullObject("Book's author should not be null");
        }

        Author savedAuthor = authorService.searchByAuthorName(bookAuthor);

        if (Objects.isNull(savedAuthor)) {
            throw new ExceptionObjectNotFound("Book's author not found");
        }

        BookWork bookWorkWithTitleAndAuthor = this.searchByTitleAndAuthor(bookWork);

        if (Objects.nonNull(bookWorkWithTitleAndAuthor)) {
            throw new ExceptionObjectAlreadyExists("Book work already exists");
        }

        bookWork.setAuthor(savedAuthor);
        savedBookWork = bookWorkRepository.save(bookWork);

        return savedBookWork;
    }

    @Override
    public BookWork searchByTitleAndAuthor(BookWork bookWork) {
        BookWork foundBookWork = null;

        if (Objects.isNull(bookWork)) {
            throw new ExceptionNullObject("Book work should not be null");
        }
        Author fetchedAuthor = authorService.searchByAuthorName(bookWork.getAuthor());

        if (Objects.isNull(fetchedAuthor)){
            throw new ExceptionNullObject("Book's author should not be null");
        }

        Optional<BookWork> optionalBookWork
                        = bookWorkRepository.findByTitleAndAuthor(bookWork.getTitle(), fetchedAuthor);


        if (!optionalBookWork.isPresent()) {
            throw new ExceptionObjectNotFound("Searched book work not found");
        }

        foundBookWork = optionalBookWork.get();

        return foundBookWork;
    }

    @Override
    public List<BookWork> searchAuthorBookWorks(Author author) {
        List<BookWork> authorBookWorks;

        if (Objects.isNull(author)) {
            throw new ExceptionNullObject("Author should not be null");
        }

        Author fetchedAuthor = authorService.searchByAuthorName(author);

        if (Objects.isNull(fetchedAuthor)) {
            throw new ExceptionObjectNotFound("Searched author not found");
        }

        authorBookWorks = bookWorkRepository.findByAuthor(author);
        return authorBookWorks;
    }

    @Override
    public BookWork editBookWork(Integer bookWorkId, BookEditBookWork bookEdit) {
        BookWork editedBookWork = null;
        String title = bookEdit.getTitle();
        Integer publicationYear = bookEdit.getPublicationYear();

        Optional<BookWork> optionalBookWork = bookWorkRepository.findById(bookWorkId);

        if (!optionalBookWork.isPresent()) {
            throw new ExceptionObjectNotFound("Searched book work not found");
        }

        BookWork savedBookWork = optionalBookWork.get();

        if (!StringUtils.isEmpty(title) && !title.trim().isBlank()) {
            savedBookWork.setTitle(title);
        }

        if (publicationYear != null) {
            savedBookWork.setPublicationYear(publicationYear);
        }

        editedBookWork = this.saveNewBookWork(savedBookWork);

        return editedBookWork;
    }

    @Override
    public String deleteBookWork(Integer bookWorkId) {
        Optional<BookWork> optionalBookWork = bookWorkRepository.findById(bookWorkId);

        if (!optionalBookWork.isPresent()) {
            throw new ExceptionObjectNotFound("Book work not found");
        }

        bookWorkRepository.deleteById(bookWorkId);
        return "Book work deleted successfully";
    }

    @Override
    public BookWork createBookWorkFromBookSaveDTO(BookSaveBookWork bookWork) {
        return new BookWork().builder()
                    .title(bookWork.getTitle())
                    .author(
                            authorService.createAuthorFromDTO(bookWork.getAuthor())
                    )
                    .publicationYear(bookWork.getPublicationYear())
                .build();
    }

}
