package com.miguel.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BookEdition")
public class BookEdition {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer idBookEdition;

    @NotBlank
    private String editor;

    @NotNull
    private Integer editionYear;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_Book_Work")
    private BookWork bookWork;
}
