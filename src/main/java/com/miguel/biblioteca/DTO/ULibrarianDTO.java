package com.miguel.biblioteca.DTO;

import com.miguel.biblioteca.model.JWT;
import com.miguel.biblioteca.model.Role;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ULibrarianDTO {

    private String firstName;

    private String lastName;

    private String userPhoneNumber;

    private String userEmail;

    private String password;

    private Set<RoleDTO> authoritiesDTO;

    private List<JWT> tokens;

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

    public ULibrarianDTO(String firstName,
                         String lastName,
                         String userPhoneNumber,
                         String userEmail,
                         String password,
                         Set<RoleDTO> authoritiesDTO) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.password = password;
        this.authoritiesDTO = authoritiesDTO;
    }
}
