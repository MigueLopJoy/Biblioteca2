package com.miguel.library.services;

import com.miguel.library.DTO.AuthRegisterRequest;
import com.miguel.library.DTO.AuthRegisterResponse;
import com.miguel.library.DTO.AuthRequest;
import com.miguel.library.DTO.AuthResponse;

public interface IAuthenticationService {

    public AuthRegisterResponse register(AuthRegisterRequest request);

    public AuthResponse authenticate(AuthRequest request);
}
