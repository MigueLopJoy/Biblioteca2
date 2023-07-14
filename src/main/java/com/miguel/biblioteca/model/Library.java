package com.miguel.biblioteca.model;

import jakarta.persistence.Entity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Library {
    private String libraryName;
    private LibraryAddress libraryAddress;
    private List<Librarian> librarians;
}
