package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.mapper.ULibrarianMapper;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import com.miguel.biblioteca.repositories.IULibrarianRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class ImpULibrarianService implements IULibrarianService{

    private final IULibrarianRepository uLibrarianRepository;

    private final ULibrarianMapper uLibrarianMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ULibrarianDTO signUpNewLibrarian(ULibrarianDTO uLibrarianDTO, Set<Role> authorities) {
        ULibrarian uLibrarian = uLibrarianMapper.mapDtoToEntity(uLibrarianDTO);

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
}
