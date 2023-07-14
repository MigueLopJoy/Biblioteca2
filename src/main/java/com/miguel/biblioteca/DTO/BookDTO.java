package com.miguel.biblioteca.DTO;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
   private String bookCode;
   private String title;  
   private AuthorDTO authorDTO;
   private Integer publicationYear;
   private String publisher;
}
