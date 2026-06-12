package com.ktb.post.repository;

import com.ktb.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Post findPostById(Long id);
}
