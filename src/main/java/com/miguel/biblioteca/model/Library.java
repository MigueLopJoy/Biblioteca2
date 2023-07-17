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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Libraries")
public class Library {    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer idLibrary;
    private String libraryName;
    @OneToOne
    @JoinColumn(name = "address_id")
    private LibraryAddress libraryAddress;
    
    @OneToMany(mappedBy = "library")
    private List<ULibrarian> librarians;
}
