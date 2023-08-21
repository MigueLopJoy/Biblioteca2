package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.mapper.ULibrarianMapper;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import com.miguel.biblioteca.repositories.IRoleRepository;
import com.miguel.biblioteca.repositories.IULibrarianRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class ImpULibrarianService implements IULibrarianService{

    private final IULibrarianRepository uLibrarianRepository;

    private final IRoleRepository roleRepository;

    private final ULibrarianMapper uLibrarianMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ULibrarianDTO signUpNewLibrarian(ULibrarianDTO uLibrarianDTO) {
        ULibrarian uLibrarian = uLibrarianMapper.mapDtoToEntity(uLibrarianDTO);

        Set<Role> authorities = new HashSet<>();
        Optional<Role> optionalLibrarianRole = roleRepository.findByAuthority("LIBRARIAN");
        if (optionalLibrarianRole.isPresent()) {
            authorities.add(optionalLibrarianRole.get());
        }

        String encodedPassword = passwordEncoder.encode(uLibrarian.getPassword());
        uLibrarian.setPassword(encodedPassword);
        uLibrarian.setAuthorities(authorities);

        Optional<ULibrarian> optionalULibrarianFoundByEmail = uLibrarianRepository.findByUserEmail(uLibrarian.getUserEmail());
        if (optionalULibrarianFoundByEmail.isPresent()) {
            throw new IllegalStateException(String.format("Email '[%s]' alredy taken", uLibrarian.getUserEmail()));
        }

        Optional<ULibrarian> optionalULibrarianFoundByPhoneNumber = uLibrarianRepository.findByUserEmail(uLibrarian.getUserPhoneNumber());
        if (optionalULibrarianFoundByPhoneNumber.isPresent()) {
            ULibrarian savedLibrarian = optionalULibrarianFoundByPhoneNumber.get();
            if (optionalULibrarianFoundByPhoneNumber.isPresent()) {
                throw new IllegalStateException(String.format("Phone number '[%s]' alredy taken", uLibrarian.getUserPhoneNumber()));
            }
        }

        return uLibrarianMapper.mapEntityToDto(uLibrarianRepository.save(uLibrarian));
    }

    @Override
    public ULibrarianDTO signUpLibraryManager(ULibrarianDTO uLibrarianDTO) {
        ULibrarian uLibrarian = uLibrarianMapper.mapDtoToEntity(uLibrarianDTO);

        Set<Role> authorities = new HashSet<>();
        Optional<Role> optionalManagerRole = roleRepository.findByAuthority("LIBRARIAN");
        Optional<Role> optionalLibrarianRole = roleRepository.findByAuthority("MANAGER");

        if (optionalLibrarianRole.isPresent()) {
            authorities.add(optionalLibrarianRole.get());
        }

        if (optionalManagerRole.isPresent()) {
            authorities.add(optionalManagerRole.get());
        }

        String encodedPassword = passwordEncoder.encode(uLibrarian.getPassword());
        uLibrarian.setPassword(encodedPassword);
        uLibrarian.setAuthorities(authorities);

        Optional<ULibrarian> optionalULibrarianFoundByEmail
                = uLibrarianRepository.findByUserEmail(uLibrarian.getUserEmail());
        if (optionalULibrarianFoundByEmail.isPresent()) {
            throw new IllegalStateException(String.format("Email '[%s]' alredy taken", uLibrarian.getUserEmail()));
        }

        Optional<ULibrarian> optionalULibrarianFoundByPhoneNumber
                = uLibrarianRepository.findByUserPhoneNumber(uLibrarian.getUserPhoneNumber());
        if (optionalULibrarianFoundByPhoneNumber.isPresent()) {
            throw new IllegalStateException(String.format("Phone number '[%s]' alredy taken", uLibrarian.getUserPhoneNumber()));
        }

        return uLibrarianMapper.mapEntityToDto(uLibrarianRepository.save(uLibrarian));
    }
}
