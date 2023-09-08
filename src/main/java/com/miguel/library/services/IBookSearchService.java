package com.miguel.library.services;

import com.miguel.library.DTO.BookEditRequest;
import com.miguel.library.DTO.BookSearchRequest;

import java.util.List;

public interface IBookSearchService {
    public List<Object> searchBooks(BookSearchRequest bookSearchRequest);
}
