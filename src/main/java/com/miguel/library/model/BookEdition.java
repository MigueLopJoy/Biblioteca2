package com.miguel.library.model;

import jakarta.persistence.*;
import lombok.*;

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
}
