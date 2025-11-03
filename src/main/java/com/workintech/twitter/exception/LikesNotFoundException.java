package com.workintech.twitter.exception;

import org.springframework.http.HttpStatus;

public class LikesNotFoundException extends TweetsException {
    public LikesNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);

    }
}
