package com.miguel.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BookCopies")
public class BookCopy {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer idBookCopy;

    @Column(unique=true)
    private String barCode;

    @Column(unique = true)
    private Long registrationNumber;

    private LocalDate registrationDate;

    private String signature;

    @ManyToOne
    @JoinColumn(name = "id_book_edition")
    @JsonIgnoreProperties("bookEditionCopies")
    private BookEdition bookEdition;

    @ManyToOne
    @JoinColumn(name="id_library")
    @JsonIgnoreProperties("libraryBookCopies")
    private Library library;
}
