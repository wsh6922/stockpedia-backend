package com.ktb.post.repository;

import com.ktb.post.dto.PostResponse;

public interface PostRepositoryCustom {

    PostResponse.DetailPostResponse findPostDetailByPostId(Long postId, Long MemberId);
}
