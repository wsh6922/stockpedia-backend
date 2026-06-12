package com.ktb.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentResponse {

    @Getter
    @AllArgsConstructor
    public static class CreateCommentResponse {

        private Long id;

        private Long postId;

        private String content;

        private LocalDateTime createdAt;

        private Long commentCount;

        private Boolean isMine;

        private AuthorResponse author;
    }

    @Getter
    @AllArgsConstructor
    public static class AuthorResponse {

        private Long memberId;

        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateCommentResponse {
        private Long id;

        private String content;

        // private LocalDateTime updateAt;
    }
}
