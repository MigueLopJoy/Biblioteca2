package com.miguel.library.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
