package com.miguel.library.services;

import com.miguel.library.DTO.BookEditRequest;
import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookEdition;
import com.miguel.library.model.BookWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ImpBookEditService implements  IBookEditService{

    @Autowired
    private IBookCopyService bookCopyService;

    @Autowired
    private IBookEditionService bookEditionService;

    @Autowired
    private IBookWorkService bookWorkService;

    @Autowired
    private IAuthorService authorService;


    @Override
    public Object editBooks(BookEditRequest bookEditRequest) {

        Object editedObject = null;

        BookEdition bookEditionToEdit;

        BookWork bookWorkToEdit;


        if (StringUtils.isEmpty(bookEditRequest.getTitle())) {

        }

        if (StringUtils.isEmpty(bookEditRequest.getAuthor())) {

        }

        if (StringUtils.isEmpty(bookEditRequest.getPublicationYear())) {

        }


        if (StringUtils.isEmpty(bookEditRequest.getISBN())) {

        }

        if (StringUtils.isEmpty(bookEditRequest.getEditor())) {

        }

        if (StringUtils.isEmpty(bookEditRequest.getEditionYear())) {

        }


        return null;
    }
}
