package com.miguel.library.model;

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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="edition_copies_junction",
            joinColumns = {@JoinColumn(name="id_bookEdition")},
            inverseJoinColumns = {@JoinColumn(name="id_bookCopy")}
    )
    private List<BookCopy> bookEditionCopies;
}
