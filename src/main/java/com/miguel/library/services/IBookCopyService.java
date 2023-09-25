package com.miguel.library.services;

import com.miguel.library.DTO.BooksEditDTOBookCopy;
import com.miguel.library.DTO.BooksSaveDTOBookCopy;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;

import java.util.List;

public interface IBookCopyService {
    public BookCopy saveNewBookCopy(BookCopy bookCopy);

    public BookCopy searchByBarCode(String barCode);

    public BookCopy searchByRegistrationNumber(Long registrationNumber);

    public List<BookCopy> searchBookEditionCopies(BookEdition bookEdition);

    public BookCopy editBookCopy(Integer bookCopyId, BooksEditDTOBookCopy bookEdit);

    public String deleteBookCopy(Integer bookCopyId);

    public BookCopy createBookCopyFromBookSaveDTO(BooksSaveDTOBookCopy bookCopy);

}
