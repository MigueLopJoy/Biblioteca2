package com.miguel.library.services;

import com.miguel.library.DTO.UserDTOChangePasswordRequest;
import com.miguel.library.DTO.UserDTOSaveUser;
import com.miguel.library.DTO.UserDTOUserResponse;
import com.miguel.library.model.UReader;
import com.miguel.library.model.User;

import java.security.Principal;

public interface IUserService {

    public UserDTOUserResponse changeUserPassword(UserDTOChangePasswordRequest passwordRequest, Principal connectedUser);

    public User getConnectedUser(Principal principal);

    public User searchByEmail(String email);
    public User searchByPhoneNumber(String phoneNumber);
}
