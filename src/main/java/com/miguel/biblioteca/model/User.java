package com.miguel.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer idUser;    
    private String firstName;
    private String lastName;
    @Column(unique=true)    
    private String userPhoneNumber;
    @Column(unique=true)    
    private String userEmail;
    private String userPassword;

}
