package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.LibraryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILibraryAddressRepository extends JpaRepository<LibraryAddress, Integer>{    
}
