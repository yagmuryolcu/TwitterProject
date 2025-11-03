package com.workintech.twitter.mapper;


import com.workintech.twitter.dto.request.RetweetsRequestDto;
import com.workintech.twitter.dto.response.RetweetsResponseDto;
import com.workintech.twitter.entity.Retweets;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class RetweetsMapper {

    public Retweets toEntity(RetweetsRequestDto retweetsRequestDto, Users user, Tweets originalTweet) {
        Retweets retweet = new Retweets();
        retweet.setUser(user);
        retweet.setOriginalTweet(originalTweet);
        return retweet;
    }

    public RetweetsResponseDto toResponseDto(Retweets retweet) {
        return new RetweetsResponseDto(
                retweet.getId(),
                retweet.getUser().getId(),
                retweet.getUser().getUsername(),
                retweet.getOriginalTweet().getTweetId(),
                retweet.getCreatedAt()
        );
    }
}
