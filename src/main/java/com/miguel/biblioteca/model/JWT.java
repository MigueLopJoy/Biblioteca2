package com.miguel.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

import javax.persistence.Entity;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class JWT {

    @Id
    @GeneratedValue
    public Integer idToken;

    @Column(unique = true,length = 3000)
    public String token;

    @Enumerated(EnumType.STRING)
    public JWTType jwtType = JWTType.BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_librarian")
    public ULibrarian uLibrarian;
}
