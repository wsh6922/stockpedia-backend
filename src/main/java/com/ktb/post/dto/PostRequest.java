package com.ktb.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class PostRequest {

    @Getter
    @Setter // @ModelAttribute
    public static class CreatePostRequest {

        private String title;

        private String content;
    }

    @Getter
    @Setter
    public static class UpdatePostRequest {

        private String title;

        private String content;
    }
}
