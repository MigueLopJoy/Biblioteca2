package com.miguel.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BookWork")
public class BookWork {

   @Id
   @GeneratedValue(strategy=GenerationType.SEQUENCE)
   private Integer idBookWork;

   private String title;

   @NotNull
   @ManyToOne
   @JoinColumn(name = "id_author")   
   private Author author;

   @NotNull
   private Integer publicationYear;
}
