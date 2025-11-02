package com.workintech.twitter.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "twitter")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Roles {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Role name cannot be blank")
    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    private Set<Users> users = new HashSet<>();


    public void addUser(Users user) {
        if (user != null) {
            users.add(user);
            user.getRoles().add(this);
        }
    }

    public void removeUser(Users user) {
        if (user != null) {
            users.remove(user);
            user.getRoles().remove(this);
        }
    }
}
