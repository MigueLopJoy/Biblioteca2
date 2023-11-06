package com.miguel.library.DTO;

import com.miguel.library.model.UReader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOReaderResponse {
    private String successMessage;
    private UReader reader;
}
