package com.ktb.comment.repository;

import com.ktb.comment.domain.Comment;
import com.ktb.comment.dto.CommentResponse;

public interface CommentRepositoryCustom {

    CommentResponse.CreateCommentResponse findCommentById(Long commentId, Long memberId);
}
