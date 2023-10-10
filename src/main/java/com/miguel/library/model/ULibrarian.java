package com.miguel.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "librarians")
@AttributeOverride(name = "idUser", column = @Column(name = "id_librarian"))
public class ULibrarian {
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
}
