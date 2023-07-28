package com.miguel.biblioteca.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "libraries")
public class Library {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer idLibrary;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String libraryName;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String libraryAddress;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String city;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String province;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String postalCode;

    @NotEmpty
    @Column(nullable = false)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="library_librarians_junction",
            joinColumns = {@JoinColumn(name="id_library")},
            inverseJoinColumns = {@JoinColumn(name="id_librarian")}
    )
    private List<ULibrarian> librarians;

    public Library(String libraryName, String libraryAddress, String city, String province, String postalCode) {
        this.libraryName = libraryName;
        this.libraryAddress = libraryAddress;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.librarians = new ArrayList<>();
    }
}
