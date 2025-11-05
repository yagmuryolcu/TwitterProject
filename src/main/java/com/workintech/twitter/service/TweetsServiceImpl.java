package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.TweetsPatchRequestDto;
import com.workintech.twitter.dto.request.TweetsRequestDto;
import com.workintech.twitter.dto.response.TweetsResponseDto;
import com.workintech.twitter.entity.Tweets;
import com.workintech.twitter.exception.TweetsNotFoundException;
import com.workintech.twitter.mapper.TweetsMapper;
import com.workintech.twitter.repository.TweetsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TweetsServiceImpl implements TweetsService{

    @Autowired
    private final TweetsRepository tweetsRepository;
    @Autowired
    private final TweetsMapper tweetsMapper;

    @Override
    public List<TweetsResponseDto> getAll() {
        return tweetsRepository
                .findAll()
                .stream()
                .map(tweetsMapper::toResponseDto)
                .toList();
    }

    @Override
    public TweetsResponseDto findById(Long id) {
        Optional<Tweets> optionalTweets = tweetsRepository.findById(id);
        if (optionalTweets.isPresent()){
            Tweets tweets = optionalTweets.get();
            return tweetsMapper.toResponseDto(tweets);
        }

        throw  new TweetsNotFoundException(id + " 'id li tweet bulunamadı.");
    }

    @Override
    public TweetsResponseDto create(TweetsRequestDto tweetRequestDto) {
        return tweetsMapper.toResponseDto(tweetsRepository.save(tweetsMapper.toEntity(tweetRequestDto)));
    }

    @Override
    public TweetsResponseDto replaceOrCreate(Long id, TweetsRequestDto tweetRequestDto) {
        Optional<Tweets> optionalTweet = tweetsRepository.findById(id);

        if (optionalTweet.isPresent()) {
            Tweets existingTweet = optionalTweet.get();

            // Mevcut tweet'in create bilgisini koru
            existingTweet.setContents(tweetRequestDto.contents());
            existingTweet.setUser(
                    tweetsMapper.toEntity(tweetRequestDto).getUser()
            );

            // createdAt dokunmadan save et
            Tweets updatedTweet = tweetsRepository.save(existingTweet);
            return tweetsMapper.toResponseDto(updatedTweet);
        }

        // Eğer tweet yoksa yeni oluştur
        Tweets newTweet = tweetsMapper.toEntity(tweetRequestDto);
        Tweets savedTweet = tweetsRepository.save(newTweet);
        return tweetsMapper.toResponseDto(savedTweet);

    }

    @Override
    public TweetsResponseDto update(Long id, TweetsPatchRequestDto tweetPatchRequestDto) {
        Tweets tweetToUpdate = tweetsRepository
                .findById(id)
                .orElseThrow(()-> new TweetsNotFoundException(id + "id'li tweet bulunamadi"));

            tweetsMapper.updateEntity(tweetToUpdate,tweetPatchRequestDto);
            return tweetsMapper.toResponseDto(tweetsRepository.save(tweetToUpdate));
    }

    @Override
    public void deleteById(Long id) {
      /*  Tweets tweet = tweetsRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(id + " id'li tweet bulunamadı."));

        if (!tweet.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Bu tweet'i sadece sahibi silebilir.");
        }
*/
        tweetsRepository.deleteById(id);
    }
}
