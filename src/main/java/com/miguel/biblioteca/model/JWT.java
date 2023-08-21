package com.miguel.biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JWT {

    @Id
    @GeneratedValue
    public Integer idToken;

    @Column(unique = true,length = 3000)
    public String token;

    @Enumerated(EnumType.STRING)
    public JWTType JWTType = com.miguel.biblioteca.model.JWTType.BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_librarian")
    public ULibrarian uLibrarian;
}
