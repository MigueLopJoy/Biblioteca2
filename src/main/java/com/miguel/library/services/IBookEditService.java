package com.miguel.library.services;

import com.miguel.library.DTO.BookEditRequest;

import java.util.List;

public interface IBookEditService {
    public Object editBooks(BookEditRequest bookEditRequest);
}
