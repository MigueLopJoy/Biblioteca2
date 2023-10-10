package com.miguel.library.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id_role")
    private Integer idRole;

    @Column(nullable = false, unique = true)
    private String authority;
}
