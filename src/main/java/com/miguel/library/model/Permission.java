package com.miguel.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    MANAGER_READ("manager:read"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_CREATE("manager:create"),
    MANAGER_DELETE("manager:delete"),
    CATALOGER_READ("cataloger:read"),
    CATALOGER_UPDATE("cataloger:update"),
    CATALOGER_CREATE("cataloger:create"),
    CATALOGER_DELETE("cataloger:delete"),
    LIBRARIAN_READ("librarian:create"),
    LIBRARIAN_UPDATE("librarian:read"),
    LIBRARIAN_DELETE("librarian:update"),
    LIBRARIAN_CREATE("librarian:delete"),
    READER_READ("reader:create"),
    READER_UPDATE("reader:update")
    ;

    @Getter
    private final String permission;
}
