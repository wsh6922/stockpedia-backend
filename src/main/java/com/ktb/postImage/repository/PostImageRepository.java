package com.ktb.postImage.repository;

import com.ktb.postImage.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    void deleteByPostId(Long postId);
}
