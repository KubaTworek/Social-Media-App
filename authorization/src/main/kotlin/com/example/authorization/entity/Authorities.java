package com.example.authorization.entity;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String authority;

    @OneToMany(mappedBy = "authorities", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
        user.setAuthorities(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setAuthorities(null);
    }
}
