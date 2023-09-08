package com.miguel.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    private Date registrationDate;

    private String signature;

    @ManyToOne
    @JoinColumn(name = "id_Book_Edition")
    private BookEdition bookEdition;
}
