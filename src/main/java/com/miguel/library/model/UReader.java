package com.miguel.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "readers")
public class UReader extends User implements Comparable<UReader>{

    @Column(unique = true, nullable = false)
    private String readerNumber;

    public UReader(String firstName,
                   String lastName,
                   Character gender,
                   Integer birthYear,
                   String phoneNumber,
                   String email,
                   String password,
                   Role role,
                   String readerNumber
    ) {
        super(firstName, lastName, gender, birthYear, phoneNumber, email, password, role);
        this.readerNumber = readerNumber;
    }

    @Override
    public int compareTo(UReader otherReader) {
        Integer orderResult = this.getFirstName().compareTo(otherReader.getFirstName());

        if (orderResult == 0) {
            orderResult = this.getLastName().compareTo(otherReader.getLastName());
        }
        return orderResult;
    }
}
