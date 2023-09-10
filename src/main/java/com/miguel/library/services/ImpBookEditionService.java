package com.miguel.library.services;

import com.miguel.library.DTO.BookEditBookEdition;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import com.miguel.library.repository.IBookEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
                BookWork savedBookWork = bookWorkService.searchByTitleAndAuthor(bookWork);

                if (savedBookWork != null) {
                    BookEdition bookEditionWithISBN
                            = this.searchByISBN(bookEdition.getISBN());

                    if (bookEditionWithISBN == null) {
                        bookEdition.setBookWork(savedBookWork);
                        savedBookEdition = bookEditionRepository.save(bookEdition);
                    }
                }
            }
        } else {
            throw new RuntimeException("Book Edition information not provided");
        }
        return savedBookEdition;
    }

    @Override
    public BookEdition searchByISBN(String ISBN) {
        return bookEditionRepository.findByISBN(ISBN).orElse(null);
    }

    @Override
    public List<BookEdition> searchBookWorkEditions(BookWork bookWork) {
        List<BookEdition> bookWorkEditions = new ArrayList<>();

        if (bookWork != null) {
            BookWork fetchedBookWork = this.fetchBookWork(bookWork);
            if (fetchedBookWork != null) {
                bookWorkEditions.addAll(bookEditionRepository.findByBookWork(fetchedBookWork));
            }
        }
        return bookWorkEditions;
    }

    @Override
    public BookEdition editBookEdition(Integer bookEditionId, BookEditBookEdition bookEdit) {

        BookEdition editedBookEdition = null;
        String isbn = bookEdit.getISBN();
        String editor = bookEdit.getEditor();
        Integer editionYear = bookEdit.getEditionYear();
        String language = bookEdit.getLanguage();

        Optional<BookEdition> optionalBookEdition = bookEditionRepository.findById(bookEditionId);

        if (optionalBookEdition.isPresent()) {
            BookEdition savedBookEdition = optionalBookEdition.get();

            if (!StringUtils.isEmpty(isbn) && !isbn.trim().isBlank()) {
                savedBookEdition.setISBN(isbn);
            }

            if (!StringUtils.isEmpty(editor) && !editor.trim().isBlank()) {
                savedBookEdition.setEditor(editor);
            }

            if (editionYear != null) {
                savedBookEdition.setEditionYear(editionYear);
            }

            if (!StringUtils.isEmpty(language) && !language.trim().isBlank()) {
                savedBookEdition.setLanguage(language);
            }

            editedBookEdition = this.saveNewBookEdition(savedBookEdition);
        }

        return editedBookEdition;
    }

    @Override
    public void deleteBookEdition(Integer bookEditionId) {
        Optional<BookEdition> optionalBookEdition = bookEditionRepository.findById(bookEditionId);

        if (optionalBookEdition.isPresent()) {
            bookEditionRepository.deleteById(bookEditionId);
        }
    }

    private BookWork fetchBookWork(BookWork bookWork) {
        return bookWorkService.searchByTitleAndAuthor(bookWork);
    }
}
