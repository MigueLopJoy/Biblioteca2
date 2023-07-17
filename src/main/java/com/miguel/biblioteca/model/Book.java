package com.miguel.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Books")
public class Book {
   @Id
   @GeneratedValue(strategy=GenerationType.SEQUENCE)
   private Integer idBook;
   @Column(unique=true)
   private String bookCode;
   private String title;
   
   @ManyToOne
   @JoinColumn(name = "id_author")   
   private Author author;
   private Integer publicationYear;
   private String publisher;
}
