package com.ktb.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class PostResponse {

    @Getter
    @AllArgsConstructor
    public static class CreatePostResponse {

        private Long id;

        private String title;

        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdatePostResponse {
        private Long id;

        private String title;

        private String content;

        private LocalDateTime updatedAt;
    }

    @Getter
    @AllArgsConstructor
    public static class DetailPostResponse {

        private Long postId;

        private String title;

        private String content;

        private int viewCount;

        private int likeCount;

        private int commentCount;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private Boolean isMine;

        private AuthorResponse author;
    }

    @Getter
    @AllArgsConstructor
    public static class AuthorResponse {

        private Long memberId;

        private String nickname;
    }
}
