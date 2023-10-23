package com.miguel.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.awt.print.Book;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BookWorks")
public class BookWork implements Comparable<BookWork> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer idBookWork;

    private String title;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private Author author;

    private Integer publicationYear;

    @Override
    public int compareTo(BookWork otherBookWork) {
        int comparisonResult = this.title.compareTo(otherBookWork.getTitle());

        if (comparisonResult == 0) {
            comparisonResult = this.author.getLastName().compareTo(otherBookWork.getAuthor().getLastName());
            if (comparisonResult == 0) {
                comparisonResult = this.author.getFirstName().compareTo(otherBookWork.getAuthor().getFirstName());
            }
        }
        return comparisonResult;
    }
}
