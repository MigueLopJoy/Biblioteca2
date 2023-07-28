package com.miguel.biblioteca.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@AllArgsConstructor
@Getter @Setter
public class ULibrarianDTO {

    private String firstName;

    private String lastName;

    private String userPhoneNumber;

    private String userEmail;

    private String password;

    private Set<RoleDTO> authoritiesDTO;

    public ULibrarianDTO(String firstName,
                         String lastName,
                         String userPhoneNumber,
                         String userEmail,
                         String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.password = password;
    }
}
