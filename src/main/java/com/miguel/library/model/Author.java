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
public class Author {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer idAuthor;
    private String firstName;
    private String lastName;
}
