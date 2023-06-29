package com.miguel.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer idAuthor;
    private String firstName;
    private String lastNames;
    
    @OneToMany(mappedBy = "author")
    private List<Book> books;
}
