package com.miguel.library.services;

import com.miguel.library.DTO.AuthRegisterRequest;
import com.miguel.library.DTO.AuthRegisterResponse;
import com.miguel.library.DTO.AuthRequest;
import com.miguel.library.DTO.AuthResponse;
import com.miguel.library.Exceptions.ExceptionNoSearchResultsFound;
import com.miguel.library.Exceptions.ExceptionNullObject;
import com.miguel.library.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ImpAuthenticationService implements IAuthenticationService {

    @Autowired
    private ILibraryService libraryService;

    @Autowired
    private IULibrarianService librarianService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthRegisterResponse register(AuthRegisterRequest request) {

        if (Objects.isNull(request)) {
            throw new ExceptionNullObject("Registration Request Should Not Be Null");
        }

        Library newLibrary = libraryService.saveNewLibrary(
                libraryService.createLibraryFromDTO(
                        request.getLibrary()
                )
        ).getLibrary();

        ULibrarian savedLibrarian =
                librarianService.saveLibraryManager(
                        librarianService.createLibrarianFromDTO(
                                request.getLibrarian(),
                                newLibrary
                        )
                ).getLibrarian();

        return new AuthRegisterResponse(
                "New Library Account Created Successfully",
                newLibrary,
                savedLibrarian
        );
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userService.searchByEmail(request.getEmail());

        if (Objects.isNull(user)) {
            throw new ExceptionNoSearchResultsFound("User not found");
        }

        String accessToken = tokenService.generateToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);
        tokenService.revokeAllUserTokens(user);
        tokenService.saveToken(
                tokenService.generateUserTokenFromJwtString(accessToken)
        );
        return new AuthResponse(
                accessToken,
                refreshToken
        );
    }
}
