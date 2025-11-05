package com.workintech.twitter.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tweets", schema = "twitter")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "tweetId")
public class Tweets {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long tweetId;

    @Column(name = "content")
    @NotBlank
    @NotEmpty
    @NotNull
    @Size(max = 100, message = "Tweet content cannot exceed 100 characters")
    private String contents;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Users user;


    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Comments> comments = new HashSet<>();

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Likes> likes = new HashSet<>();

    @OneToMany(mappedBy = "originalTweet", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Retweets> retweets = new HashSet<>();

    // Helper Methods
    public void addComment(Comments comment) {
        if (comment != null && comment.getTweet() != null && comment.getTweet().equals(this)) {
            comments.add(comment);
        }
    }

    public void removeComment(Comments comment) {
        comments.remove(comment);
    }

    public void addLike(Likes like) {
        if (like != null && like.getTweet() != null && like.getTweet().equals(this)) {
            likes.add(like);
        }
    }

    public void removeLike(Likes like) {
        likes.remove(like);
    }

    public void addRetweet(Retweets retweet) {
        if (retweet != null && retweet.getOriginalTweet() != null && retweet.getOriginalTweet().equals(this)) {
            retweets.add(retweet);
        }
    }

    public void removeRetweet(Retweets retweet) {
        retweets.remove(retweet);
    }

}
