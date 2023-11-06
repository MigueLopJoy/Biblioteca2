package com.miguel.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "librarians")
public class ULibrarian extends User implements Comparator<ULibrarian> {

    @NotNull
    @ManyToOne
    @JoinColumn(name="id_library", nullable=false)
    private Library library;

    public ULibrarian(String firstName,
                      String lastName,
                      Character gender,
                      Integer birthYear,
                      String phoneNumber,
                      String email,
                      String password,
                      Role role,
                      Library library
    ) {
        super(firstName, lastName, gender, birthYear, phoneNumber, email, password, role);
        this.library = library;
    }

    @Override
    public int compare(ULibrarian thisLibrarian, ULibrarian otherLibrarian) {
        Integer orderResult = this.getFirstName().compareTo(otherLibrarian.getFirstName());

        if (orderResult == 0) {
                orderResult = this.getLastName().compareTo(otherLibrarian.getLastName());
        }
        return orderResult;
    }
}
