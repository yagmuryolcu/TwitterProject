package com.workintech.twitter.mapper;


import com.workintech.twitter.dto.patchrequest.LikesPatchRequestDto;
import com.workintech.twitter.dto.request.LikesRequestDto;
import com.workintech.twitter.dto.response.LikesResponseDto;
import com.workintech.twitter.entity.Likes;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.repository.TweetsRepository;
import com.workintech.twitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikesMapper {

    @Autowired
    private final UsersRepository userRepository;
    @Autowired
    private final TweetsRepository tweetRepository;


    public Likes toEntity(LikesRequestDto likesRequestDto, Users users, Tweets tweets){
        Likes likes = new Likes();
        Users user=userRepository.findById(likesRequestDto.userId())
                .orElseThrow(()-> new RuntimeException(likesRequestDto.userId() + "id'li kullanıcı bulunamadı."));
        likes.setUser(users);

        Tweets tweet = tweetRepository.findById(likesRequestDto.tweetId())
                .orElseThrow(() -> new RuntimeException(likesRequestDto.tweetId() + "id'li tweet bulunamadi"));

        likes.setTweet(tweet);

        return likes;
    }

    public LikesResponseDto toResponseDto(Likes like) {
        return new LikesResponseDto(
                like.getId(),
                like.getUser().getId(),
                like.getUser().getUsername(),
                like.getTweet().getTweetId(),
                like.getLikedAt()
        );
    }
    public Likes updateEntity(Likes likeToUpdate, LikesPatchRequestDto likePatchRequestDto){
        return likeToUpdate;
    }

}
