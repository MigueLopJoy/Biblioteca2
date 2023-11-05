package com.miguel.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Authors")
public class Author implements Comparable<Author> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer idAuthor;
    private String firstName;
    private String lastName;

    @Override
    public int compareTo(Author otherAuthor) {
        Integer orderResult = this.firstName.compareTo(otherAuthor.getFirstName());

        if (orderResult == 0) {
            orderResult = this.lastName.compareTo(otherAuthor.getLastName());
        }
        return orderResult;
    }
}
