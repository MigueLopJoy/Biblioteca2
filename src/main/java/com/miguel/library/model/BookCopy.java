package com.miguel.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
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
    @JoinColumn(name = "id_Book_Edition")
    private BookEdition bookEdition;

    @NotNull
    @ManyToOne
    @JoinColumn(name="id_library", nullable=false)
    private Library library;
}
