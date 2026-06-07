package com.ktb.postlike.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class PostLikeId implements Serializable {

    private Long postId;

    private Long memberId;
}