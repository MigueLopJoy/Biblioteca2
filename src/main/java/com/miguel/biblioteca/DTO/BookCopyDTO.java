package com.miguel.biblioteca.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCopyDTO {

   private String bookCopyCodeDTO;

   private String title;
   private AuthorDTO authorDTO;
   private Integer publicationYear;

   private String editor;
   private Integer editionYear;

   public BookCopyDTO(
           String title,
           AuthorDTO authorDTO,
           Integer publicationYear,
           String editor,
           Integer editionYear
   ) {
      this.title = title;
      this.authorDTO = authorDTO;
      this.publicationYear = publicationYear;
      this.editor = editor;
      this.editionYear = editionYear;
   }
}
