package com.miguel.library.DTO;

import com.miguel.library.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOUserResponse {
    private String successMessage;
    private User user;
}
