package com.workintech.twitter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentsNotFoundException extends RuntimeException
{
    public CommentsNotFoundException(String message) {
        super(message);
    }
}
