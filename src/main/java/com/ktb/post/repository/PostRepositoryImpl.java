package com.ktb.post.repository;

import com.ktb.member.domain.QMember;
import com.ktb.post.domain.QPost;
import com.ktb.post.dto.PostResponse;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public PostResponse.DetailPostResponse findPostDetailByPostId(Long postId, Long memberId) {
        try {
            PostResponse.DetailPostResponse response = jpaQueryFactory.select(
                            Projections.constructor(
                                    PostResponse.DetailPostResponse.class,
                                    QPost.post.id,
                                    QPost.post.title,
                                    QPost.post.content,
                                    QPost.post.viewCount,
                                    QPost.post.likeCount,
                                    QPost.post.commentCount,
                                    QPost.post.createdAt,
                                    QPost.post.updatedAt,
                                    QPost.post.member.id.eq(memberId),
                                    Projections.constructor(
                                            PostResponse.AuthorResponse.class,
                                            QMember.member.id,
                                            QMember.member.nickname
                                    )
                            )
                    )
                    .from(QPost.post)
                    .join(QPost.post.member, QMember.member)
                    .where(
                            QPost.post.id.eq(postId),
                            QPost.post.deletedAt.isNull()
                    )
                    .fetchOne();
            return response;
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return null;
    }
}
