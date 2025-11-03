package com.workintech.twitter.exception;

import org.springframework.http.HttpStatus;

public class TweetsNotFoundException extends TweetsException {
    public TweetsNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);

    }
}
