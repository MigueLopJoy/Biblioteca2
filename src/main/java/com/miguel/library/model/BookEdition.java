package com.miguel.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "id_Book_Work")
    private BookWork bookWork;
}
