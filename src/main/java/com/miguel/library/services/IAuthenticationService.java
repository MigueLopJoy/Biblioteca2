package com.miguel.library.services;

import com.miguel.library.DTO.AuthRegisterRequest;
import com.miguel.library.DTO.AuthRegisterResponse;
import com.miguel.library.DTO.AuthRequest;
import com.miguel.library.DTO.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IAuthenticationService {

    public AuthRegisterResponse register(AuthRegisterRequest request);

    public AuthResponse authenticate(AuthRequest request);

    public AuthResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
