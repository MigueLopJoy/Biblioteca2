package com.miguel.library.services;

import com.miguel.library.DTO.BookSearchRequestBookWork;

import java.util.List;

public interface IBookSearchService {
    public List<Object> searchBooks(BookSearchRequestBookWork bookSearchRequest);
}
