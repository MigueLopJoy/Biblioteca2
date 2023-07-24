package com.miguel.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class User implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer idUser;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NonNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private String userPhoneNumber;

    @NonNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private String userEmail;
}
