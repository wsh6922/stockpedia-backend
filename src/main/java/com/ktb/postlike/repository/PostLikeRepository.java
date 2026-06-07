package com.ktb.postlike.repository;

public interface PostLikeRepository {

    void save(Long postId, Long currentMemberId);

    void delete(Long postId, Long currentMemberId);

    boolean existsByPostIdAndMemberId(Long postId, Long currentMemberId);
}
