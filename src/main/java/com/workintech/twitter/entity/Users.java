package com.workintech.twitter.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "users",schema = "twitter")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @NotBlank
    @NotEmpty
    @Size(max = 50)
    private String username;


    @Email
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(max = 100)
    private String email;


    @NotNull
    @NotBlank
    @NotEmpty
    @Size(max = 100)
    private String password;


    @Column(name = "full_name")
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(max=100)
    private String fullName;

    @ToString.Exclude
    private String bio;

    @Column(name = "created_at",updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Tweets> tweets = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Comments> comments = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Likes> likes = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Retweets> retweets = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles = new HashSet<>();


    public void addTweet(Tweets tweet) {
        if (tweet != null && tweet.getUser() != null && tweet.getUser().equals(this)) {
            tweets.add(tweet);
        }
    }

    public void removeTweet(Tweets tweet) {
        tweets.remove(tweet);
    }

    public void addComment(Comments comment) {
        if (comment != null && comment.getUser() != null && comment.getUser().equals(this)) {
            comments.add(comment);
        }
    }

    public void removeComment(Comments   comment) {
        comments.remove(comment);
    }

    public void addRole(Roles role) {
        if (role != null) {
            roles.add(role);
            role.getUsers().add(this);
        }
    }

    public void removeRole(Roles role) {
        if (role != null) {
            roles.remove(role);
            role.getUsers().remove(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null || obj.getClass() != getClass())
            return false;

        Users user = (Users) obj;
        return user.id != null && user.id.equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
