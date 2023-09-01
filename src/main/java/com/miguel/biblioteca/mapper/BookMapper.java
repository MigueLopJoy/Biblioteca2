package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.AuthorDTO;
import com.miguel.biblioteca.DTO.BookCopyDTO;
import com.miguel.biblioteca.DTO.BookSearchResponseDTO;
import com.miguel.biblioteca.model.*;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class BookMapper {
    
    @Autowired
    private final ModelMapper modelMapper;
    
    @Autowired
    private final AuthorMapper authorMapper;


    public BookCopy mapDtoToEntity(BookCopyDTO bookDto) {

        BookWork bookWork = modelMapper.map(bookDto, BookWork.class);
        BookEdition bookEdition = modelMapper.map(bookDto, BookEdition.class);
        BookCopy bookCopy = modelMapper.map(bookDto, BookCopy.class);

        AuthorDTO authorDTO = bookDto.getAuthorDTO();
        if (authorDTO != null) {
            Author author = authorMapper.mapDtoToEntity(authorDTO);
            bookWork.setAuthor(author);
        }

        if (bookWork != null) {
            bookEdition.setBookWork(bookWork);
        }

        if (bookEdition != null) {
            bookCopy.setBookEdition(bookEdition);
        }

        return bookCopy;
    }

    public BookCopyDTO mapEntityToDto(BookCopy bookCopy) {
        BookCopyDTO bookDTO = modelMapper.map(bookCopy, BookCopyDTO.class);

        BookEdition bookEdition = bookCopy.getBookEdition();
        BookWork bookWork = bookCopy.getBookEdition().getBookWork();

        if (bookEdition != null) {
            if (bookWork != null) {
                Author author = bookWork.getAuthor();
                if (author != null) {
                    AuthorDTO authorDTO = authorMapper.mapEntityToDto(author);
                    bookDTO.setAuthorDTO(authorDTO);
                }
                bookDTO.setTitle(bookWork.getTitle());
                bookDTO.setPublicationYear(bookWork.getPublicationYear());
            }
            bookDTO.setEditor(bookEdition.getEditor());
            bookDTO.setEditionYear(bookEdition.getEditionYear());
        }
        return bookDTO;
    }

    public BookSearchResponseDTO mapSearchResultEntityToDto(BookEdition bookEdition) {
        BookSearchResponseDTO bookSearchResponseDTO = modelMapper.map(bookEdition, BookSearchResponseDTO.class);

        BookWork bookWork = bookEdition.getBookWork();

        if (bookWork != null) {
            Author author = bookWork.getAuthor();
            if (author != null) {
                AuthorDTO authorDTO = authorMapper.mapEntityToDto(author);
                bookSearchResponseDTO.setAuthorDTO(authorDTO);
            }
            bookSearchResponseDTO.setTitle(bookWork.getTitle());
            bookSearchResponseDTO.setPublicationYear(bookWork.getPublicationYear());
        }
        bookSearchResponseDTO.setEditor(bookEdition.getEditor());
        bookSearchResponseDTO.setEditionYear(bookEdition.getEditionYear());

        return bookSearchResponseDTO;
    }

    public List<BookSearchResponseDTO> mapSearchResultEntityListToDtoList(List<BookEdition> books) {
        return books.stream()
                .map(this::mapSearchResultEntityToDto)
                .collect(Collectors.toList());
    }

}
