package com.workintech.twitter.dto.patchrequest;

import jakarta.validation.constraints.Size;

public record CommentsPatchRequestDto(
        @Size(max = 255, message = "Comment content cannot exceed 255 characters")
        String content
) {
}
