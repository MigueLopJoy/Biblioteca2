package com.miguel.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "Users")
public abstract class User {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer idUser;    
    private String firstName;
    private String lastName;
    @Column(unique=true)    
    private String userPhoneNumber;
    @Column(unique=true)    
    private String userEmail;
}
