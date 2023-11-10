package com.miguel.library.services;

import com.miguel.library.DTO.UserDTOChangePasswordRequest;
import com.miguel.library.DTO.UserDTOSaveUser;
import com.miguel.library.DTO.UserDTOUserResponse;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.model.User;
import com.miguel.library.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;

@Service
public class ImpUserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTOUserResponse changeUserPassword(UserDTOChangePasswordRequest passwordRequest, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        return new UserDTOUserResponse(
            "Password changed successfully",
                userRepository.save(user)
        );
    }

    @Override
    public User getConnectedUser(Principal principal) {
        User user = this.searchByEmail(principal.getName());
        if (Objects.isNull(user)) {
            throw new ExceptionNoSearchResultsFound("User not found");
        }
        return user;
    }

    @Override
    public User searchByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User searchByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }
}
