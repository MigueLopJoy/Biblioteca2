package com.miguel.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@MappedSuperclass
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer idUser;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(unique = true)
    private String phoneNumber;

    @NotBlank
    @Column(unique = true)
    private String email;

    public User(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
