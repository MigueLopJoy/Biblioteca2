package com.miguel.library.services;

import com.miguel.library.DTO.AuthRegisterRequest;
import com.miguel.library.DTO.AuthRequest;
import com.miguel.library.DTO.AuthResponse;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.model.ULibrarian;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ImpAuthenticationService implements IAuthenticationService {

    @Autowired
    private ILibraryService libraryService;

    @Autowired
    private IULibrarianService librarianService;

    @Autowired
    private ITokenService tokenService;

    @Override
    public AuthResponse register(AuthRegisterRequest request) {

        if (Objects.isNull(request)) {
            throw new ExceptionNullObject("Registration Request Should Not Be Null");
        }

        libraryService.saveNewLibrary(
                libraryService.createLibraryFromDTO(
                        request.getLibrary()
                )
        );

        ULibrarian savedLibrarian =
                librarianService.saveNewLibrarian(
                        librarianService.createLibrarianFromDTO(
                                request.getLibrarian()
                        )
                ).getLibrarian();

        String jwtToken = tokenService.generateToken(savedLibrarian);
        String refreshToken = tokenService.generateRefreshToken(savedLibrarian);

        tokenService.saveToken(savedLibrarian, jwtToken);

        return new AuthResponse(
                jwtToken,
                refreshToken
        );
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        return null;
    }
}
