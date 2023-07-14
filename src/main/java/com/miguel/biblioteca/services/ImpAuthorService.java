package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.repositories.IAuthorRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImpAuthorService implements IAuthorService{

    @Autowired
    private IAuthorRepository authorRepository;
    
    @Override
    public Optional<Author> findByAuthorName(String authorName) {
        return authorRepository.findByAuthorName(authorName);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return authorRepository.save(author);
    }
    
    @Override
    public Author getOrCreateAuthor(Author author) {
        Author searchedAuthor = authorRepository.findByAuthorName(this.getFullAuthorName(author)).orElse(null);
        
        if (searchedAuthor == null) {
            return authorRepository.save(author);
        }           
        return searchedAuthor;
    }
    
    @Override
    public String getFullAuthorName(Author author) {
        String authorFirstName = author.getFirstName() != null ? author.getFirstName() : "";
        String authorLastName = author.getLastNames() != null ? author.getLastNames() : "";
        return (authorFirstName + ' ' + authorLastName).trim();
    }


    
    /**
     * Valida una cadena de texto contra un formato de nombre establecido por una expresion regular
     *
     * @param name cadena de texto que quiere ser valdiada contra el formato de nombre establecido
     * @return booleano que indica si la cadena de texto es un nombre valido o no
     */
    @Override    
    public boolean validateName(String name) {
        boolean validName = false;

        // Admite varias palabras separadas por espacios en las que solo haya letras de la 'a' a la 'z' en mayuscula
        // y minuscla, asi como vocales acentuadas
        if (name.matches("^[a-zA-ZÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$")) {
            validName = true;
        } else {
            System.out.println("Nombre no valido");
        }
        return validName;
    }
    
    /**
     * Utilizado para la peticion de nombres y apellidos, recibe una cadena de texto y la adecua al
     * formato estandar de dichos datos: primera mayuscula y el resto minuscula
     *
     * @param name Texto a modificar convirtiendo a mayuscula la primera letra de cada palabra de la cadena
     * @return cadena de texto con la primera letra de cada palabra convertida a mayuscula
     */
    @Override    
    public String normalizeName(String name) {
        String[] palabrasTexto;
        String textoMayusculas = "";

        // Comprueba que el texto recibido es un nombre valido
        if (validateName(name)) {
            // Llena el array 'palabrasTexto' con los elementos de la cadena pasada por parametro obtenidos de aplicar
            // el separador " " (espacio en blanco) a la misma
            palabrasTexto = name.split(" ");
            for (String palabraTexto : palabrasTexto) {
                // Comprueba que el primer caracter cada elemento del array es una letra
                if (palabraTexto.length() > 0 && Character.isLetter(palabraTexto.charAt(0))) {
                    // Se suma a 'textoMayusculas' la palabra con la primera letra convertida a mayuscula mas un espacio en blanco
                    textoMayusculas += (palabraTexto.toUpperCase().charAt(0) + (palabraTexto.substring(1, palabraTexto.length()).toLowerCase()) + " ");
                }
            }
            // Eliminar espacios en blanco sobrantes
            textoMayusculas = textoMayusculas.trim();
        }
        return textoMayusculas;
    }
}
