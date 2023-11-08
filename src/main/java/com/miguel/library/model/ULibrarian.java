package com.miguel.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Comparator;

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
                      Library library
    ) {
        super(firstName, lastName, gender, birthYear, phoneNumber, email, password);
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
