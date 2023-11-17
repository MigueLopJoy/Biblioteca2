package com.miguel.library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "libraries")
public class Library {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id_library")
    private Integer idLibrary;

    @Column(unique = true, nullable = false)
    private String libraryName;

    @Column(unique = true, nullable = false)
    private String libraryPhoneNumber;

    @Column(unique = true, nullable = false)
    private String libraryEmail;

    @Column(nullable = false)
    private String libraryAddress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String postalCode;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="library_librarians_junction",
            joinColumns = {@JoinColumn(name="id_library")},
            inverseJoinColumns = {@JoinColumn(name="id_user")}
    )
    private List<ULibrarian> librarians;

    @OneToMany(mappedBy = "library")
    @JsonManagedReference
    private List<BookCopy> libraryBookCopies;
    public Library(String libraryName,
                   String libraryPhoneNumber,
                   String libraryEmail,
                   String libraryAddress,
                   String city,
                   String province,
                   String postalCode) {
        this.libraryName = libraryName;
        this.libraryPhoneNumber = libraryPhoneNumber;
        this.libraryEmail = libraryEmail;
        this.libraryAddress = libraryAddress;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    public Library(String libraryName,
                   String libraryPhoneNumber,
                   String libraryEmail,
                   String libraryAddress,
                   String city,
                   String province,
                   String postalCode,
                   List<ULibrarian> librarians) {
        this.libraryName = libraryName;
        this.libraryPhoneNumber = libraryPhoneNumber;
        this.libraryEmail = libraryEmail;
        this.libraryAddress = libraryAddress;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.librarians = librarians;
    }
}
