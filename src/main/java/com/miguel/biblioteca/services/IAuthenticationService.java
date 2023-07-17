package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Library;
import com.miguel.biblioteca.model.ULibrarian;

public interface IAuthenticationService {
    public Library SignUpNewLibrary(Library library);
    public ULibrarian signUpLibraryManager(ULibrarian uLibrarian);    
    public ULibrarian signUpNewLibrarian(ULibrarian uLibrarian);
}
