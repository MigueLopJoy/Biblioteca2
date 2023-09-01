package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.BookEdition;

public interface IBookEditionService {
    public BookEdition saveNewBookEdition(BookEdition bookEdition);

    public BookEdition searchByEditorAndEditionYear(String edition, Integer editionYear);
}
