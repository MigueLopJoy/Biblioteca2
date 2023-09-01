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
@Table(name = "BookCopy")
public class BookCopy {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer idBookCopy;

    @NotBlank
    @Column(unique=true)
    private String bookCopyCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_Book_Edition")
    private BookEdition bookEdition;
}
