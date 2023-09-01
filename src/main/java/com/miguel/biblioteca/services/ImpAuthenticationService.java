package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.*;
import com.miguel.biblioteca.mapper.LibraryMapper;
import com.miguel.biblioteca.mapper.ULibrarianMapper;
import com.miguel.biblioteca.model.*;
import com.miguel.biblioteca.repositories.ILibraryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.miguel.biblioteca.repositories.IULibrarianRepository;
import lombok.AllArgsConstructor;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ImpAuthenticationService implements IAuthenticationService {

    private final ILibraryRepository libraryRepository;

    private final IULibrarianRepository uLibrarianRepository;

    private final IULibrarianService uLibrarianService;

    private final IJWTService jwtService;

    private final LibraryMapper libraryMapper;

    private final AuthenticationManager authenticationManager;

    @Override
    public LibraryDTO SignUpNewLibrary(LibraryRegistrationRequestDTO request) {

        LibraryDTO requestLibraryDTO = request.getLibraryDTO();
        ULibrarianDTO requestLibrarianDTO = request.getULibrarianDTO();

        Library responseLibrary = libraryMapper.mapDtoToEntity(requestLibraryDTO);

        uLibrarianService.signUpLibraryManager(requestLibrarianDTO);

        Optional<ULibrarian> retrievedULibrarian
                = uLibrarianRepository.findByUserEmail(requestLibrarianDTO.getUserEmail());

        List<ULibrarian> libraryCurrentLibrarians = new ArrayList<>();
        libraryCurrentLibrarians.add(retrievedULibrarian.get());
        responseLibrary.setLibrarians(libraryCurrentLibrarians);

        Library savedLibrary = libraryRepository.save(responseLibrary);

        return libraryMapper.mapEntityToDto(savedLibrary);
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {

        String userEmail = request.getUserEmail();
        String password = request.getPassword();
        AuthenticationResponseDTO response = new AuthenticationResponseDTO();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userEmail,
                        password
                )
        );

        Optional<ULibrarian> optionalLibrarian = uLibrarianRepository.findByUserEmail(userEmail);

        if (optionalLibrarian.isPresent()) {
            ULibrarian librarian = optionalLibrarian.get();

            jwtService.revokeAllLibrarianTokens(librarian);

            String accessToken = jwtService.generateAccessToken(librarian);
            String refreshToken = jwtService.generateRefreshToken(librarian);

            jwtService.saveLibrarianToken(librarian, refreshToken);

            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
        } else {
                response.setError("User is not a librarian");
        }
        return response;
    }
}
