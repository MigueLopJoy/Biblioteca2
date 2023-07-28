package com.miguel.biblioteca.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "librarians")
@AttributeOverride(name = "idUser", column = @Column(name = "id_librarian"))
public class ULibrarian extends User implements UserDetails {

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotEmpty
    @Column(nullable = false)
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
        name="librarian_role_junction",
        joinColumns = {@JoinColumn(name="id_librarian")},
        inverseJoinColumns = {@JoinColumn(name="id_role")}
    )
    private Set<Role> authorities;
    public ULibrarian(
            String firstName,
            String lastName,
            String userPhoneNumber,
            String userEmail,
            String password
    ){
        super(firstName, lastName, userPhoneNumber, userEmail);
        this.password = password;
        this.authorities = new HashSet<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
