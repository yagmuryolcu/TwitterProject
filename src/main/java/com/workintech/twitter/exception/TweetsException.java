package com.workintech.twitter.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TweetsException extends RuntimeException {
    private HttpStatus httpStatus;
    public TweetsException(String message,HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;
    }
}
