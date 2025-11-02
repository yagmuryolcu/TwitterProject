package com.workintech.twitter.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "retweets", schema = "twitter")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")

public class Retweets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private Users user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_tweet_id")
    @ToString.Exclude
    private Tweets originalTweet;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
