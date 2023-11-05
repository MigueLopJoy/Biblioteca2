package com.miguel.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Comparator;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "librarians")
@AttributeOverride(name = "idUser", column = @Column(name = "id_librarian"))
public class ULibrarian extends User implements Comparator<ULibrarian> {
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

    public ULibrarian(String firstName, String lastName, String phoneNumber, String email, String password, Set<Role> authorities) {
        super(firstName, lastName, phoneNumber, email);
        this.password = password;
        this.authorities = authorities;
    }
    @Override
    public int compare(ULibrarian thisLibrarian, ULibrarian otherLibrarian) {
        Integer orderResult = compareRoles(thisLibrarian.getAuthorities(), otherLibrarian.getAuthorities());

        if (orderResult == 0) {
            orderResult = this.getFirstName().compareTo(otherLibrarian.getFirstName());

            if (orderResult == 0) {
                orderResult = this.getLastName().compareTo(otherLibrarian.getLastName());
            }
        }
        return orderResult;
    }

    private int compareRoles(Set<Role> roles1, Set<Role> roles2) {
        Integer priority1 = getMaxRolePriority(roles1);
        Integer priority2 = getMaxRolePriority(roles2);

        return Integer.compare(priority1, priority2);
    }

    private int getMaxRolePriority(Set<Role> roles) {
        Integer maxPriority = 0;

        for (Role role : roles) {
            Integer priority = getRolePriority(role);
            if (priority > maxPriority) {
                maxPriority = priority;
            }
        }
        return maxPriority;
    }
    private int getRolePriority(Role role) {
        switch (role.getIdRole()) {
            case 1:
                return 3;
            case 2:
                return 2;
            case 3:
                return 1;
            default:
                throw new IllegalArgumentException("Unknown role id");
        }
    }
}
