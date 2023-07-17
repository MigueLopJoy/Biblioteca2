package com.miguel.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Readers")
public class UReader extends User{
    private String readerCode;  
    private String gender;
    private LocalDate dateOfBirth;
}
