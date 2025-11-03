package com.workintech.twitter.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp,
        List<String> details
) {
    public ErrorResponse(int status, String message, List<String> details) {
        this(status, message, LocalDateTime.now(), details);
    }

    public ErrorResponse(int status, String message, String detail) {
        this(status, message, LocalDateTime.now(), List.of(detail));
    }

}
