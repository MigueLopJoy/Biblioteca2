package com.miguel.library.controller;

import com.miguel.library.DTO.AuthRegisterRequest;
import com.miguel.library.DTO.AuthRegisterResponse;
import com.miguel.library.DTO.AuthRequest;
import com.miguel.library.DTO.AuthResponse;
import com.miguel.library.services.IAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthRegisterResponse> register(
            @Valid @RequestBody AuthRegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}