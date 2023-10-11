package com.miguel.library.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "readers")
@AttributeOverride(name = "idUser", column = @Column(name = "id_reader"))
public class UReader extends User{

    @Column(unique = true, nullable = false)
    private String readerNumber;

    private Character gender;

    private LocalDate dateOfBirth;

    public UReader(String firstName,
                   String lastName,
                   String email,
                   String phoneNumber,
                   String readerNumber,
                   LocalDate dateOfBirth,
                   Character gender
    ) {
        super(firstName, lastName, email, phoneNumber);
        this.readerNumber = readerNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}
