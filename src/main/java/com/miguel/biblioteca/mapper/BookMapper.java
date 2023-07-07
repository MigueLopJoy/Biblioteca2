package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.AuthorDTO;
import com.miguel.biblioteca.DTO.BookDTO;
import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Book;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    
    private final ModelMapper modelMapper;
    
    private final AuthorMapper authorMapper;
    
    @Autowired
    public BookMapper(ModelMapper modelMapper, AuthorMapper authorMapper) {
        this.modelMapper = modelMapper;
        this.authorMapper = authorMapper;
    }

    public Book mapDtoToEntity(BookDTO bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);        
        AuthorDTO authorDTO = bookDto.getAuthorDTO();        
        
        if (authorDTO != null) {
            Author author = authorMapper.mapDtoToEntity(authorDTO);
            book.setAuthor(author);
        }       
        return book;
    }
    
    public BookDTO mapEntityToDto(Book book) {
        BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
        Author author = book.getAuthor();
        
        if (author != null) {
            AuthorDTO authorDTO = authorMapper.mapEntityToDto(author);
            bookDTO.setAuthorDTO(authorDTO);
        }        
        return bookDTO;
    }
    
    public List<BookDTO> mapEntityListToDtoList(List<Book> books) {
        return books.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
