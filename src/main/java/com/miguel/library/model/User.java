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

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;
}
