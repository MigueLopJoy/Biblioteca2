package com.miguel.biblioteca.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter @Setter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "readers")
@AttributeOverride(name = "idUser", column = @Column(name = "id_reader"))
public class UReader extends User{

    @NotBlank
    @Column(unique = true)
    private String readerCode;

    @NonNull
    private Character gender;

    @NonNull
    private LocalDate dateOfBirth;

    public UReader(
            Integer id,
            String firstName,
            String lastName,
            String userPhoneNumber,
            String userEmail,
            String readerCode,
            Character gender,
            LocalDate dateOfBirth
    ) {
        super(id, firstName, lastName, userPhoneNumber, userEmail);
        this.readerCode = readerCode;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public UReader(
            String firstName,
            String lastName,
            String userPhoneNumber,
            String userEmail,
            String readerCode,
            Character gender,
            LocalDate dateOfBirth
    ) {
        super(firstName, lastName, userPhoneNumber, userEmail);
        this.readerCode = readerCode;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}
