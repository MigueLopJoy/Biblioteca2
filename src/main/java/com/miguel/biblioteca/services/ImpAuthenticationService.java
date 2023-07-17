package com.miguel.biblioteca.services;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ImpAuthenticationService implements IAuthenticationService{

    @Autowired
    private ILibraryRepository libraryRepository;
    
    @Autowired
    private ILibraryAddressRepository libraryAddressRepository;
    
    @Autowired
    private IULibrarianRepository uLibrarianRepository;
    
    @Autowired
    private IRoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Library SignUpNewLibrary(Library library) {
        LibraryAddress libraryAddress = libraryAddressRepository.save(library.getLibraryAddress());
        library.setLibraryAddress(libraryAddress);
        
        ULibrarian libraryManager = signUpLibraryManager(library.getLibrarians().get(0));
        List<ULibrarian> currentLibrarians = new ArrayList<>();
        currentLibrarians.add(libraryManager);
        library.setLibrarians(currentLibrarians);
        
        return libraryRepository.save(library);
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

    @Override
    public ULibrarian signUpNewLibrarian(ULibrarian uLibrarian) {
        String encodedPassword = passwordEncoder.encode(uLibrarian.getPassword());
        uLibrarian.setPassword(encodedPassword);
        
        Set<Role> authorities = new HashSet<>();
        authorities.add(roleRepository.findByAuthority("LIBRARIAN").get());
        uLibrarian.setAuthorities(authorities);

        return uLibrarianRepository.save(uLibrarian);        
    }    
}
