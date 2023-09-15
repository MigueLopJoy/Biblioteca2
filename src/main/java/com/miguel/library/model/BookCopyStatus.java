package com.miguel.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
public enum BookCopyStatus {
    IN_CIRCULATION("In circulation"),
    OUT_OF_CIRCULATION("Out of circulation"),
    LOST("Lost"),
    WITHDRAWN("Withdrawn");

    private final String status;
}
