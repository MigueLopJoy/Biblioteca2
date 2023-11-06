package com.miguel.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.miguel.library.model.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER(Collections.emptySet()),

    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE,
                    CATALOGER_READ,
                    CATALOGER_UPDATE,
                    CATALOGER_DELETE,
                    CATALOGER_CREATE,
                    LIBRARIAN_READ,
                    LIBRARIAN_UPDATE,
                    LIBRARIAN_DELETE,
                    LIBRARIAN_CREATE
            )
    ),
    CATALOGER(
            Set.of(
                    CATALOGER_READ,
                    CATALOGER_UPDATE,
                    CATALOGER_DELETE,
                    CATALOGER_CREATE,
                    LIBRARIAN_READ,
                    LIBRARIAN_UPDATE,
                    LIBRARIAN_DELETE,
                    LIBRARIAN_CREATE
            )
    ),
    LIBRARIAN(
            Set.of(
                    LIBRARIAN_READ,
                    LIBRARIAN_UPDATE,
                    LIBRARIAN_DELETE,
                    LIBRARIAN_CREATE
            )
    ),
    READER(
            Set.of(
                    READER_READ,
                    READER_UPDATE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
