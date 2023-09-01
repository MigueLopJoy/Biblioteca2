package com.miguel.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "Authors")
public class Author {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer idAuthor;
    private String firstName;
    private String lastName;
    
    @OneToMany(mappedBy = "author")
    private List<BookWork> books;
}
