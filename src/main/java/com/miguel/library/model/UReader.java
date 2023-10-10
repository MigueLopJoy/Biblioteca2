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
public class UReader {
    @NotBlank
    @Column(unique = true)
    private String readerCode;

    @NonNull
    private Character gender;

    @NonNull
    private LocalDate dateOfBirth;
}
