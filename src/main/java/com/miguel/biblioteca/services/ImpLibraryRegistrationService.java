package com.miguel.biblioteca.services;

import com.miguel.biblioteca.DTO.LibraryDTO;
import com.miguel.biblioteca.mapper.LibraryMapper;
import com.miguel.biblioteca.model.Library;
import com.miguel.biblioteca.model.LibraryAddress;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import com.miguel.biblioteca.repositories.ILibraryAddressRepository;
import com.miguel.biblioteca.repositories.ILibraryRepository;
import com.miguel.biblioteca.repositories.IRoleRepository;
import com.miguel.biblioteca.repositories.IULibrarianRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ImpLibraryRegistrationService implements ILibraryRegistrationService{

    @Autowired
    private ILibraryRepository libraryRepository;
    
    @Autowired
    private ILibraryAddressRepository libraryAddressRepository;
    
    @Autowired
    private IULibrarianRepository uLibrarianRepository;
    
    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public LibraryDTO SignUpNewLibrary(LibraryDTO libraryDTO) {

        Library library = libraryMapper.mapDtoToEntity(libraryDTO);

        LibraryAddress libraryAddress = libraryAddressRepository.save(library.getLibraryAddress());
        library.setLibraryAddress(libraryAddress);

        ULibrarian libraryManager = signUpLibraryManager(library.getLibraryManager());
        library.setLibraryManager(libraryManager);

        return libraryMapper.mapEntityToDto(libraryRepository.save(library));
    }

    @Override
    public ULibrarian signUpLibraryManager(ULibrarian uLibrarian) {
        String encodedPassword = passwordEncoder.encode(uLibrarian.getPassword());
        uLibrarian.setPassword(encodedPassword);
        
        Set<Role> authorities = new HashSet<>();
        authorities.add(roleRepository.findByAuthority("LIBRARIAN").get());
        authorities.add(roleRepository.findByAuthority("MANAGER").get());
        uLibrarian.setAuthorities(authorities);

        return uLibrarianRepository.save(uLibrarian);
    }
}
