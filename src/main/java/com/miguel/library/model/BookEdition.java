package com.miguel.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BookEditions")
public class BookEdition {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer idBookEdition;

    @Column(unique=true)
    private String ISBN;

    private String editor;

    private Integer editionYear;

    private String language;

    @ManyToOne
    @JoinColumn(name = "id_Book_Work")
    private BookWork bookWork;

    @OneToMany(mappedBy = "bookEdition")
    @JsonManagedReference
    private List<BookCopy> bookEditionCopies;
}
