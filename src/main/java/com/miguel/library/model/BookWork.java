package com.miguel.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BookWorks")
public class BookWork {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer idBookWork;

    private String title;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private Author author;

    private Integer publicationYear;
}
