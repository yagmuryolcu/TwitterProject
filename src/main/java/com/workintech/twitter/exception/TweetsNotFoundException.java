package com.workintech.twitter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class TweetsNotFoundException extends TweetsException {
    public TweetsNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);

    }
}
