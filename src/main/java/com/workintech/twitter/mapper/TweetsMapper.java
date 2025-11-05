package com.workintech.twitter.mapper;

import com.workintech.twitter.dto.patchrequest.TweetsPatchRequestDto;
import com.workintech.twitter.dto.request.TweetsRequestDto;
import com.workintech.twitter.dto.response.TweetsResponseDto;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.exception.UsersNotFoundException;
import com.workintech.twitter.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TweetsMapper {


    private final UsersRepository userRepository;

    @Autowired
    public TweetsMapper(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Tweets toEntity(TweetsRequestDto tweetRequestDto){

        Tweets tweet = new Tweets();
        tweet.setContents(tweetRequestDto.contents());


        Users user = userRepository.findById(tweetRequestDto.userId())
                .orElseThrow(() -> new UsersNotFoundException(tweetRequestDto.userId() + " id'li kullanici bulunamadi"));

        tweet.setUser(user);

        return tweet;
    }

    public TweetsResponseDto toResponseDto(Tweets tweet){
        return new TweetsResponseDto(
                tweet.getTweetId(),
                tweet.getContents(),
                tweet.getCreatedAt(),
                tweet.getUser().getId(),
                tweet.getUser().getUsername()
        );
    }

    public Tweets updateEntity(Tweets tweetToUpdate, TweetsPatchRequestDto tweetPatchRequestDto){
        if(tweetPatchRequestDto.contents() != null)
            tweetToUpdate.setContents(tweetPatchRequestDto.contents());

        return tweetToUpdate;
    }

}
