package com.miguel.biblioteca.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ULibrarianDTO {
    private String firstName;
    private String lastName;
    private String userPhoneNumber;
    private String userEmail;    
    private String password;  
    private LibraryDTO libraryDTO;
}
