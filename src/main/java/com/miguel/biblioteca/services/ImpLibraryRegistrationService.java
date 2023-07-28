package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.mapper.LibraryMapper;
import com.miguel.biblioteca.mapper.ULibrarianMapper;
import com.miguel.biblioteca.model.Library;
import com.miguel.biblioteca.model.LibraryAddress;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import com.miguel.biblioteca.repositories.ILibraryAddressRepository;
import com.miguel.biblioteca.repositories.ILibraryRepository;
import com.miguel.biblioteca.repositories.IRoleRepository;
import com.miguel.biblioteca.repositories.IULibrarianRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ImpLibraryRegistrationService implements ILibraryRegistrationService{

    private final ILibraryRepository libraryRepository;
    
    private final IULibrarianService uLibrarianService;
    
    private final IRoleRepository roleRepository;

    private final LibraryMapper libraryMapper;

    private final ULibrarianMapper uLibrarianMapper;
    
    @Override
    public LibraryDTO SignUpNewLibrary(LibraryDTO libraryDTO, ULibrarianDTO librarianDTO) {
        Library library = libraryMapper.mapDtoToEntity(libraryDTO);

        Set<Role> authorities = new HashSet<>();
        authorities.add(roleRepository.findByAuthority("LIBRARIAN").get());
        authorities.add(roleRepository.findByAuthority("MANAGER").get());

        ULibrarianDTO libraryManagerDTO = uLibrarianService.signUpNewLibrarian(librarianDTO, authorities);
        ULibrarian libraryManager = uLibrarianMapper.mapDtoToEntity(libraryManagerDTO);

        List<ULibrarian> libraryCurrentLibrarians = new ArrayList<>();
        libraryCurrentLibrarians.add(libraryManager);
        library.setLibrarians(libraryCurrentLibrarians);

        Library savedLibrary = libraryRepository.save(library);
        return libraryMapper.mapEntityToDto(savedLibrary);
    }
}
