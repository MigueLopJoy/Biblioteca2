package com.miguel.library.services;

import com.miguel.library.DTO.BookResponseDTOBookCopy;
import com.miguel.library.DTO.BooksEditDTOBookCopy;
import com.miguel.library.DTO.BooksSaveDTOBookCopy;
import com.miguel.library.DTO.SuccessfulObjectDeletionDTO;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;

import java.util.List;

public interface IBookCopyService {
    public BookResponseDTOBookCopy saveNewBookCopy(BookCopy bookCopy);

    public List<BookCopy> findAll();

    public BookCopy searchByBarCode(String barCode);

    public BookCopy searchByRegistrationNumber(Long registrationNumber);

    public List<BookCopy> searchBookEditionCopies(Integer bookEditionId);

    public BookResponseDTOBookCopy editBookCopy(Integer bookCopyId, BooksEditDTOBookCopy bookEdit);

    public SuccessfulObjectDeletionDTO deleteBookCopy(Integer bookCopyId);

    public BookCopy createBookCopyFromBookSaveDTO(BooksSaveDTOBookCopy bookCopy);

}
