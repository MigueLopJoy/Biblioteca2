package com.miguel.library.DTO;

import com.miguel.library.Validations.UniqueReaderNumber;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@UniqueReaderNumber
public class UserDTOEditReader extends UserDTOEditUser {

    @Pattern(regexp = "^L\\d{8}$", message = "Must provide a valid reader number")
    private String readerNumber;
}
