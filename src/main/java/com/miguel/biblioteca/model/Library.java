package com.miguel.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "libraries")
public class Library {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer idLibrary;

    @NonNull
    private String libraryName;

    @NonNull
    @OneToOne
    @JoinColumn(name = "id_library_address")
    private LibraryAddress libraryAddress;

    @NonNull
    @OneToOne
    @JoinColumn(name = "id_librarian")
    private ULibrarian libraryManager;

    @NonNull
    @OneToMany
    private List<ULibrarian> librarians;
}
