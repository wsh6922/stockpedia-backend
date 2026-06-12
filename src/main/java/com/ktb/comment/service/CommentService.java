package com.ktb.comment.service;

import com.ktb.comment.domain.Comment;
import com.ktb.comment.dto.CommentResponse;
import com.ktb.comment.repository.CommentRepository;
import com.ktb.global.exception.BusinessException;
import com.ktb.global.exception.ErrorCode;
import com.ktb.member.domain.Member;
import com.ktb.member.repository.MemberRepository;
import com.ktb.post.domain.Post;
import com.ktb.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;


    @Transactional
    public CommentResponse.CreateCommentResponse createComment(
            Long postId, Long currentMemberId, String content) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Member member = memberRepository.findMemberById(currentMemberId);

        if (member == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        Comment comment = new Comment(
                post,
                member,
                content
        );

        post.addComment();

        Comment result = commentRepository.save(comment);

        return commentRepository.findCommentById(result.getId(), result.getMember().getId());
    }

    @Transactional
    public CommentResponse.UpdateCommentResponse updateComment(
            Long postId, Long currentMemberId, Long commentId, String content) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Comment comment = commentRepository.findCommentById(commentId);

        if (comment == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getMember().getId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCode.COMMENT_UPDATE_ACCESS_FORBIDDEN);
        }

        comment.update(content);

        return new CommentResponse.UpdateCommentResponse(
                comment.getId(),
                comment.getContent());
    }

    @Transactional
    public void deleteComment(Long postId, Long currentMemberId, Long commentId) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Comment comment = commentRepository.findCommentById(commentId);

        if (comment == null || !comment.getPost().getId().equals(postId)) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if (!comment.getMember().getId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCode.COMMENT_DELETE_ACCESS_FORBIDDEN);
        }

        commentRepository.deleteById(commentId);

        post.removeComment();
    }
}
