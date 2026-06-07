package com.ktb.postlike.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(PostLikeId.class)
public class PostLike {

    /**
     * @EmbeddedId
     * private PostLikeId id;
     */
    @Id
    private Long postId;

    @Id
    private Long memberId;

    private LocalDateTime createdAt;
}
