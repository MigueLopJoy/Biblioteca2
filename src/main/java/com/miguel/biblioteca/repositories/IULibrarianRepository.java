package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.ULibrarian;
import org.springframework.stereotype.Repository;

@Repository
public interface IULibrarianRepository extends IUserRepository{
    public ULibrarian findByUserEmail(String userEmail);
}
