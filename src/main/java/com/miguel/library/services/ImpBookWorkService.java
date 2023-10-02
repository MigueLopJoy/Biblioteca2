package com.miguel.library.services;

import com.miguel.library.DTO.BooksEditDTOBookWork;
import com.miguel.library.DTO.BooksSaveDTOBookWork;
import com.miguel.library.Exceptions.*;
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

        if (Objects.isNull(bookWork)) {
            throw new ExceptionNullObject("Book work should not be null");
        }

        Author fetchedAuthor = authorService.searchByAuthorName(bookWork.getAuthor());

        if (Objects.isNull(fetchedAuthor)) {
            throw new ExceptionObjectNotFound("Book's author not found");
        }

        try {
            this.searchByTitleAndAuthor(bookWork);
            throw new ExceptionObjectAlreadyExists("Book work already exists");
        } catch (ExceptionObjectNotFound ex) {

            bookWork.setAuthor(fetchedAuthor);

            return bookWorkRepository.save(bookWork);
        }
    }

    @Override
    public List<BookWork> findAll() {
        List<BookWork> allBookWorks = bookWorkRepository.findAll();
        if (allBookWorks.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No book works were found");
        }
        return allBookWorks;
    }

    @Override
    public BookWork searchByTitleAndAuthor(BookWork bookWork) {

        if (Objects.isNull(bookWork)) {
            throw new ExceptionNullObject("Book work should not be null");
        }
        Author fetchedAuthor = authorService.searchByAuthorName(bookWork.getAuthor());

        if (Objects.isNull(fetchedAuthor)){
            throw new ExceptionObjectNotFound("Book's author not found");
        }

        Optional<BookWork> optionalBookWork
                        = bookWorkRepository.findByTitleAndAuthor(bookWork.getTitle(), fetchedAuthor);


        if (!optionalBookWork.isPresent()) {
            throw new ExceptionObjectNotFound("Searched book work not found");
        }

        return optionalBookWork.get();
    }

    @Override
    public List<BookWork> searchAuthorBookWorks(Author author) {

        if (Objects.isNull(author)) {
            throw new ExceptionNullObject("Author should not be null");
        }

        Author fetchedAuthor = authorService.searchByAuthorName(author);

        if (Objects.isNull(fetchedAuthor)) {
            throw new ExceptionObjectNotFound("Searched author not found");
        }

        return bookWorkRepository.findByAuthor(author);
    }

    @Override
    public BookWork editBookWork(Integer bookWorkId, BooksEditDTOBookWork bookEdit) {
        String title = bookEdit.getTitle();
        Integer publicationYear = bookEdit.getPublicationYear();

        Optional<BookWork> optionalBookWork = bookWorkRepository.findById(bookWorkId);

        if (!optionalBookWork.isPresent()) {
            throw new ExceptionObjectNotFound("Book work not found");
        }

        BookWork savedBookWork = optionalBookWork.get();

        if (Objects.isNull(title) && Objects.isNull(publicationYear)) {
            throw new ExceptionNoInformationProvided("No information provided. Book work cannot be edited.");
        }

        if (Objects.nonNull(title)) {
            savedBookWork.setTitle(title);
        }

        if (Objects.nonNull(publicationYear)) {
            savedBookWork.setPublicationYear(publicationYear);
        }

        return this.saveNewBookWork(savedBookWork);
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
    public BookWork createBookWorkFromBookSaveDTO(BooksSaveDTOBookWork bookWork) {
        return new BookWork().builder()
                    .title(bookWork.getTitle())
                    .author(
                            authorService.createAuthorFromDTO(bookWork.getAuthor())
                    )
                    .publicationYear(bookWork.getPublicationYear())
                .build();
    }

}
