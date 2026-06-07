package com.ktb.comment.service;

import com.ktb.comment.domain.Comment;
import com.ktb.comment.dto.CommentResponse;
import com.ktb.comment.repository.CommentRepository;
import com.ktb.global.exception.BusinessException;
import com.ktb.global.exception.ErrorCode;
import com.ktb.post.domain.Post;
import com.ktb.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;


    @Transactional
    public CommentResponse.CreateCommentResponse createComment(
            Long postId, Long currentMemberId, String content) {

        Post post = postRepository.findByPostId(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Comment comment = new Comment(
                null,
                postId,
                currentMemberId,
                content,
                null,
                null,
                null
        );

        postRepository.incrementCommentCount(postId);

        CommentResponse.CreateCommentResponse result = commentRepository.save(comment);

        return result;
    }

    @Transactional
    public CommentResponse.UpdateCommentResponse updateComment(
            Long postId, Long currentMemberId, Long commentId, String content) {

        Post post = postRepository.findByPostId(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Comment comment = commentRepository.findById(commentId);

        if (comment == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getMemberId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCode.COMMENT_UPDATE_ACCESS_FORBIDDEN);
        }

        commentRepository.updateById(commentId, content);

        Comment result = commentRepository.findById(commentId);

        return new CommentResponse.UpdateCommentResponse(
                result.getId(),
                result.getContent(),
                result.getUpdatedAt());
    }

    @Transactional
    public void deleteComment(Long postId, Long currentMemberId, Long commentId) {

        Post post = postRepository.findByPostId(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Comment comment = commentRepository.findById(commentId);

        if (comment == null || !comment.getPostId().equals(postId)) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getMemberId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCode.COMMENT_DELETE_ACCESS_FORBIDDEN);
        }

        commentRepository.deleteById(commentId);

        postRepository.decrementCommentCount(postId);
    }
}
