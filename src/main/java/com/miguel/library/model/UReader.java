package com.miguel.library.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "readers")
@AttributeOverride(name = "idUser", column = @Column(name = "idReader"))
public class UReader extends User{

    @Column(unique = true, nullable = false)
    private String readerNumber;

    @Column(nullable = false)
    private Character gender;

    @Column(nullable = false)
    private Integer yearOfBirth;

    public UReader(String firstName,
                   String lastName,
                   String email,
                   String phoneNumber,
                   String readerNumber,
                   Integer yearOfBirth,
                   Character gender
    ) {
        super(firstName, lastName, email, phoneNumber);
        this.readerNumber = readerNumber;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
    }
}
