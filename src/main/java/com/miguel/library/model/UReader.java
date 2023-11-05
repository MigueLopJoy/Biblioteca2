package com.miguel.library.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "readers")
@AttributeOverride(name = "idUser", column = @Column(name = "idReader"))
public class UReader extends User implements Comparable<UReader>{

    @Column(unique = true, nullable = false)
    private String readerNumber;

    @Column(nullable = false)
    private Character gender;

    @Column(nullable = false)
    private Integer birthYear;

    public UReader(String firstName,
                   String lastName,
                   String email,
                   String phoneNumber,
                   String readerNumber,
                   Integer birthYear,
                   Character gender
    ) {
        super(firstName, lastName, email, phoneNumber);
        this.readerNumber = readerNumber;
        this.gender = gender;
        this.birthYear = birthYear;
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
