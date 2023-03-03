package com.example.authorization.model;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
@Entity
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "authority")
    private String authority;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "authorities", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    private List<User> users;
}
