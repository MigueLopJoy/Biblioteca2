package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.BookDTO;
import com.miguel.biblioteca.model.Book;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

public class BookMapper {
    
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Book mapDtoToEntity(BookDTO bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
    
    public static BookDTO mapEntityToDto(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }
    
    public static List<BookDTO> mapEntityListToDtoList(List<Book> books) {
        return books.stream()
                .map(BookMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
