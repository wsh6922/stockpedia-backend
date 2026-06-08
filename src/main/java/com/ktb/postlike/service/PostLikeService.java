package com.ktb.postlike.service;

import com.ktb.global.exception.BusinessException;
import com.ktb.global.exception.ErrorCode;
import com.ktb.post.domain.Post;
import com.ktb.post.repository.PostRepository;
import com.ktb.postlike.dto.PostLikeResponse;
import com.ktb.postlike.repository.PostLikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;


    @Transactional
    public PostLikeResponse createPostLike(Long postId, Long currentMemberId) {

        Post post = postRepository.findByPostId(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        boolean isLike = postLikeRepository.existsByPostIdAndMemberId(postId, currentMemberId);


        if (isLike) {
            throw new BusinessException(ErrorCode.ALREADY_LIKED);
        }

        postLikeRepository.save(postId, currentMemberId);
        postRepository.incrementLikeCount(postId);

        return new PostLikeResponse(postId,
                currentMemberId,
                postRepository.findLikeCountByPostId(postId),
                true);
    }

    @Transactional
    public PostLikeResponse deletePostLike(Long postId, Long currentMemberId) {

        Post post = postRepository.findByPostId(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        boolean isLike = postLikeRepository.existsByPostIdAndMemberId(postId, currentMemberId);

        if (!isLike) {
            throw new BusinessException(ErrorCode.NOT_LIKED_YET);
        }

        postLikeRepository.delete(postId, currentMemberId);
        postRepository.decrementLikeCount(postId);

        return new PostLikeResponse(postId,
                currentMemberId,
                postRepository.findLikeCountByPostId(postId),
                false);
    }
}
