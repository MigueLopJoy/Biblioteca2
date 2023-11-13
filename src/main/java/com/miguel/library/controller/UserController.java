package com.miguel.library.controller;

import com.miguel.library.DTO.UserDTOChangePasswordRequest;
import com.miguel.library.DTO.UserDTOUserResponse;
import com.miguel.library.services.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private LogoutHandler logoutHandler;

    @PostMapping("/change-password")
    public ResponseEntity<UserDTOUserResponse> changePassword(
            @Valid @RequestBody UserDTOChangePasswordRequest passwordRequest,
            Principal connectedUser
    ) {
        return ResponseEntity.ok(userService.changeUserPassword(passwordRequest, connectedUser));
    }

    @PostMapping("/get-connected-user")
    public ResponseEntity<?> getConnectedUser(
            Principal connectedUser
    ) {
        return ResponseEntity.ok(userService.getConnectedUser(connectedUser));
    }
}
