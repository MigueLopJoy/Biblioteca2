package com.miguel.library.services;

import com.miguel.library.DTO.BookResponseDTOBookEdition;
import com.miguel.library.DTO.BooksEditDTOBookEdition;
import com.miguel.library.DTO.BooksSaveDTOBookEdition;
import com.miguel.library.DTO.SuccessfulObjectDeletionDTO;
import com.miguel.library.Exceptions.*;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImpBookEditionService implements IBookEditionService{

    @Autowired
    private IBookEditionRepository bookEditionRepository;

    @Autowired
    private IBookWorkService bookWorkService;

    @Autowired
    private IBookCopyService bookCopyService;

    @Override
    public BookResponseDTOBookEdition saveNewBookEdition(BookEdition bookEdition) {
        if (Objects.isNull(bookEdition)) {
            throw new ExceptionNullObject("Book edition should not be null");
        }

        BookWork bookWork = bookEdition.getBookWork();

        if (Objects.isNull(bookWork)) {
            throw new ExceptionNullObject("Book edition's book work should not be null");
        }

        BookWork savedBookWork = bookWorkService.searchByTitleAndAuthor(bookWork);

        if (Objects.isNull(savedBookWork)) {
            throw new ExceptionObjectNotFound("Book edition's book work not found");
        }

        BookEdition bookEditionWithISBN
                = this.searchByISBN(bookEdition.getISBN());

        if (Objects.nonNull(bookEditionWithISBN)) {
            throw new ExceptionObjectAlreadyExists("Book edition already exists");
        }

        bookEdition.setBookWork(savedBookWork);

        return new BookResponseDTOBookEdition(
                "New Edition Created Successfully",
                bookEditionRepository.save(bookEdition)
        );
    }

    @Override
    public List<BookEdition> findAll() {
        List<BookEdition> allBookEditions = bookEditionRepository.findAll();
        if (allBookEditions.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No book editions were found");
        }
        return allBookEditions;
    }

    public BookEdition searchById(Integer bookEditionId) {
        return bookEditionRepository.findById(bookEditionId).orElse(null);
    }
    @Override
    public BookEdition searchByISBN(String ISBN) {
        return bookEditionRepository.findByISBN(ISBN).orElse(null);
    }

    @Override
    public List<BookEdition> searchBookWorkEditions(Integer bookWorkId) {
        if (Objects.isNull(bookWorkId)) {
            throw new ExceptionObjectNotFound("Book Work Should Not Be Null");
        }

        BookWork bookWork = bookWorkService.searchByBookWorkId(bookWorkId);
        if (Objects.isNull(bookWork)) {
            throw new ExceptionObjectNotFound("Book Work Not Found");
        }

        List<BookEdition> bookWorkEditions = bookEditionRepository.findByBookWork(bookWork);
        if (bookWorkEditions.isEmpty()) {
            throw new ExceptionNoSearchResultsFound("No Editions Were Found");
        }
        return bookWorkEditions;
    }

    @Override
    public BookResponseDTOBookEdition editBookEdition(Integer bookEditionId, BooksEditDTOBookEdition bookEdit) {
        String isbn = bookEdit.getISBN();
        String editor = bookEdit.getEditor();
        Integer editionYear = bookEdit.getEditionYear();
        String language = bookEdit.getLanguage();

        Optional<BookEdition> optionalBookEdition = bookEditionRepository.findById(bookEditionId);

        if (!optionalBookEdition.isPresent()) {
            throw new ExceptionObjectNotFound("Book edition not found");
        }

        BookEdition savedBookEdition = optionalBookEdition.get();

        if (!StringUtils.isEmpty(isbn) && !isbn.trim().isBlank()) {
            savedBookEdition.setISBN(isbn);
        }

        if (!StringUtils.isEmpty(editor) && !editor.trim().isBlank()) {
            savedBookEdition.setEditor(editor);
        }

        if (Objects.nonNull(editionYear)) {
            savedBookEdition.setEditionYear(editionYear);
        }

        if (!StringUtils.isEmpty(language) && !language.trim().isBlank()) {
            savedBookEdition.setLanguage(language);
        }

        return new BookResponseDTOBookEdition(
                "Book Edition Edited Successfully",
                bookEditionRepository.save(savedBookEdition)
        );
    }

    @Override
    public SuccessfulObjectDeletionDTO deleteBookEdition(Integer bookEditionId) {

        BookEdition bookEdition = this.searchById(bookEditionId);
        if (Objects.isNull(bookEdition)) {
            throw new ExceptionObjectNotFound("Book Edition Not Found");
        }

        try{
            bookCopyService.searchBookEditionCopies(
                    bookEdition.getIdBookEdition()
            );
            throw new ExceptionHasRelatedObjects("Cannot Delete Book Edition While Associated Copies Exist");
        } catch (ExceptionNoSearchResultsFound ex) {
            bookEditionRepository.deleteById(bookEditionId);
            return new SuccessfulObjectDeletionDTO("Book Edition Deleted SuccessFully");
        }
    }

    @Override
    public BookEdition createBookEditionFromBookSaveDTO(BooksSaveDTOBookEdition bookEdition) {
        return BookEdition.builder()
                    .ISBN(bookEdition.getISBN())
                    .editor(bookEdition.getEditor())
                    .language(bookEdition.getLanguage())
                    .editionYear(bookEdition.getEditionYear())
                    .bookWork(
                            bookWorkService.createBookWorkFromDTO(
                                    bookEdition.getBookWork()
                            )
                    )
                .build();
    }
}
